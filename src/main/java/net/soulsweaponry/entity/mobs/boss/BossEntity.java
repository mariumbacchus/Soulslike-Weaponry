package net.soulsweaponry.entity.mobs.boss;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.entity.boss.BossBar.Style;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.events.BossEvent;
import net.soulsweaponry.networking.PacketHelper;
import net.soulsweaponry.networking.S2C.packets.StopBossMusicS2C;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import net.soulsweaponry.util.IAnimatedDeath;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BossEntity<T extends Enum<T>> extends HostileEntity implements IAnimatedDeath {
    
    protected final ServerBossBar bossBar;
    private boolean hasUpdatedHealth = false;
    private boolean playingMusic = false;
    private int blockBreakingCooldown;
    private final Class<T> stateEnumClass;
    private int deathTicks;
    public int phaseTransitionTicks;
    /**
     * Map of states and the event tied to the state, such as Returning Knight's Unbreakable event
     * which only happens once when the boss is below 50% health
     */
    private final Map<T, BossEvent<T, ?>> events = new LinkedHashMap<>();
    @Nullable
    private BossEvent<T, ?> currentEvent;
    @Nullable
    private T currentEventState;
    private int eventTicks;
    private final List<UUID> attackingPlayers = new ArrayList<>();
    private long ticksWithoutPlayers;
    private long lastSavedWorldTime;
    private boolean checkOfflineHealing;
    private final Set<UUID> trackingPlayers = new HashSet<>();

    private static final TrackedData<Integer> STATES = DataTracker.registerData(BossEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> PHASE_2 = DataTracker.registerData(BossEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    // Attack status tick for debugging, do not use in prod due to spamming the server with packets!
    private static final TrackedData<Integer> ATTACK_STATUS = DataTracker.registerData(BossEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public BossEntity(EntityType<? extends HostileEntity> entityType, World world, Color barColor, Class<T> stateEnumClass) {
        super(entityType, world);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), barColor, Style.NOTCHED_10)).setDarkenSky(true);
        this.experiencePoints = this.getXp();
        this.stateEnumClass = stateEnumClass;
        this.ignoreCameraFrustum = true;
    }

    /**
     * To be used when dealing damage or setting the damage a projectile will do.
     * @return damage * EntityConfig.&lt;boss&gt;_damage_modifier
     */
    public abstract float getModifiedDamage(float damage);

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(STATES, 0);
        builder.add(PHASE_2, false);
        builder.add(ATTACK_STATUS, 0);
    }

    public double getAnimationSpeed() {
        return 1;
    }

    /**
     * Return true if attack status from attacks should be data-tracked to be client side too. WARNING: This will spam the server with packets!
     */
    public boolean isDebugMode() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    /**
     * Only set if {@link #isDebugMode()} returns true. Should not be used outside of debugging.
     */
    public void setAttackStatus(int attackStatus) {
        if (this.isDebugMode()) {
            // SoulsWeaponry.LOGGER.warn("Boss entity attack status data-tracker registered! Should not happen in production!");
            this.dataTracker.set(ATTACK_STATUS, attackStatus);
        }
    }

    /**
     * Returns only values if {@link #isDebugMode()} returns true. Should not be used outside of debugging.
     */
    public int getAttackStatus() {
        return this.isDebugMode() ? this.dataTracker.get(ATTACK_STATUS) : 0;
    }

    /**
     * Set the state the boss should perform and animate.
     * <p><b>NB! Value 0 should always be idle!</b></p>
     * Implementation for animation must be handled independently.
     */
    public void setState(T state) {
        this.dataTracker.set(STATES, state.ordinal());
    }

    /**
     * Get the state the boss should perform and animate.
     * <p><b>NB! Value 0 should always be idle!</b></p>
     * Falls back to 0 if the saved state is out of bounds.
     */
    public T getState() {
        T[] values = this.stateEnumClass.getEnumConstants();
        int index = this.dataTracker.get(STATES);
        if (index < 0 || index >= values.length) {
            return values[0];
        }
        return values[index];
    }

    /**
     * Returns whether the boss is performing the state supplied or not.
     */
    public boolean isState(T state) {
        return this.getState() == state;
    }

    /**
     * Returns whether the boss is performing any of the states provided.
     */
    @SafeVarargs
    public final boolean isAnyState(T... states) {
        T currentState = this.getState();
        for (T state : states) {
            if (currentState == state) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the state with index 0 which should always be idle.
     */
    public boolean isIdle() {
        return this.dataTracker.get(STATES) == 0;
    }

    /**
     * Sets the state of the boss to 0 which should always be idle.
     */
    public void setIdle() {
        this.dataTracker.set(STATES, 0);
    }

    /**
     * Set whether the boss is phase 2 or not, will naturally only
     * affect those that has phase 2 model and attacks.
     */
    public void setPhaseTwo(boolean bl) {
        this.dataTracker.set(PHASE_2, bl);
    }

    /**
     * Gets whether the boss is phase 2 or not, doesn't affect
     * bosses that doesn't do anything with this method.
     */
    public boolean isPhaseTwo() {
        return this.dataTracker.get(PHASE_2);
    }

    public abstract boolean isSpawning();

    @Override
    protected void mobTick() {
        if (!this.hasUpdatedHealth) {
            this.setHealth(this.getMaxHealth());
            this.hasUpdatedHealth = true;
        }
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        this.tickPassiveHealing();
    }

    /**
     * Basic re-usable initiate phase 2 method to be used inside {@link #mobTick()} that heals the entity to
     * full after a delay and sets the phase 2 datatracked value to true when modelChangeTick value is reached.
     * <p>
     *     NB! Remember to call this.isState(INITIATE_PHASE_2) in an if statement so this doesn't
     *     trigger all the time!
     * </p>
     */
    public void initiatePhaseTwo(int maxTicks, int modelChangeTick, int delayTicks) {
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 30));
        this.phaseTransitionTicks++;
        if (this.phaseTransitionTicks >= delayTicks) {
            int maxHealTicks = maxTicks - delayTicks;
            float healPerTick = this.getMaxHealth() / maxHealTicks;
            this.heal(healPerTick);
        }
        if (this.phaseTransitionTicks == modelChangeTick) {
            this.setPhaseTwo(true);
        }
        if (this.phaseTransitionTicks >= maxTicks) {
            this.setIdle();
        }
    }

    //TODO stop music if players are far enough away & stop minecraft music if playing
    public void tryToPlayBossMusic() {
        if (this.hasBossMusic() && !this.getWorld().isClient && !playingMusic) {
            this.getWorld().playSound(null, this.getBlockPos(), this.getBossMusic(), SoundCategory.MUSIC, 1f, 1f);
            this.playingMusic = true;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isSpawning()) {
            this.tryToPlayBossMusic();
        }
        if (!this.getWorld().isClient) {
            this.tickBossEvents();
        }
    }

    /**
     * Meant to be used within the goal/ai class for returning out of
     * the tick() method early in situations where for example the
     * boss is dead or is initiating phase 2.
     */
    public boolean shouldNotTickAttack() {
        return this.isDead() || this.isSpawning() || this.isEventRunning();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.getHealth() - amount > 0f) {
            this.tryToPlayBossMusic();
        }
        if (this.blockBreakingCooldown <= 0) {
            this.blockBreakingCooldown = 20;
        }
        if (!this.getWorld().isClient && source.getAttacker() instanceof PlayerEntity player && !this.attackingPlayers.contains(player.getUuid())) {
            this.attackingPlayers.add(player.getUuid());
        }
        return super.damage(source, amount);
    }

    public abstract SoundEvent getBossMusic();
    public abstract boolean hasBossMusic();

    public boolean isPlayingMusic() {
        return playingMusic;
    }

    public void setPlayingMusic(boolean playingMusic) {
        this.playingMusic = playingMusic;
    }

    /**
     * Returns a list of all players that attacked the boss, permanently increasing the boss' difficulty for this fight.
     */
    public List<UUID> getAttackingPlayersUUIDs() {
        return this.attackingPlayers;
    }

    public int getAttackingPlayersAmount() {
        return this.attackingPlayers.size();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("HasUpdatedHealth", this.hasUpdatedHealth);
        nbt.putBoolean("PhaseTwo", this.isPhaseTwo());

        // Store completed events so they don't trigger again
        NbtCompound completedEvents = new NbtCompound();
        for (Map.Entry<T, BossEvent<T, ?>> entry : this.events.entrySet()) {
            completedEvents.putBoolean(entry.getKey().name(), entry.getValue().hasCompleted());
        }
        nbt.put("CompletedBossEvents", completedEvents);

        NbtList players = new NbtList();
        for (UUID uuid : this.attackingPlayers) {
            NbtCompound entry = new NbtCompound();
            entry.putUuid("UUID", uuid);
            players.add(entry);
        }
        nbt.put("AttackingPlayers", players);

        // Save one persistent state if it should be saved, such as INITIATE_PHASE_2 for some bosses
        T state = this.getState();
        if (this.shouldPersistState(state)) {
            nbt.putString("PersistentBossState", state.name());
            nbt.putInt("PhaseTransitionTicks", this.phaseTransitionTicks);
        }

        // Save world time and ticks since not being around players to calculate healing when no player is around
        nbt.putLong("TicksWithoutPlayers", this.ticksWithoutPlayers);
        nbt.putLong("LastSavedWorldTime", this.getWorld().getTime());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (this.hasCustomName()) {
           this.bossBar.setName(this.getDisplayName());
        }
        if (nbt.contains("HasUpdatedHealth")) {
            this.hasUpdatedHealth = nbt.getBoolean("HasUpdatedHealth");
        }
        if (nbt.contains("PhaseTwo")) {
            this.setPhaseTwo(nbt.getBoolean("PhaseTwo"));
        }
        NbtCompound completedEvents = nbt.getCompound("CompletedBossEvents");
        for (Map.Entry<T, BossEvent<T, ?>> entry : this.events.entrySet()) {
            entry.getValue().setCompleted(completedEvents.getBoolean(entry.getKey().name()));
        }
        this.attackingPlayers.clear();
        NbtList players = nbt.getList("AttackingPlayers", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < players.size(); i++) {
            NbtCompound entry = players.getCompound(i);
            if (entry.containsUuid("UUID")) {
                UUID uuid = entry.getUuid("UUID");
                if (!this.attackingPlayers.contains(uuid)) {
                    this.attackingPlayers.add(uuid);
                }
            }
        }
        // Try to save the persistent state such as INITIATE_PHASE_2
        if (nbt.contains("PersistentBossState")) {
            try {
                T state = Enum.valueOf(this.stateEnumClass, nbt.getString("PersistentBossState"));
                if (this.shouldPersistState(state)) {
                    this.setState(state);
                    this.onPersistentStateLoaded(state);
                }
            } catch (Exception ignored) {}
        }
        // Load values to check if no player has been around and if it should heal for time being away from players
        if (nbt.contains("LastSavedWorldTime")) {
            this.ticksWithoutPlayers = nbt.getLong("TicksWithoutPlayers");
            this.lastSavedWorldTime = nbt.getLong("LastSavedWorldTime");
            this.checkOfflineHealing = true;
        }
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
        this.trackingPlayers.add(player.getUuid());
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
        this.trackingPlayers.remove(player.getUuid());
        if (this.trackingPlayers.isEmpty() && this.shouldPersistState(this.getState())) {
            this.onPersistentStateLoaded(this.getState());
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (this.getBossMusic() != null && this.hasBossMusic() && this.getWorld() instanceof ServerWorld serverWorld) {
            PacketHelper.sendToAllPlayersS2C(serverWorld, this.getBlockPos(), new StopBossMusicS2C(this.getBossMusic().getId()));
        }
    }

    @Override
    protected boolean shouldAlwaysDropXp() {
        return true;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks >= this.getScaledMaxTicks(this.getTicksUntilDeath()) && !this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            if (!this.getDeathParticles().isEmpty()) {
                CustomDeathHandler.deathExplosionEvent(this.getWorld(), this.getPos(), SoundRegistry.DAWNBREAKER_EVENT, this.getDeathParticles());
            }
            this.remove(RemovalReason.KILLED);
        }
    }

    /**
     * Should return a list of the particles that should be in the spheric explosion upon death & despawn, fetched in {@link #updatePostDeath()}.
     * <p>Example list: {@code List.of(ParticleTypes.LARGE_SMOKE, ParticleRegistry.NIGHTFALL_PARTICLE)}</p>
     */
    public abstract List<ParticleEffect> getDeathParticles();

    @Override
    public abstract int getTicksUntilDeath();

    @Override
    public int getDeathTicks() {
        return this.deathTicks;
    }

    @Override
    public abstract boolean isFireImmune();

    @Override
    public abstract boolean hasInvertedHealingAndHarm();

    @Override
    public abstract boolean disablesShield();

    public abstract int getXp();

    /**
     * Scales ticks based on the animation speed, i.e. if animation speed is 0.5
     * then the input ticks is doubled.
     */
    public int getScaledMaxTicks(int ticks) {
        return (int) Math.ceil(ticks / this.getAnimationSpeed());
    }

    /**
     * Should be called during damage method for bosses that are projectile immune to check whether the entity
     * should damage the boss or not.
     */
    public boolean isProjectileWhitelisted(DamageSource source) {
        if (source.getSource() != null) {
            return this.isProjectileWhitelisted(source.getSource());
        }
        return false;
    }

    /**
     * Should be called during damage method for bosses that are projectile immune to check whether the entity
     * should damage the boss or not.
     */
    public boolean isProjectileWhitelisted(Entity entity) {
        Identifier projectileId = EntityType.getId(entity.getType());
        if (projectileId == null) {
            return false;
        }

        EntityType<?> type = entity.getType();
        for (String raw : this.getWhitelistedProjectiles()) {
            if (raw == null || raw.isEmpty()) {
                continue;
            }

            String s = raw.trim();
            // Tag form, i.e: "#minecraft:arrows" or "#soulsweapons:something"
            if (s.startsWith("#")) {
                String tagStr = s.substring(1); // drop '#'
                Identifier tagId = tagStr.contains(":")
                        ? Identifier.of(tagStr)
                        : Identifier.of(projectileId.getNamespace(), tagStr);
                TagKey<EntityType<?>> tagKey = TagKey.of(RegistryKeys.ENTITY_TYPE, tagId);
                if (type.getRegistryEntry().isIn(tagKey)) {
                    return true;
                }
                continue;
            }

            // Normal ID form, i.e: "arrow", "minecraft:arrow", "big_moonlight_projectile", "soulsweapons:big_moonlight_projectile"
            Identifier whitelistId = s.contains(":")
                    ? Identifier.of(s) // full ID given
                    : Identifier.of(projectileId.getNamespace(), s); // use projectile's namespace by default

            if (whitelistId.equals(projectileId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Should be overwritten to get a list of projectiles that should damage the boss regardless of abilities the boss
     * has, such as projectile immunity.
     */
    public String[] getWhitelistedProjectiles() {
        return new String[0];
    }

    /**
     * @return Array of status effect identifiers that the boss is immune to
     */
    public abstract String[] getBlacklistedStatusEffects();

    @Override
    public boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source) {
        String effectId = Objects.requireNonNull(Registries.STATUS_EFFECT.getId(effect.getEffectType().value())).toString();
        for (String blacklisted : this.getBlacklistedStatusEffects()) {
            if (blacklisted.equals(effectId)) {
                return false;
            }
        }
        return super.addStatusEffect(effect, source);
    }

    /**
     * Call during mobTick() to break blocks around the boss when taking damage to make way.
     */
    public void breakSurroundingBlocks() {
        if (EntityConfig.can_bosses_break_blocks) {
            if (this.blockBreakingCooldown > 0) {
                this.blockBreakingCooldown--;
                if (this.blockBreakingCooldown == 0 && this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    boolean blockBroken = false;
                    int j = MathHelper.floor(this.getWidth() / 2.0F + 1.0F);
                    int k = MathHelper.floor(this.getHeight());
                    for (BlockPos blockPos : BlockPos.iterate(this.getBlockX() - j, this.getBlockY(), this.getBlockZ() - j, this.getBlockX() + j, this.getBlockY() + k, this.getBlockZ() + j)) {
                        BlockState blockState = this.getWorld().getBlockState(blockPos);
                        if (canDestroy(blockState, blockPos)) {
                            blockBroken = this.getWorld().breakBlock(blockPos, true, this) || blockBroken;
                        }
                    }
                    if (blockBroken) {
                        this.getWorld().syncWorldEvent(null, WorldEvents.WITHER_BREAKS_BLOCK, this.getBlockPos(), 0);
                    }
                }
            }
        }
    }

    public boolean canDestroy(BlockState block, BlockPos pos) {
        return !block.isAir() && !(block.getBlock() instanceof BlockWithEntity) && block.getHardness(this.getWorld(), pos) >= 0.0F && !block.isIn(BlockTags.WITHER_IMMUNE);
    }

    /**
     * Add an event that should trigger at a certain point only once.
     * For example, Returning Knight has the Unbreakable event triggering
     * once when below 50% health, indicating that it gained increased resistence.
     * @param state state the event is tied to, for example States.UNBREAKABLE
     * @param event event object
     */
    public void registerEvent(T state, BossEvent<T, ?> event) {
        this.events.put(state, event);
    }

    public boolean isEventRunning() {
        return this.currentEvent != null;
    }

    /**
     * Override to exclude additional states, such as a phase transition.
     * Do not use shouldNotTickAttack() here because it also blocks during events.
     */
    protected boolean canRunBossEvents() {
        return this.isAlive() && !this.isSpawning();
    }

    private void tickBossEvents() {
        if (!this.canRunBossEvents()) {
            this.cancelBossEvent();
            return;
        }
        // Another system changed the state, for example a phase transition
        if (this.currentEvent != null && !this.currentEvent.ownsState(this.getState(), this.currentEventState)) {
            this.cancelBossEvent();
            return;
        }
        if (this.currentEvent == null) {
            for (Map.Entry<T, BossEvent<T, ?>> entry : this.events.entrySet()) {
                BossEvent<T, ?> event = entry.getValue();
                if (event.hasCompleted() || !event.canTrigger()) {
                    continue;
                }
                this.currentEvent = event;
                this.currentEventState = entry.getKey();
                this.eventTicks = 0;
                this.getNavigation().stop();
                this.setState(entry.getKey());
                event.onStart();
                break;
            }
        }
        BossEvent<T, ?> event = this.currentEvent;
        if (event == null) {
            return;
        }
        event.tickEvent(++this.eventTicks);
        if (this.isDebugMode()) {
            this.setAttackStatus(this.eventTicks);
        }
        if (this.eventTicks >= this.getScaledMaxTicks(event.getEventLength())) {
            T finishedState = this.currentEventState;
            this.currentEvent = null;
            this.currentEventState = null;
            this.eventTicks = 0;
            event.setCompleted(true);
            event.onFinish();

            // Preserve any different state selected by onFinish()
            if (event.ownsState(this.getState(), finishedState)) {
                this.setIdle();
            }
        }
    }

    public void cancelBossEvent() {
        BossEvent<T, ?> event = this.currentEvent;
        if (event == null) {
            return;
        }
        T cancelledState = this.currentEventState;
        this.currentEvent = null;
        this.currentEventState = null;
        this.eventTicks = 0;
        event.onCancel();

        // Preserve external state changes, such as entering phase two
        if (event.ownsState(this.getState(), cancelledState)) {
            this.setIdle();
        }
    }

    /**
     * Returns the distance to an entity in blocks.
     */
    public double distanceToTargetBlocks(Entity target) {
        return Math.sqrt(this.squaredDistanceTo(target));
    }

    /**
     * Checks whether an entity is within the given distance in blocks.
     */
    public boolean isWithinDistance(Entity target, double blocks) {
        return this.squaredDistanceTo(target) <= blocks * blocks;
    }

    /**
     * Override to specify states that should persist after unloading, such as INITIATE_PHASE_2.
     */
    protected abstract boolean shouldPersistState(T state);

    /**
     * Override to set values upon the persisted state is loaded, such as resetting
     * phaseTransitionTicks to 0 and setting back to phase 1 during INITIATE_PHASE_2
     * reload.
     */
    protected abstract void onPersistentStateLoaded(T state);

    /**
     * Returns whether a persistent state should continue ticking, such as INITIATE_PHASE_2.
     * Meant for re-syncing states after de-loading the boss and reloading it again
     * like entering its chunk after dying while its transitioning phases.
     */
    protected boolean canTickPersistentState() {
        return !this.trackingPlayers.isEmpty();
    }

    /**
     * Heal the boss when no player is within a certain range and the boss is idle.
     * Meant to combat cheesing so one can't just do damage, die, respawn, repeat.
     */
    protected void tickPassiveHealing() {
        final PassiveHealingConfig config = this.getPassiveHealingConfig();
        if (!config.canPassivelyHeal() || !(this.getWorld() instanceof ServerWorld world) || this.isDead()) {
            return;
        }
        if (this.checkOfflineHealing) {
            this.checkOfflineHealing = false;
            long elapsed = Math.max(0, world.getTime() - this.lastSavedWorldTime);
            if (this.isIdle()) {
                long previous = this.ticksWithoutPlayers;
                this.ticksWithoutPlayers += elapsed;
                this.healForElapsedTicks(previous, this.ticksWithoutPlayers);
            }
        }
        boolean playersNearby = world.getPlayers().stream().anyMatch(player -> !player.isSpectator()
                && this.isWithinDistance(player, config.healRange));
        if (playersNearby) {
            this.ticksWithoutPlayers = 0;
            return;
        }
        if (this.isIdle()) {
            long previous = this.ticksWithoutPlayers++;
            this.healForElapsedTicks(previous, this.ticksWithoutPlayers);
        } else {
            this.ticksWithoutPlayers = 0;
        }
    }

    private void healForElapsedTicks(long previous, long current) {
        final PassiveHealingConfig config = this.getPassiveHealingConfig();
        long before = Math.max(0, previous / 20 - (config.healDelay - 1) / 20);
        long after = Math.max(0, current / 20 - (config.healDelay - 1) / 20);
        long healingTicks = after - before;
        if (healingTicks > 0) {
            this.heal(this.getMaxHealth() * config.healPercent * healingTicks);
        }
    }

    /**
     * Returns the config values to determine if, when and how much the boss can heal
     * if players have not been around it for a while.
     */
    protected abstract PassiveHealingConfig getPassiveHealingConfig();

    /**
     * Build values for passive healing. if the boss can heal, delay until it can heal, range players need to be within
     * to stop healing and how much to heal.
     * @param canPassivelyHeal if the boss can heal passively while not around players
     * @param healDelay delay until the healing can start
     * @param healRange range in blocks players must be within to stop the healing
     * @param healPercent percent of max health to heal
     */
    public record PassiveHealingConfig(boolean canPassivelyHeal, int healDelay, float healRange, float healPercent) {}
}
