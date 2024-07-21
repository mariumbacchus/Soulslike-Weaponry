package net.soulsweaponry.entity.ai.goal;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.util.BlackflameSnakeLogic;
import net.soulsweaponry.entity.util.DeathSpiralLogic;
import net.soulsweaponry.entity.mobs.*;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.entity.projectile.NightsEdge;
import net.soulsweaponry.entity.projectile.NoDragWitherSkull;
import net.soulsweaponry.entity.projectile.invisible.BlackflameSnakeEntity;
import net.soulsweaponry.entity.projectile.invisible.FogEntity;
import net.soulsweaponry.entity.projectile.invisible.InvisibleEntity;
import net.soulsweaponry.entity.projectile.invisible.NightWaveEntity;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class NightProwlerGoal extends MeleeAttackGoal {
    private final NightProwler boss;
    private int attackCooldown;
    private int specialCooldown;
    private int attackStatus;
    private int attackLength;
    private boolean hasExploded;
    //private int flightTimer; // Testing only.
    private int changeFlightTargetTimer;
    private Vec3d flightPosAdder;
    private int bonusDmg;
    private int flipCounter;

    public NightProwlerGoal(NightProwler boss, double speed, boolean pauseWhenMobIdle) {
        super(boss, speed, pauseWhenMobIdle);
        this.boss = boss;
    }

    /*
     * When issuing an attack, it periodically syncronises with the boss how much time that
     * is left before the animation is over. This way, if it is interrupted, the animation will go on,
     * and the next attack will have its cooldown set to how much time was left until the animation
     * is finished.
     *
     * Example:
     * Day Stalker uses an attack with 60 ticks as animation time, but the target gets in creative mode
     * or dies at 25 ticks. 35 ticks is saved in the boss class and reduced in the tickMovement or mobTick
     * method. When the AI kicks up again, it checks whether that saved cooldown is being reduced or not
     * before doing any further action.
     */

    @Override
    public boolean canStart() {
        if (this.boss.isFlying() && this.boss.getTarget() != null) {
            return true;
        }
        return super.canStart();
    }

    @Override
    public void stop() {
        super.stop();
        int remainingTicks = this.attackLength - this.attackStatus;
        if (remainingTicks > 0) this.boss.setWaitAnimation(true);
        this.boss.setRemainingAniTicks(remainingTicks);
        this.attackCooldown = 20;
        this.specialCooldown = 20;
        this.attackStatus = 0;
        this.attackLength = 0;
        this.hasExploded = false;
        this.boss.setFlying(false);
        this.changeFlightTargetTimer = 0;
        this.boss.setParticleState(0);
        this.bonusDmg = 0;
        this.flipCounter = 0;
    }

    private void checkAndSetAttack(LivingEntity target) {
        double distanceToEntity = this.boss.squaredDistanceTo(target);
        int rand = this.boss.getRandom().nextInt(NightProwler.ATTACKS_LENGTH);
        NightProwler.Attacks attack = NightProwler.Attacks.values()[rand];
        switch (attack) {
            case TRINITY -> {
                if (this.isInMeleeRange(target) || this.boss.isFlying()) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case REAPING_SLASH -> {
                if (!this.boss.isPhaseTwo() && distanceToEntity < 300D) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case NIGHTS_EMBRACE -> {
                if (this.boss.isPhaseTwo() ? !this.isSummonsAlive() : this.specialCooldown <= 0) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case RIPPLE_FANG, SOUL_REAPER -> {
                if (this.isInMeleeRange(target)) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case DARKNESS_RISE -> {
                if (this.isInMeleeRange(target) && !this.boss.getDarknessRise()) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case BLADES_REACH -> {
                if (distanceToEntity < 200D) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case DIMINISHING_LIGHT, ENGULF -> this.boss.setAttackAnimation(attack);
            case BLACKFLAME_SNAKE -> {
                if (this.boss.getBlackflameSnakeLogic() == null) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case ECLIPSE -> {
                if (this.boss.isPhaseTwo() && this.specialCooldown <= 0) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case LUNAR_DISPLACEMENT -> {
                if (this.boss.isPhaseTwo()) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case DEATHBRINGERS_GRASP -> {
                if (distanceToEntity < 81D) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            default -> this.boss.setAttackAnimation(NightProwler.Attacks.IDLE);
        }
    }

    @Override
    public void tick() {
        if (this.boss.getRemainingAniTicks() > 0 || this.boss.getAttackAnimation().equals(NightProwler.Attacks.SPAWN)) {
            return;
        }
        if (this.boss.isInitiatingPhaseTwo()) {
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 255));
            return;
        }
        this.attackCooldown = Math.max(this.attackCooldown - 1, 0);
        this.specialCooldown = Math.max(this.specialCooldown - 1, 0);
        if (!this.boss.isFlying() && this.boss.shouldChaseTarget()) {
            super.tick();
        }
        //NOTE: For testing purposes only.
        /*
        *
        if (!this.boss.isPhaseTwo()) {
            if (this.flightTimer == 0 && this.boss.getRandom().nextInt(4 ) == 1) {
                this.flightTimer = 1;
                this.boss.setFlying(true);
            }
            if (this.flightTimer >= 1) {
                this.flightTimer++;
                if (flightTimer >= 300) {
                    this.boss.setFlying(false);
                    this.flightTimer = 0;
                }
            }
        }
        * */
        LivingEntity target = this.boss.getTarget();
        if (target != null) {
            if (this.boss.isFlying() && !this.boss.getAttackAnimation().equals(NightProwler.Attacks.ECLIPSE)) {
                this.moveAboveTarget(target);
            }
            //double distanceToEntity = this.boss.squaredDistanceTo(target);
            if (this.attackCooldown <= 0 && this.boss.getAttackAnimation().equals(NightProwler.Attacks.IDLE)) {
                this.checkAndSetAttack(target);
            }
            boolean phase2 = this.boss.isPhaseTwo();
            switch (this.boss.getAttackAnimation()) {
                case TRINITY -> {
                    this.attackLength = phase2 ? 90 : 110;
                    this.trinity();
                }
                case REAPING_SLASH -> {
                    this.attackLength = 70;
                    this.reapingSlash(target);
                }
                case NIGHTS_EMBRACE -> {
                    this.attackLength = phase2 ? 90 : 80;
                    this.nightsEmbrace();
                }
                case RIPPLE_FANG -> {
                    this.attackLength = phase2 ? 45 : 30;
                    this.rippleFang(target);
                }
                case BLADES_REACH -> {
                    this.attackLength = phase2 ? 60 : 40;
                    this.bladesReach(target);
                }
                case SOUL_REAPER -> {
                    this.attackLength = phase2 ? 150 : 95;
                    this.soulReaper(target);
                }
                case DIMINISHING_LIGHT -> {
                    this.attackLength = phase2 ? 60 : 40;
                    this.diminishingLight(target);
                }
                case DARKNESS_RISE -> {
                    this.attackLength = phase2 ? 70 : 40;
                    this.darknessRise();
                }
                case ECLIPSE -> {
                    this.attackLength = 270;
                    this.eclipse();
                }
                case ENGULF -> {
                    this.attackLength = phase2 ? 90 : 42;
                    this.engulf(target);
                }
                case BLACKFLAME_SNAKE -> {
                    this.attackLength = phase2 ? 140 : 60;
                    this.blackflameSnake(target);
                }
                case LUNAR_DISPLACEMENT -> {
                    this.attackLength = 70;
                    this.lunarDisplacement(target);
                }
                case DEATHBRINGERS_GRASP -> {
                    this.attackLength = phase2 ? 60 : 35;
                    this.deathsGrasp(target);
                }
            }
        }
    }

    private Vec3d randomizeVecAdder() {
        return new Vec3d(this.boss.getRandom().nextBetween(3, 10) * (this.boss.getRandom().nextBoolean() ? -1 : 1), 0, this.boss.getRandom().nextBetween(3, 10) * (this.boss.getRandom().nextBoolean() ? -1 : 1));
    }

    private void moveAboveTarget(LivingEntity target) {
        this.boss.getLookControl().lookAt(target);
        this.boss.lookAtEntity(target, this.boss.getMaxLookYawChange(), this.boss.getMaxLookPitchChange());
        Vec3d vec3d = this.boss.getVelocity().multiply(0.8f, 0.6f, 0.8f);
        double d = vec3d.y;
        boolean bl = this.boss.getAttackAnimation().equals(NightProwler.Attacks.TRINITY);
        if (this.boss.getY() < target.getY() || this.boss.getY() < target.getY() + (bl ? 9f : 6f)) {
            d = Math.max(0.0, d);
            d += 0.3 - d * (double)0.6f;
        }
        vec3d = new Vec3d(vec3d.x, d, vec3d.z);
        if (this.flightPosAdder != null) {
            this.changeFlightTargetTimer++;
            if (this.changeFlightTargetTimer >= 40) {
                this.flightPosAdder = this.randomizeVecAdder();
                this.changeFlightTargetTimer = 0;
            }
        } else {
            this.flightPosAdder = this.randomizeVecAdder();
        }
        Vec3d vec3d2 = new Vec3d(target.getX() - this.boss.getX() + (bl ? 0 : this.flightPosAdder.x), 0.0, target.getZ() - this.boss.getZ() + (bl ? 0 : this.flightPosAdder.z));
        if (vec3d2.horizontalLengthSquared() > 9.0) {
            Vec3d vec3d3 = vec3d2.normalize();
            vec3d = vec3d.add(vec3d3.x * 0.3 - vec3d.x * 0.6, 0.0, vec3d3.z * 0.3 - vec3d.z * 0.6);
        }
        this.boss.setVelocity(vec3d);
    }

    private void checkAndReset(int attackCooldown, int specialCooldown) {
        if (this.attackStatus > this.attackLength) {
            this.attackStatus = 0;
            this.attackCooldown = MathHelper.floor((double)attackCooldown
                    * (this.boss.isPhaseTwo() ? ConfigConstructor.night_prowler_cooldown_modifier_phase_2 : ConfigConstructor.night_prowler_cooldown_modifier_phase_1));
            if (specialCooldown != 0) this.specialCooldown = MathHelper.floor((double)specialCooldown
                    * (this.boss.isPhaseTwo() ? ConfigConstructor.night_prowler_special_cooldown_modifier_phase_2 : ConfigConstructor.night_prowler_special_cooldown_modifier_phase_1));
            this.attackLength = 0;
            this.boss.setAttackAnimation(NightProwler.Attacks.IDLE);
            this.boss.setChaseTarget(true);
            this.hasExploded = false;
            this.boss.setParticleState(0);
            this.bonusDmg = 0;
            this.flipCounter = 0;
        }
    }

    private float getModifiedDamage(float damage) {
        return (damage + this.bonusDmg) * ConfigConstructor.night_prowler_damage_modifier * (this.boss.isEmpowered() ? 1.25f : 1) * (this.boss.getDarknessRise() ? 1.25f : 1);
    }

    private boolean damageTarget(LivingEntity target, float damage) {
        if (this.boss.isPartner(target)) {
            return false;
        }
        if (target.damage(this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(damage))) {
            if (this.boss.isEmpowered()) {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0));
                if (target.isDead()) {
                    this.boss.heal(target.getMaxHealth());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected void attack(LivingEntity target, double squaredDistance) {}

    protected boolean isInMeleeRange(LivingEntity target) {
        double distanceToEntity = this.boss.squaredDistanceTo(target);
        return distanceToEntity <= this.getSquaredMaxAttackDistance(target);
    }

    /**
     * Easily accessible playSound function.
     * @param pos Position to play sound on, if set to null, will play on the boss' position
     * @param sound Sound to be played
     * @param volume Volume
     * @param pitch Pitch
     */
    private void playSound(@Nullable BlockPos pos, SoundEvent sound, float volume, float pitch) {
        if (pos == null) pos = this.boss.getBlockPos();
        this.boss.getWorld().playSound(null, pos, sound, SoundCategory.HOSTILE, volume, pitch);
    }

    private void trinity() {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        boolean phase2 = this.boss.isPhaseTwo();
        int stopFlying = phase2 ? 61 : 75;
        int min = phase2 ? 65 : 77;
        int max = phase2 ? 76 : 95;
        boolean bl1 = this.attackStatus >= (phase2 ? 16 : 26) && this.attackStatus <= (phase2 ? 60 : 68);
        boolean bl2 = this.attackStatus >= (phase2 ? 38 : 45) && this.attackStatus <= (phase2 ? 43 : 51);
        if (bl1 && this.attackStatus % 3 == 0 && !bl2) {
            this.boss.playSound(SoundRegistry.SCYTHE_SWIPE, 1f, (float)this.boss.getRandom().nextBetween(6, 10)/10f);
        }
        if (phase2) {
            if (this.attackStatus == 5) {
                this.boss.playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1f, 1f);
                this.boss.setFlying(true);
                this.boss.addVelocity(0, 0.75f, 0);
            }
        }
        DayStalker partner;
        if (this.boss.isFlying() && this.attackStatus == stopFlying && !this.boss.getWorld().isClient && (partner = this.boss.getPartner((ServerWorld) this.boss.getWorld())) != null) {
            this.boss.setFlying(false);
            partner.setFlying(true);
            partner.flightTimer = ConfigConstructor.duo_fight_time_before_switch;
            this.boss.setVelocity(0, -2f, 0);
        }
        if (!this.hasExploded && this.attackStatus >= min && this.attackStatus <= max && this.boss.isOnGround()) {
            Vec3d vec = this.boss.getRotationVector().multiply(4D).add(this.boss.getPos());
            BlockPos pos = new BlockPos((int) vec.getX(), this.boss.getBlockY(), (int) vec.getZ());
            Box box = new Box(pos).expand(3D);
            this.aoe(box, 40f, 3f, true);
            this.hasExploded = true;
            this.boss.setTargetPos(pos);
            this.boss.setParticleState(1);
            Vec3d target = Vec3d.ofCenter(pos);
            ParticleHandler.flashParticle(this.boss.getWorld(), target.getX(), target.getY(), target.getZ(), new ParticleHandler.RGB(142, 107, 1), 10f);
            ParticleHandler.flashParticle(this.boss.getWorld(), target.getX(), target.getY(), target.getZ(), new ParticleHandler.RGB(72, 0, 140), 2f);
            ParticleHandler.particleOutburstMap(this.boss.getWorld(), 300, pos.getX(), pos.getY(), pos.getZ(), ParticleEvents.OBLITERATE_MAP, 1f);
            this.boss.playSound(SoundRegistry.TRINITY, 1f, 1f);
            if (this.boss.isPhaseTwo()) {
                this.trinityShockwave();
            }
        } else {
            this.boss.setParticleState(0);
        }
        this.checkAndReset(this.boss.isPhaseTwo() ? 40 : 60, 0);
    }

    private void trinityShockwave() {
        float r = 1f;
        for (int theta = 0; theta < 360; theta += 15) {
            double x0 = this.boss.getX();
            double z0 = this.boss.getZ();
            double x = x0 + r * Math.cos(theta * Math.PI / 180);
            double z = z0 + r * Math.sin(theta * Math.PI / 180);
            NightWaveEntity wave = new NightWaveEntity(EntityRegistry.NIGHT_WAVE, this.boss.getWorld());
            wave.setPos(x, this.boss.getY(), z);
            wave.setVelocity(this.boss, 0, theta - 90, 0.0f, 1.5f, 0f);
            wave.setDamage(this.getModifiedDamage(20f));
            wave.setOwner(this.boss);
            this.boss.getWorld().spawnEntity(wave);
        }
    }

    public void aoe(Box box, float damage, float knockback, boolean knockbackAway, StatusEffect[] effects) {
        for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, box)) {
            if (entity instanceof LivingEntity target) {
                if (this.damageTarget(target, damage) && knockback > 0) {
                    double x = target.getX() - this.boss.getX();
                    double z = target.getZ() - this.boss.getZ();
                    int mod = knockbackAway ? 1 : -1;
                    target.takeKnockback(knockback, -x * mod, -z * mod);
                    for (StatusEffect effect : effects) {
                        target.addStatusEffect(new StatusEffectInstance(effect, 60, 0));
                    }
                }
            }
        }
    }

    public void aoe(Box box, float damage, float knockback, boolean knockbackAway) {
        this.aoe(box, damage, knockback, knockbackAway, new StatusEffect[0]);
    }

    private void reapingSlash(LivingEntity target) {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        double x = (target.getX() - this.boss.getX()) / 10;
        double y = (target.getY() - this.boss.getY()) / 10;
        double z = (target.getZ() - this.boss.getZ()) / 10;
        if (this.attackStatus >= 26 && this.attackStatus <= 40) {
            this.boss.setVelocity(x, y, z);
            if (this.attackStatus % 2 == 0) {
                this.aoe(this.boss.getBoundingBox().expand(2D), 25f, 0, true);
            }
        }
        this.checkAndReset(5, 0);
    }

    private void nightsEmbrace() {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        boolean phase2 = this.boss.isPhaseTwo();
        int trigger = phase2 ? 75 : 61;
        boolean bl1 = this.attackStatus >= (phase2 ? 18 : 13) && this.attackStatus <= (phase2 ? 46 : 56);
        if (bl1 && this.attackStatus % 3 == 0) {
            this.boss.playSound(SoundRegistry.SCYTHE_SWIPE, 1f, (float)this.boss.getRandom().nextBetween(6, 10)/10f);
        }
        if (!this.boss.getWorld().isClient && this.attackStatus == trigger) {
            DayStalker partner = phase2 ? null : this.boss.getPartner((ServerWorld) this.boss.getWorld());
            BlockPos start = partner == null || partner.isFlying() ? this.boss.getBlockPos() : partner.getBlockPos();
            int amount = phase2 ? 4 : 8;
            int[] list = new int[amount];
            int i = 0;
            for (int theta = 0; theta < 360; theta += 360/amount) {
                int r = 5;
                double x0 = start.getX();
                double z0 = start.getZ();
                double x = x0 + r * Math.cos(theta * Math.PI / 180);
                double z = z0 + r * Math.sin(theta * Math.PI / 180);
                BlockPos pos = BlockPos.ofFloored(x, start.getY() - 3, z);
                Remnant entity;
                if (phase2) {
                    entity = new Forlorn(EntityRegistry.FORLORN, this.boss.getWorld());
                    HashMap<Enchantment, Integer> map = new HashMap<>();
                    map.put(Enchantments.PROTECTION, 2);
                    map.put(Enchantments.VANISHING_CURSE, 1);
                    Forlorn.initEquip(entity, map);
                } else {
                    entity = new SoulReaperGhost(EntityRegistry.SOUL_REAPER_GHOST, this.boss.getWorld());
                }
                pos = this.getNonAirPos(pos, this.boss.getWorld());
                entity.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
                ParticleHandler.particleOutburstMap(this.boss.getWorld(), 100, pos.getX(), pos.getY(), pos.getZ(), ParticleEvents.CONJURE_ENTITY_MAP, 1f);
                DeathSpiralEntity spiral = new DeathSpiralEntity(this.boss.getWorld(), entity.getPos(), 1f);
                spiral.setPosition(entity.getPos());
                this.boss.getWorld().spawnEntity(spiral);
                this.playSound(pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, 0.7f, 1f);
                this.boss.getWorld().spawnEntity(entity);
                list[i] = entity.getId();
                i++;
            }
            this.boss.setAliveSummons(list);
            this.boss.playSound(SoundRegistry.SCYTHE_SWIPE, 1f, 0.75f);
        }
        this.checkAndReset(phase2 ? 10 : 60, phase2 ? 120 : 200);
    }

    @SuppressWarnings("deprecation")
    private BlockPos getNonAirPos(BlockPos start, World world) {
        if (!world.getBlockState(start).blocksMovement() && !world.getBlockState(start.up()).blocksMovement()) {
            return start;
        } else {
            return getNonAirPos(start.add(0, 1, 0), world);
        }
    }

    /**
     * Returns whether previous summons are still alive. Used mainly in NIGHTS_EMBRACE to check if
     * the boss can spawn more entities.
     * <p>NOTE: It only tracks id, not Uuid, so the list will be refreshed over new game runs.</p>
     * @return If summons still live
     */
    private boolean isSummonsAlive() {
        int[] list = this.boss.getAliveSummonsList();
        int[] arr = new int[list.length];
        for (int i = 0; i < list.length; i++) {
            if (this.boss.getWorld().getEntityById(list[i]) != null) {
                arr[i] = list[i];
            }
        }
        this.boss.setAliveSummons(arr);
        int i = 0;
        for (int d : arr) {
            i += d;
        }
        return i != 0;
    }

    private void rippleFang(LivingEntity target) {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        boolean phase2 = this.boss.isPhaseTwo();
        if (this.attackStatus == 1) {
            this.boss.playSound(SoundRegistry.NIGHT_SKULL_DIE, 1f, 0.75f);
        }
        if (this.attackStatus == (phase2 ? 29 : 18)) {
            this.castSpell(target, true);
        }
        this.checkAndReset(5, 0);
    }

    protected void castSpell(LivingEntity livingEntity, boolean ripple) {
        Vec3d start = this.boss.getPos();
        float r;
        double maxY = Math.min(livingEntity.getY(), this.boss.getY());
        double y = Math.max(livingEntity.getY(), this.boss.getY()) + 1.0;
        float f = (float)MathHelper.atan2(livingEntity.getZ() - this.boss.getZ(), livingEntity.getX() - this.boss.getX());
        if (ripple) {
            float yaw;
            int i;
            for (int waves = 0; waves < 5; waves++) {
                for (i = 0; i < 360; i += MathHelper.floor((30f * (this.boss.isPhaseTwo() ? 2 : 1)) / (waves + 1f))) {
                    r = 1.5f + waves * 1.75f;
                    yaw = (float) (f + i * Math.PI / 180f);
                    double x0 = start.getX();
                    double z0 = start.getZ();
                    double x = x0 + r * Math.cos(i * Math.PI / 180);
                    double z = z0 + r * Math.sin(i * Math.PI / 180);
                    this.conjureFangs(x, z, maxY, y, yaw, 3 * (waves + 1));
                }
            }
        } else {
            for (int i = 0; i < 20; ++i) {
                double h = 1.25 * (double)(i + 1);
                this.conjureFangs(this.boss.getX() + (double)MathHelper.cos(f) * h, this.boss.getZ() + (double)MathHelper.sin(f) * h, maxY, y, f, i);
            }
        }
    }

    private void conjureFangs(double x, double z, double maxY, double y, float yaw, int warmup) {
        BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
        boolean bl = false;
        double d = 0.0;
        do {
            VoxelShape voxelShape;
            BlockPos blockPos2;
            if (!this.boss.getWorld().getBlockState(blockPos2 = blockPos.down()).isSideSolidFullSquare(this.boss.getWorld(), blockPos2, Direction.UP)) continue;
            if (!this.boss.getWorld().isAir(blockPos) && !(voxelShape = this.boss.getWorld().getBlockState(blockPos).getCollisionShape(this.boss.getWorld(), blockPos)).isEmpty()) {
                d = voxelShape.getMax(Direction.Axis.Y);
            }
            bl = true;
            break;
        } while ((blockPos = blockPos.down()).getY() >= MathHelper.floor(maxY) - 1);
        if (bl) {
            if (this.boss.isPhaseTwo()) {
                NightsEdge edge = new NightsEdge(EntityRegistry.NIGHTS_EDGE, this.boss.getWorld());
                edge.setOwner(this.boss);
                edge.setWarmup(warmup);
                edge.setDamage(this.getModifiedDamage(15.69f));
                edge.setYaw(yaw * 57.295776F);
                edge.setPos(x, (double)blockPos.getY() + d, z);
                this.boss.getWorld().spawnEntity(edge);
            } else {
                this.boss.getWorld().spawnEntity(new EvokerFangsEntity(this.boss.getWorld(), x, (double)blockPos.getY() + d, z, yaw, warmup, this.boss));
            }
        }
    }

    private void bladesReach(LivingEntity target) {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        boolean phase2 = this.boss.isPhaseTwo();
        if (this.attackStatus == (phase2 ? 16 : 23)) {
            this.castSpell(target, false);
            this.boss.playSound(SoundRegistry.SCYTHE_SWIPE, 1f, 0.7f);
        }
        if (phase2 && this.attackStatus == 43) {
            Vec3d vec = new Vec3d(target.getX() - (this.boss.getX()), target.getEyeY() - this.boss.getBodyY(1f), target.getZ() - this.boss.getZ());
            this.shootSplitSkulls(vec, 5, 1.75f);
        }
        this.checkAndReset(this.boss.isPhaseTwo() ? 1 : 40, 0);
    }

    private void shootSplitSkulls(Vec3d target, int amount, float velocity) {
        this.shootSplitProjectile(target, amount, velocity, EntityRegistry.NO_DRAG_WITHER_SKULL);
    }

    private void shootSplitProjectile(Vec3d target, int amount, float velocity, EntityType<? extends ProjectileEntity> type) {
        int m = MathHelper.floor(amount/2f);
        this.playSound(null, SoundEvents.ENTITY_WITHER_SHOOT, 1f, 1f);
        for (int i = -m; i <= m; i++) {
            Vec3d vec = target.rotateY((float) Math.toRadians(5 * i));
            ProjectileEntity entity = type.create(this.boss.getWorld());
            if (entity != null) {
                entity.setPos(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
                entity.setVelocity(vec.getX(), vec.getY(), vec.getZ(), velocity, 1f);
                entity.setOwner(this.boss);
                this.boss.getWorld().spawnEntity(entity);
            }
        }
    }

    private void shootSplitMoonlight(Vec3d target, int amount) {
        int m = MathHelper.floor(amount/2f);
        this.playSound(null, SoundRegistry.MOONLIGHT_BIG_EVENT, 1f, 1f);
        for (int i = -m; i <= m; i++) {
            Vec3d vec = target.rotateY((float) Math.toRadians(8 * i));
            MoonlightProjectile entity = new MoonlightProjectile(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE, this.boss.getWorld());
            entity.setAgeAndPoints(30, 150, 4);
            entity.setDamage(this.getModifiedDamage(20f));
            entity.setPos(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
            entity.setVelocity(vec.getX(), vec.getY(), vec.getZ(), 1.75f, 1f);
            entity.setOwner(this.boss);
            this.boss.getWorld().spawnEntity(entity);
        }
    }

    private void shootSplitBoth(Vec3d target, int amount) {
        int m = MathHelper.floor(amount/2f);
        this.playSound(null, SoundRegistry.MOONLIGHT_BIG_EVENT, 1f, 1f);
        this.playSound(null, SoundEvents.ENTITY_WITHER_SHOOT, 1f, 1f);
        for (int i = -m; i <= m; i++) {
            Vec3d vec = target.rotateY((float) Math.toRadians(8 * i));
            ProjectileEntity entity;
            boolean bl = (this.flipCounter % 2 == 0) == (i % 2 == 0);
            if (bl) {
                entity = new NoDragWitherSkull(EntityType.WITHER_SKULL, this.boss.getWorld());
            } else {
                entity = new MoonlightProjectile(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE, this.boss.getWorld());
                ((MoonlightProjectile) entity).setAgeAndPoints(30, 150, 4);
                ((MoonlightProjectile) entity).setDamage(this.getModifiedDamage(20f));
            }
            entity.setPos(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
            entity.setVelocity(vec.getX(), vec.getY(), vec.getZ(), 1.75f, 1f);
            entity.setOwner(this.boss);
            this.boss.getWorld().spawnEntity(entity);
        }
        this.flipCounter++;
    }

    private void soulReaper(LivingEntity target) {
        this.attackStatus++;
        this.boss.getLookControl().lookAt(target);
        this.boss.lookAtEntity(target, this.boss.getMaxLookYawChange(), this.boss.getMaxLookPitchChange());
        this.boss.getNavigation().stop();
        Vec3d vel = new Vec3d(target.getX() - (this.boss.getX()), target.getEyeY() - this.boss.getBodyY(1f), target.getZ() - this.boss.getZ());
        boolean phase2 = this.boss.isPhaseTwo();
        Vec3d vec = this.boss.getRotationVector().multiply(4D).add(this.boss.getPos());
        vec = new Vec3d(vec.getX(), target.getY(), vec.getZ());
        this.boss.setTargetPos(BlockPos.ofFloored(vec).withY(this.boss.getBlockY()));
        HashMap<Integer, Box> map = new HashMap<>();
        if (!phase2) {
            map.put(14, new Box(BlockPos.ofFloored(vec)).expand(2D));
            map.put(29, new Box(BlockPos.ofFloored(vec)).expand(2D));
            map.put(49, this.boss.getBoundingBox().expand(2D));
            map.put(72, this.boss.getBoundingBox().expand(2D));
        } else {
            map.put(17, new Box(BlockPos.ofFloored(vec)).expand(2D));
            map.put(28, new Box(BlockPos.ofFloored(vec)).expand(2D));
            map.put(50, this.boss.getBoundingBox().expand(2D));
            map.put(61, new Box(BlockPos.ofFloored(vec)).expand(2D));
            map.put(74, new Box(BlockPos.ofFloored(vec)).expand(2D));
            map.put(96, this.boss.getBoundingBox().expand(2D));
            map.put(109, new Box(BlockPos.ofFloored(vec)).expand(2D));
            map.put(128, new Box(BlockPos.ofFloored(vec)).expand(3D));
        }
        for (int frame : map.keySet()) {
            if (this.attackStatus == frame) {
                this.playSound(null, SoundRegistry.SCYTHE_SWIPE, 1f, (float) this.boss.getRandom().nextBetween(6, 10) / 10f);
                this.aoe(map.get(frame), 20f, 0.4f, true);
                this.bonusDmg += phase2 ? 3 : 5;
                this.boss.setVelocity(vel.multiply(0.1D));
                if (this.attackStatus == 61) {
                    this.shootSplitSkulls(vel, 3, 1.5f);
                }
                if (this.attackStatus == 74) {
                    this.boss.setParticleState(2);
                    this.boss.playSound(SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1f, 1f);
                }
                if (this.attackStatus == 109) {
                    this.shootSplitMoonlight(vel, 3);
                }
                if (this.attackStatus == 128) {
                    this.boss.setParticleState(3);
                }
            }
        }
        if (this.attackStatus != 74 && this.attackStatus != 128) {
            this.boss.setParticleState(0);
        }
        this.checkAndReset(this.boss.isPhaseTwo() ? 5 : 40, 0);
    }

    private void diminishingLight(LivingEntity target) {
        this.attackStatus++;
        this.boss.getLookControl().lookAt(target);
        this.boss.lookAtEntity(target, this.boss.getMaxLookYawChange(), this.boss.getMaxLookPitchChange());
        this.boss.getNavigation().stop();
        boolean phase2 = this.boss.isPhaseTwo();
        int[] frames = {20, 30, 40};
        Vec3d vec = new Vec3d(target.getX() - (this.boss.getX()), target.getEyeY() - this.boss.getBodyY(1f), target.getZ() - this.boss.getZ());
        if (!phase2) {
            if (this.attackStatus == 25) {
                this.shootSplitMoonlight(vec, 5);
            }
        } else {
            for (int f : frames) {
                if (f == this.attackStatus) {
                    this.shootSplitBoth(vec, 5);
                }
            }
        }
        this.checkAndReset(this.boss.isFlying() ? 40 : 5, 0);
    }

    private void darknessRise() {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        if (this.attackStatus == 24) {
            this.boss.playSound(SoundRegistry.DARKNESS_RISE, 1f, 1f);
            this.boss.setDarknessRise(true);
        }
        this.checkAndReset(10, 0);
    }

    private void eclipse() {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        if (this.attackStatus <= 1) {
            this.boss.playSound(SoundRegistry.PARTNER_DIES, 0.8f, 0.8f);
            this.playSound(this.boss.getTarget() != null ? this.boss.getTarget().getBlockPos() : this.boss.getBlockPos(), SoundRegistry.PARTNER_DIES, 0.8f, 0.8f);
            this.boss.setFlying(true);
            this.boss.addVelocity(0, 0.5f, 0);
        }
        if (this.attackStatus == 40) {
            this.boss.setParticleState(4);
        }
        if (this.attackStatus >= 50 && this.attackStatus <= 220) {
            for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, this.boss.getBoundingBox().expand(35))) {
                if (this.attackStatus % 12 == 0 && entity instanceof LivingEntity target) {
                    Vec3d vec = new Vec3d(target.getX() - (this.boss.getX()), target.getEyeY() - this.boss.getBodyY(1f), target.getZ() - this.boss.getZ());
                    this.shootSplitProjectile(vec, 3, 1.75f, EntityRegistry.NIGHT_SKULL);
                    if (target.isDead() && target.deathTime < 2) {
                        this.boss.heal(ConfigConstructor.night_prowler_eclipse_healing);
                        DeathSpiralEntity spiral = new DeathSpiralEntity(this.boss.getWorld(), target.getPos(), 1f);
                        spiral.setPosition(target.getPos());
                        this.boss.getWorld().spawnEntity(spiral);
                    }
                }
            }
        }
        if (this.attackStatus >= 240) {
            this.boss.setParticleState(0);
            this.boss.setFlying(false);
        }
        this.checkAndReset(20, 300);
    }

    private void engulf(LivingEntity target) {
        this.attackStatus++;
        this.boss.getLookControl().lookAt(target);
        this.boss.lookAtEntity(target, this.boss.getMaxLookYawChange(), this.boss.getMaxLookPitchChange());
        this.boss.getNavigation().stop();
        boolean phase2 = this.boss.isPhaseTwo();
        if (this.attackStatus == (phase2 ? 19 : 21)) {
            Vec3d vec = new Vec3d(target.getX() - (this.boss.getX()), target.getY() - this.boss.getY(), target.getZ() - this.boss.getZ());
            FogEntity entity = new FogEntity(EntityRegistry.FOG_ENTITY, this.boss.getWorld());
            entity.setPos(this.boss.getX(), this.boss.getY(), this.boss.getZ());
            entity.setVelocity(vec.getX(), vec.getY(), vec.getZ(), 1f, 1f);
            entity.setOwner(this.boss);
            this.boss.getWorld().spawnEntity(entity);
            this.boss.playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1f, 1f);
        }
        if (phase2 && this.attackStatus == 45) {
            Vec3d vec = target.getRotationVector().multiply(-3).add(target.getPos());
            this.boss.teleportTo(vec.getX(), target.getY(), vec.getZ());
            this.boss.lookAtEntity(target, 180, 180);
        }
        if (this.attackStatus == 54) {
            Vec3d vec = this.boss.getRotationVector().multiply(4D).add(this.boss.getPos());
            vec = new Vec3d(vec.getX(), this.boss.getY(), vec.getZ());
            this.boss.setTargetPos(BlockPos.ofFloored(vec));
            this.aoe(new Box(BlockPos.ofFloored(vec)).expand(2D), 30f, 1.5f, true);
            this.boss.setParticleState(2);
            this.boss.playSound(SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1f, 1f);
        } else {
            this.boss.setParticleState(0);
        }
        this.checkAndReset(this.boss.isPhaseTwo() ? 10 : 50, 0);
    }

    private void blackflameSnake(LivingEntity target) {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        this.boss.getLookControl().lookAt(target);
        this.boss.lookAtEntity(target, this.boss.getMaxLookYawChange()*2, this.boss.getMaxLookPitchChange()*2);
        boolean phase2 = this.boss.isPhaseTwo();
        if (!phase2 && this.attackStatus == 31) {
            this.boss.playSound(SoundRegistry.NIGHT_SKULL_DIE, 1f, 0.75f);
            BlackflameSnakeEntity entity = new BlackflameSnakeEntity(EntityRegistry.BLACKFLAME_SNAKE_ENTITY, this.boss.getWorld());
            entity.setDamage(this.getModifiedDamage(30f));
            Vec3d vel;
            Vec3d pos;
            DayStalker partner;
            if (this.boss.isFlying() && (partner = this.boss.getPartner((ServerWorld) this.boss.getWorld())) != null) {
                vel = new Vec3d(target.getX() - (partner.getX()), target.getY() - partner.getY(), target.getZ() - partner.getZ()).multiply(0.2f);
                pos = partner.getPos();
                entity.setOwner(partner);
            } else {
                vel = new Vec3d(target.getX() - (this.boss.getX()), target.getY() - this.boss.getY(), target.getZ() - this.boss.getZ()).multiply(0.2f);
                pos = this.boss.getPos();
                entity.setOwner(this.boss);
            }
            entity.setPosition(pos);
            entity.setVelocity(vel);
            entity.setTargetUuid(target.getUuid());
            this.boss.getWorld().spawnEntity(entity);
        }
        if (phase2) {
            if (this.attackStatus <= 1) {
                this.boss.playSound(SoundRegistry.NIGHT_SKULL_DIE, 1f, 0.75f);
                this.boss.setFlying(true);
                this.boss.addVelocity(0, 0.5f, 0);
            }
            if (this.attackStatus == 84) {
                this.boss.setFlying(false);
                this.boss.setVelocity(0, -1f, 0);
            }
            if (!this.hasExploded && this.boss.isOnGround() && this.attackStatus >= 88 && this.attackStatus <= 110) {
                this.hasExploded = true;
                this.boss.setTargetPos(this.boss.getBlockPos());
                this.boss.setParticleState(2);
                this.aoe(this.boss.getBoundingBox().expand(2D), 35f, 2f, true);
                this.boss.setBlackflameSnakeLogic(new BlackflameSnakeLogic(
                        this.boss.getPos(), target.getPos(), 10f, 1, this.boss.getYaw(), this.boss.getUuid()
                ));

                BlackflameSnakeEntity entity = new BlackflameSnakeEntity(EntityRegistry.BLACKFLAME_SNAKE_ENTITY, this.boss.getWorld());
                entity.setPosition(this.boss.getPos());
                entity.setDamage(this.getModifiedDamage(30f));
                entity.setVelocity(new Vec3d(target.getX() - (this.boss.getX()), target.getY() - this.boss.getY(), target.getZ() - this.boss.getZ()).multiply(0.2f));
                entity.setTargetUuid(target.getUuid());
                this.boss.getWorld().spawnEntity(entity);
            } else {
                this.boss.setParticleState(0);
            }
        }
        this.checkAndReset(this.boss.isFlying() ? 60 : (this.boss.isPhaseTwo() ? 5 : 30), 0);
    }

    private void lunarDisplacement(LivingEntity target) {
        this.attackStatus++;
        this.boss.getLookControl().lookAt(target);
        this.boss.lookAtEntity(target, this.boss.getMaxLookYawChange(), this.boss.getMaxLookPitchChange());
        this.boss.getNavigation().stop();
        if (this.attackStatus == 1) {
            this.playSound(target.getBlockPos(), SoundRegistry.NIGHT_PROWLER_SCREAM, 1f, 1f);
        }
        if (this.attackStatus == 35) {
            Vec3d vec = target.getRotationVector().multiply(-2).add(target.getPos());
            this.boss.teleportTo(vec.getX(), target.getY(), vec.getZ());
            this.boss.lookAtEntity(target, 180, 180);
        }
        if (this.attackStatus == 42) {
            this.aoe(this.boss.getBoundingBox().expand(3D), 35f, 2f, true);
            ParticleHandler.particleSphereList(this.boss.getWorld(), 1000, this.boss.getX(), this.boss.getY(), this.boss.getZ(), 1f, ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.LARGE_SMOKE);
            this.boss.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1f, 1f);
            this.boss.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1f, 0.7f);
        }
        this.checkAndReset(10, 0);
    }

    private void deathsGrasp(LivingEntity target) {
        this.attackStatus++;
        this.boss.getLookControl().lookAt(target);
        this.boss.lookAtEntity(target, this.boss.getMaxLookYawChange(), this.boss.getMaxLookPitchChange());
        this.boss.getNavigation().stop();
        boolean phase2 = this.boss.isPhaseTwo();
        if (this.attackStatus == (phase2 ? 18 : 23)) {
            Vec3d out = this.boss.getRotationVector().multiply(5.5D, 0, 5.5D).add(this.boss.getPos().getX(), target.getY(), this.boss.getPos().getZ());
            Box box = new Box(BlockPos.ofFloored(out)).expand(3D);
            this.aoe(box, 10f, 1f, false, phase2 ? new StatusEffect[]{StatusEffects.BLINDNESS} : new StatusEffect[0]);
            this.boss.playSound(SoundRegistry.SCYTHE_SWIPE, 1f, 0.75f);
            if (phase2) {
                if (this.boss.teleportAway()) {
                    this.boss.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                }
            }
        }
        if (phase2 && this.attackStatus == 43) {
            Vec3d vec = new Vec3d(target.getX() - (this.boss.getX()), target.getEyeY() - this.boss.getBodyY(1f), target.getZ() - this.boss.getZ());
            this.shootSplitSkulls(vec, 3, 1.75f);
        }
        this.checkAndReset(this.boss.isPhaseTwo() ? 1 : 10, 0);
    }

    public static class DeathSpiralEntity extends InvisibleEntity {
        private DeathSpiralLogic logic = new DeathSpiralLogic(this.getPos(), 1f);

        public DeathSpiralEntity(EntityType<? extends InvisibleEntity> entityType, World world) {
            super(entityType, world);
        }

        protected DeathSpiralEntity(World world, Vec3d pos, float radius) {
            super(EntityRegistry.DEATH_SPIRAL_ENTITY, world);
            this.logic = new DeathSpiralLogic(pos, radius);
        }

        @Override
        public boolean damage(DamageSource source, float amount) {
            return false;
        }

        @Override
        public void tick() {
            super.tick();
            this.setPosition(this.getPos());
            if (this.getWorld().isClient) {
                for (int i = 0; i < 4; i++) {
                    logic.tick(this.getWorld(), this.getPos());
                }
            }
            if (this.logic.isFinished() || this.age > 200) {
                this.discard();
            }
        }
    }
}
