package net.soulsweaponry.entity.mobs;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.entity.boss.BossBar.Style;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.StopBossMusicS2C;
import net.soulsweaponry.util.IAnimatedDeath;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BossEntity extends HostileEntity implements IAnimatedDeath {

    protected final ServerBossBar bossBar;
    private boolean hasUpdatedHealth = false;
    private boolean playingMusic = false;
    private int blockBreakingCooldown;

    protected BossEntity(EntityType<? extends HostileEntity> entityType, World world, Color barColor) {
        super(entityType, world);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), barColor, Style.NOTCHED_10)).setDarkenSky(true);
        this.experiencePoints = this.getXp();
    }

    public abstract int getXp();

    @Override
    protected void mobTick() {
        if (!this.hasUpdatedHealth) {
            this.setHealth(this.getMaxHealth());
            this.hasUpdatedHealth = true;
        }
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
    }
    //TODO stop music if players are far enough away & stop minecraft music if playing
    public void tryToPlayBossMusic() {
        if (this.hasBossMusic() && !this.getWorld().isClient && !playingMusic) {
            this.getWorld().playSound(null, this.getBlockPos(), this.getBossMusic(), SoundCategory.MUSIC, 1f, 1f);
            this.playingMusic = true;
        }
    }

    //TODO always render as long as not in idle? (meaning always render when doing attack so no desync happens)

    @Override
    public void tick() {
        super.tick();
        if (this.isSpawning()) {
            this.tryToPlayBossMusic();
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.getHealth() - amount > 0f) {
            this.tryToPlayBossMusic();
        }
        if (this.blockBreakingCooldown <= 0) {
            this.blockBreakingCooldown = 20;
        }
        return super.damage(source, amount);
    }

    public abstract boolean isSpawning();

    public abstract SoundEvent getBossMusic();
    public abstract boolean hasBossMusic();

    public boolean isPlayingMusic() {
        return playingMusic;
    }

    public void setPlayingMusic(boolean playingMusic) {
        this.playingMusic = playingMusic;
    }

    /**
     * Gets the reduced cooldown in ticks for the boss' next attack based on number of attackers,
     * increased if the attackers are players. Will make huge raids more interesting.
     * <p> Returns {@code 5} for each player, and {@code 2} for other entities, such as
     * summons.
     */
    public int getReducedCooldownAttackers() {
        List<LivingEntity> attackers = this.getAttackers();
        int reduced = 0;
        for (LivingEntity entity : attackers) {
            reduced += entity instanceof PlayerEntity ? 5 : 2;
        }
        return reduced;
    }

    /**
     * Get all the attacking entities within 10 blocks radius from the bounding box of the boss.
     */
    public List<LivingEntity> getAttackers() {
        Box box = this.getBoundingBox().expand(10);
        List<Entity> entities = this.getWorld().getOtherEntities(this, box);
        ArrayList<LivingEntity> attackers = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && ((LivingEntity)entity).getAttacking() == this) {
                attackers.add((LivingEntity) entity);
            }
        }
        return attackers;
    }

    /**
     * Returns a list of all attacking {@code players} only within 10 blocks radius of the boss' bounding box.
     */
    public List<PlayerEntity> getAttackingPlayers() {
        List<LivingEntity> entities = this.getAttackers();
        ArrayList<PlayerEntity> players = new ArrayList<>();
        for (LivingEntity entity : entities) {
            if (entity instanceof PlayerEntity) {
                players.add((PlayerEntity) entity);
            }
        }
        return players;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("HasUpdatedHealth", this.hasUpdatedHealth);
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
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        this.setDeath();
        if (this.getBossMusic() != null && this.hasBossMusic() && this.getWorld() instanceof ServerWorld) {
            ModMessages.sendToAllPlayers(new StopBossMusicS2C(this.getBossMusic().getId()));
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
    public abstract void updatePostDeath();

    @Override
    public abstract int getTicksUntilDeath();

    @Override
    public abstract int getDeathTicks();

    @Override
    public abstract void setDeath();

    @Override
    public abstract boolean isFireImmune();

    @Override
    public abstract boolean isUndead();

    @Override
    public abstract boolean disablesShield();

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
                        ? Identifier.tryParse(tagStr)
                        : Identifier.of(projectileId.getNamespace(), tagStr);
                TagKey<EntityType<?>> tagKey = TagKey.of(RegistryKeys.ENTITY_TYPE, tagId);
                if (type.getRegistryEntry().isIn(tagKey)) {
                    return true;
                }
                continue;
            }

            // Normal ID form, i.e: "arrow", "minecraft:arrow", "big_moonlight_projectile", "soulsweapons:big_moonlight_projectile"
            Identifier whitelistId = s.contains(":")
                    ? Identifier.tryParse(s) // full ID given
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
        String effectId = Objects.requireNonNull(Registries.STATUS_EFFECT.getId(effect.getEffectType())).toString();
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
        if (ConfigConstructor.can_bosses_break_blocks) {
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
}