package net.soulsweaponry.entity.ai.goal;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.event.GameEvent;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.ChaosMonarch;
import net.soulsweaponry.entity.mobs.ChaosMonarch.Attack;
import net.soulsweaponry.entity.projectile.*;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.ParticleEvents;
import net.soulsweaponry.util.ParticleHandler;

import java.util.EnumSet;
import java.util.List;

public class ChaosMonarchGoal extends Goal {

    private final ChaosMonarch boss;
    private int attackCooldown;
    private int attackStatus;
    private boolean randomOrNot;
    private int controlledProjectile = 0;
    private BlockPos blockPos;
    
    public ChaosMonarchGoal(ChaosMonarch boss) {
        this.boss = boss;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    public float getModifiedDamage(float damage) {
        return damage * ConfigConstructor.chaos_monarch_damage_modifier;
    }

    @Override
    public boolean canStart() {
        LivingEntity target = this.boss.getTarget();
        return target != null && target.isAlive() && this.boss.canTarget(target);
    }

    @Override
    public void stop() {
        super.stop();
        this.boss.setAttack(0);
        this.boss.setAttacking(false);
    }

    public void resetAttack(float cooldownModifier) {
        this.attackStatus = 0;
        this.attackCooldown = this.adjustCooldown(cooldownModifier);
        this.boss.setAttack(0);
        this.randomOrNot = this.boss.getRandom().nextBoolean();
        this.controlledProjectile = this.boss.getRandom().nextInt(5);
        this.blockPos = null;
    }

    private int adjustCooldown(float cooldownModifier) {
        int reducedCooldown = MathHelper.floor(this.boss.getMaxHealth()/this.boss.getHealth())*4;
        return MathHelper.floor(ConfigConstructor.chaos_monarch_attack_cooldown_ticks * cooldownModifier - reducedCooldown);
    }

    public void randomAttack() {
        int attack = this.boss.getRandom().nextInt(6 - 2 + 1) + 2;
        if (attack == 3 && this.boss.getTarget() != null && this.boss.squaredDistanceTo(this.boss.getTarget()) > 40) {
            this.boss.setAttack(0);
        } else {
            this.boss.setAttack(attack);
        }
    }

    @Override
    public void tick() {
        this.attackCooldown--;
        LivingEntity target = this.boss.getTarget();
        if (target != null) {
            this.boss.setAttacking(true);
            this.boss.getLookControl().lookAt(target.getX(), target.getEyeY(), target.getZ());

            if (this.attackCooldown < 0) {
                if (this.boss.getAttack() == Attack.IDLE) this.randomAttack();
                switch (this.boss.getAttack()) {
                    case TELEPORT -> {
                        this.attackStatus++;
                        if (this.attackStatus % 2 == 0 && this.attackStatus < 10) {
                            if (!this.boss.getWorld().isClient){
                                ParticleHandler.particleSphere(this.boss.getWorld(), 1000, this.boss.getX(), this.boss.getY(), this.boss.getZ(), ParticleTypes.PORTAL, 4f);
                            }
                        }
                        if (this.attackStatus == 23) {
                            this.boss.getWorld().playSound(null, this.boss.getX(), this.boss.getY(), this.boss.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.0f);
                            boolean bl = this.teleportAway();
                            if (!bl) {
                                this.explode();
                            } else {
                                if (!this.boss.getWorld().isClient) {
                                    ParticleHandler.particleSphereList(this.boss.getWorld(), 1000, this.boss.getX(), this.boss.getY(), this.boss.getZ(), 1f, ParticleTypes.DRAGON_BREATH, ParticleTypes.DRAGON_BREATH);
                                }
                            }
                        }
                        if (this.attackStatus >= 30) {
                            this.resetAttack(.5f);
                        }
                    }
                    case MELEE -> {
                        this.attackStatus++;
                        if (this.attackStatus < 5) this.blockPos = target.getBlockPos();
                        if (this.blockPos != null) {
                            this.boss.getLookControl().lookAt(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ());
                            if (this.attackStatus == 8) this.hitBox(blockPos, HitboxType.SWIPE);
                            if (this.attackStatus == 16) this.hitBox(blockPos, HitboxType.SWIPE);
                            if (this.attackStatus == 27) this.hitBox(blockPos, HitboxType.THRUST);
                            if (this.attackStatus == 39) this.hitBox(blockPos, HitboxType.BONK);
                        }
                        if (this.attackStatus >= 45) this.resetAttack(1);
                    }
                    case LIGHTNING -> {
                        this.attackStatus++;
                        int[] triggers = {15, 20, 25};
                        if (this.attackStatus == 5)
                            this.boss.getWorld().playSound(null, this.boss.getX(), this.boss.getY(), this.boss.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.HOSTILE, 1.0f, 1.5f);
                        for (int i = 1; i < triggers.length + 1; i++) {
                            if (attackStatus == triggers[i - 1]) this.spawnLightning(i);
                        }
                        if (this.attackStatus >= 30) this.resetAttack(1);
                    }
                    case SHOOT -> {
                        this.attackStatus++;
                        if (this.attackStatus % 6 == 0) this.chaosSkull(target);
                        if (this.attackStatus >= 40) this.resetAttack(1);
                    }
                    case BARRAGE -> {
                        this.attackStatus++;
                        if (this.attackStatus >= 9 && this.attackStatus < 25) {
                            this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 2f, 1f);
                            if (this.randomOrNot) {
                                this.randomProjectiles();
                            } else {
                                this.controlledProjectiles(target);
                            }
                        }
                        if (this.attackStatus >= 30) this.resetAttack(1);
                    }
                    default -> {
                    }
                }
            }
        }
    }

    /**
     * Just realized how hard it is to create an actual rotatable bounding box without any
     * previous expirience. Sooooo here's what I like to call a "Hate Solution", not quite
     * what I wanted but good enough, maybe something I can fix later when I actaully learn
     * how to create proper hitboxes.
     */
    private void hitBox(BlockPos blockPos, HitboxType type) {
        switch (type) {
            case SWIPE -> {
                this.boss.getWorld().playSound(null, blockPos, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.HOSTILE, 1f, 1f);
                this.generateHitbox(blockPos, 3, 15f);
            }
            case THRUST -> {
                this.boss.getWorld().playSound(null, blockPos, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.HOSTILE, 1f, 1f);
                this.generateHitbox(blockPos, 2, 20);
            }
            case BONK -> {
                this.generateHitbox(blockPos, 2, 30f);
                this.boss.getWorld().playSound(null, blockPos, SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 1f, 1f);
                if (!this.boss.getWorld().isClient) {
                    ParticleHandler.particleSphereList(this.boss.getWorld(), 100, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
                }
            }
        }
    }

    private void generateHitbox(BlockPos blockPos, double expand, float damage) {
        Box box = new Box(blockPos).expand(expand);
        List<Entity> intersectingEntities = this.boss.getWorld().getOtherEntities(this.boss, box);
        for (Entity entity : intersectingEntities) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.damage(this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(damage));
                livingEntity.takeKnockback(this.boss.getRandom().nextDouble(), this.boss.getX() - livingEntity.getX(), this.boss.getZ() - livingEntity.getZ());
            }
        }
    }

    private void chaosSkull(LivingEntity target) {
        double e = target.getX() - (this.boss.getX());
        double f = target.getBodyY(0.5D) - this.boss.getBodyY(1.0D);
        double g = target.getZ() - this.boss.getZ();
        ChaosSkull skull = new ChaosSkull(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ(), e, f + 1, g, this.boss.getWorld());
        skull.setOwner(this.boss);
        this.boss.getWorld().spawnEntity(skull);
        this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.HOSTILE, 1f, 1f);
    }

    private void controlledProjectiles(LivingEntity target) {
        double e = target.getX() - (this.boss.getX());
        double f = target.getBodyY(0.5D) - this.boss.getBodyY(1.0D);
        double g = target.getZ() - this.boss.getZ();
        Vec3d vel = new Vec3d(e, f, g).multiply(0.15D);
        switch (controlledProjectile) {
            case 1 -> {
                ShulkerBulletEntity bullet = new ShulkerBulletEntity(this.boss.getWorld(), this.boss, target, this.boss.getMovementDirection().getAxis());
                bullet.setPosition(this.getBossPos());
                this.boss.getWorld().spawnEntity(bullet);
            }
            case 2 -> {
                ExperienceBottleEntity bottle = new ExperienceBottleEntity(EntityType.EXPERIENCE_BOTTLE, this.boss.getWorld());
                bottle.setPosition(this.getBossPos());
                bottle.setVelocity(vel);
                this.boss.getWorld().spawnEntity(bottle);
            }
            case 3 -> {
                if (this.attackStatus % 2 == 0 && this.attackStatus > 18) {
                    FireballEntity fireball = new FireballEntity(this.boss.getWorld(), this.boss, e, f, g, this.boss.getRandom().nextInt(3) + 1);
                    fireball.setPosition(this.getBossPos());
                    this.boss.getWorld().spawnEntity(fireball);
                } else if (this.attackStatus < 16) {
                    SmallFireballEntity fireball = new SmallFireballEntity(this.boss.getWorld(), this.boss.getX(), this.boss.getEyeY(), this.boss.getZ(), e, f, g);
                    this.boss.getWorld().spawnEntity(fireball);
                }
            }
            case 4 -> {
                SnowballEntity snow = new SnowballEntity(EntityType.SNOWBALL, this.boss.getWorld());
                snow.setPosition(this.getBossPos());
                snow.setVelocity(vel);
                this.boss.getWorld().spawnEntity(snow);
            }
            default -> {
                SpectralArrowEntity arrow = new SpectralArrowEntity(EntityType.SPECTRAL_ARROW, this.boss.getWorld());
                arrow.setPosition(this.getBossPos());
                arrow.setDamage(8f);
                arrow.setVelocity(vel.getX(), vel.getY() + .2f, vel.getZ());
                this.boss.getWorld().spawnEntity(arrow);
            }
        }
    }

    private Vec3d getBossPos() {
        return new Vec3d(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
    }

    private void randomProjectiles() {
        ProjectileEntity[] projectiles = {
            new ArrowEntity(this.boss.getWorld(), this.boss, Items.ARROW.getDefaultStack()),
            new DragonFireballEntity(EntityType.DRAGON_FIREBALL, this.boss.getWorld()),
            new FireballEntity(EntityType.FIREBALL, this.boss.getWorld()),
            new LlamaSpitEntity(EntityType.LLAMA_SPIT, this.boss.getWorld()),
            new SmallFireballEntity(EntityType.SMALL_FIREBALL, this.boss.getWorld()),
            new SpectralArrowEntity(this.boss.getWorld(), this.boss, Items.SPECTRAL_ARROW.getDefaultStack()),
            new WitherSkullEntity(EntityType.WITHER_SKULL, this.boss.getWorld()),
            new EggEntity(EntityType.EGG, this.boss.getWorld()),
            new ExperienceBottleEntity(EntityType.EXPERIENCE_BOTTLE, this.boss.getWorld()),
            new SnowballEntity(EntityType.SNOWBALL, this.boss.getWorld()),
            new TridentEntity(EntityType.TRIDENT, this.boss.getWorld()),
            //Mod projectiles
            new Cannonball(this.boss.getWorld(), this.boss),
            new ChargedArrow(EntityRegistry.CHARGED_ARROW_ENTITY_TYPE, this.boss.getWorld()),
            new CometSpearEntity(EntityRegistry.COMET_SPEAR_ENTITY_TYPE, this.boss.getWorld()),
            new DragonslayerSwordspearEntity(EntityRegistry.SWORDSPEAR_ENTITY_TYPE, this.boss.getWorld()),
            new MoonlightProjectile(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE, this.boss.getWorld()),
            new SilverBulletEntity(EntityRegistry.SILVER_BULLET_ENTITY_TYPE, this.boss.getWorld())
        };
        if (this.boss.getTarget() != null) {
            LivingEntity target = this.boss.getTarget();
            double e = target.getX() - (this.boss.getX());
            double f = target.getBodyY(0.5D) - this.boss.getBodyY(1.0D);
            double g = target.getZ() - this.boss.getZ();
            int random = this.boss.getRandom().nextInt(projectiles.length);
            ProjectileEntity entity = projectiles[random];
            if (entity instanceof FireballEntity) {
                entity = new FireballEntity(this.boss.getWorld(), this.boss, e, f, g, this.boss.getRandom().nextInt(3) + 1);
            }
            entity.setPos(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
            if (entity instanceof PersistentProjectileEntity) {
                ((PersistentProjectileEntity)entity).setDamage(6f);
            }
            entity.setVelocity(new Vec3d(e, f + 1, g).multiply(0.15D));
            this.boss.getWorld().spawnEntity(entity);
        }
    }

    private void spawnLightning(int multiplier) {
        int r = 5*multiplier;
        for (int theta = 0; theta < 360; theta+=30) {
            double x0 = this.boss.getX();
            double z0 = this.boss.getZ();
            double x = x0 + r * Math.cos(theta * Math.PI / 180);
            double z = z0 + r * Math.sin(theta * Math.PI / 180);
            BlockPos pos = BlockPos.ofFloored(x, this.boss.getY(), z);
            LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, this.boss.getWorld());
            entity.setPos(pos.getX(), pos.getY(), pos.getZ());
            this.boss.getWorld().spawnEntity(entity);
        }
    }

    protected boolean teleportAway() {
        if (this.boss.getWorld().isClient() || !this.boss.isAlive()) {
            return false;
        }
        double d = this.boss.getX() + (this.boss.getRandom().nextDouble() - 0.5) * 64.0;
        double e = this.boss.getY() + (double)(this.boss.getRandom().nextInt(64) - 16);
        double f = this.boss.getZ() + (this.boss.getRandom().nextDouble() - 0.5) * 64.0;
        return this.teleportTo(d, e, f);
    }

    private boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
        while (mutable.getY() > this.boss.getWorld().getBottomY() && !this.boss.getWorld().getBlockState(mutable).blocksMovement()) {
            mutable.move(Direction.DOWN);
        }
        BlockState blockState = this.boss.getWorld().getBlockState(mutable);
        boolean bl = blockState.blocksMovement();
        boolean bl2 = blockState.getFluidState().isIn(FluidTags.WATER);
        if (!bl || bl2) {
            return false;
        }
        Vec3d vec3d = this.boss.getPos();
        boolean bl3 = this.boss.teleport(x, y, z, true);
        if (bl3) {
            this.boss.getWorld().emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(this.boss));
            if (!this.boss.isSilent()) {
                this.boss.getWorld().playSound(null, this.boss.prevX, this.boss.prevY, this.boss.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.0f);
                this.boss.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            }
        }
        return bl3;
    }

    private void explode() {
        this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 5f, 1f);
        Box chunkBox = new Box(this.boss.getBlockPos()).expand(5);
        List<Entity> nearbyEntities = this.boss.getWorld().getOtherEntities(this.boss, chunkBox);
        for (Entity nearbyEntity : nearbyEntities) {
            if (nearbyEntity instanceof LivingEntity closestTarget) {
                double x = closestTarget.getX() - (this.boss.getX());
                double z = closestTarget.getZ() - this.boss.getZ();
                closestTarget.takeKnockback(10F, -x, -z);
                closestTarget.damage(this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(30f));
            }
        }

        if (!this.boss.getWorld().isClient) {
            ParticleHandler.particleSphereList(this.boss.getWorld(), 1000, this.boss.getX(), this.boss.getY(), this.boss.getZ(), 1f, ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.LARGE_SMOKE);
        }
    }

    enum HitboxType {
        SWIPE,
        THRUST,
        BONK
    }
}
