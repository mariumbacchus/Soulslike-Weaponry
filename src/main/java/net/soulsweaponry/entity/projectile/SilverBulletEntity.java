package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.ChainLightning;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class SilverBulletEntity extends NonArrowProjectile implements GeoEntity, IPostureLossProjectile {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private int postureLoss;
    private boolean isEthereal;
    private float explosionPower;
    private float chainLightningDamage;
    private float chainLightningRange;

    public SilverBulletEntity(EntityType<? extends SilverBulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public SilverBulletEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.SILVER_BULLET_ENTITY_TYPE.get(), owner, world, stack);
    }

    public SilverBulletEntity(EntityType<? extends SilverBulletEntity> entityType, World world, LivingEntity owner, ItemStack stack) {
        super(entityType, owner, world, stack);
    }

    @Override
    public void tick() {
        if (this.isNoClip()) {
            this.setPitch(0f);
            this.setYaw(0f);
            Vec3d currentPos = this.getPos();
            Vec3d velocity = this.getVelocity();
            Vec3d nextPos = currentPos.add(velocity);
            EntityHitResult entityHit = ProjectileUtil.raycast(
                    this,
                    currentPos,
                    nextPos,
                    this.getBoundingBox().stretch(velocity).expand(1.0D),
                    e -> e instanceof LivingEntity && e != this.getOwner(), 5D
            );
            if (entityHit != null) {
                this.onEntityHit(entityHit);
                return;
            }
        }
        super.tick();
        if (this.isNoClip()) {
            this.setPitch(0f);
            this.setYaw(0f);
        }
        if (!this.inGround) {
            Vec3d vec3d = this.getVelocity();
            double e = vec3d.x;
            double f = vec3d.y;
            double g = vec3d.z;
            for (int i = 0; i < 2; ++i) {
                this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D + 0.25f, this.getZ() + g * (double)i / 4.0D, -e*0.2, (-f + 0.2D)*0.2, -g*0.2);
            }
        }
        if (this.age > this.getMaxAge()) {
            this.discard();
        }
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    public void setEthereal(boolean ethereal) {
        this.isEthereal = ethereal;
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_STONE_BREAK;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.discard();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity target) {
            this.applyPostureLoss(target);
            if (target.isUndead()) {
                this.setDamage(this.getDamage() + (ConfigConstructor.silver_bullet_undead_bonus_damage / this.getVelocity().length()));
            }
            if (this.getOwner() instanceof LivingEntity owner) {
                if (this.chainLightningDamage > 0f) {
                    ChainLightning.trigger(this.getWorld(), target, owner, false, this.getChainLightningDamage(), this.getChainLightningRange());
                }
            }
        }
        super.onEntityHit(entityHitResult);
        if (this.explosionPower > 0f && !this.getWorld().isClient) {
            ParticleHandler.particleOutburst(this.getWorld(), 30, this.getX(), this.getY(), this.getZ(), ParticleTypes.SOUL, new Vec3d(1, 1 ,1), 0.6f);
            this.getWorld().createExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), this.explosionPower, World.ExplosionSourceType.MOB);
        }
        this.discard();
    }

    public int getMaxAge() {
        if (this.isEthereal) {
            return 25;
        }
        return 100;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemRegistry.SILVER_BULLET.get().getDefaultStack();
    }

    public void setPostureLoss(int postureLoss) {
        this.postureLoss = postureLoss;
    }

    public int getPostureLoss() {
        return postureLoss;
    }

    public void setExplosionPower(float explosionPower) {
        this.explosionPower = explosionPower;
    }

    public void setChainLightningDamage(float chainLightningDamage) {
        this.chainLightningDamage = chainLightningDamage;
    }

    public float getChainLightningDamage() {
        return this.chainLightningDamage;
    }

    public void setChainLightningRange(float chainLightningRange) {
        this.chainLightningRange = chainLightningRange;
    }

    public float getChainLightningRange() {
        return this.chainLightningRange;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("postureLoss")) {
            this.setPostureLoss(nbt.getInt("postureLoss"));
        }
        if (nbt.contains("ethereal")) {
            this.isEthereal = nbt.getBoolean("ethereal");
        }
        if (nbt.contains("explosionPower")) {
            this.explosionPower = nbt.getFloat("explosionPower");
        }
        if (nbt.contains("chainLightningDamage")) {
            this.chainLightningDamage = nbt.getFloat("chainLightningDamage");
        }
        if (nbt.contains("chainLightningRange")) {
            this.chainLightningRange = nbt.getFloat("chainLightningRange");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("postureLoss", this.getPostureLoss());
        nbt.putBoolean("ethereal", this.isEthereal);
        nbt.putFloat("explosionPower", this.explosionPower);
        nbt.putFloat("chainLightningDamage", this.chainLightningDamage);
        nbt.putFloat("chainLightningRange", this.chainLightningRange);
    }
}