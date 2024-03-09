package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.IEntityDataSaver;
import net.soulsweaponry.util.PostureData;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class SilverBulletEntity extends NonArrowProjectile implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private int postureLoss;

    public SilverBulletEntity(EntityType<? extends SilverBulletEntity> entityType, World world, ItemStack stack) {
        super(entityType, world, stack);
    }

    public SilverBulletEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.SILVER_BULLET_ENTITY_TYPE, owner, world, stack);
    }

    public SilverBulletEntity(EntityType<? extends SilverBulletEntity> entityType, World world, LivingEntity owner, ItemStack stack) {
        super(entityType, owner, world, stack);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.inGround) {
            Vec3d vec3d = this.getVelocity();
            double e = vec3d.x;
            double f = vec3d.y;
            double g = vec3d.z;
            for (int i = 0; i < 2; ++i) {
                this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D, this.getZ() + g * (double)i / 4.0D, -e*0.2, (-f + 0.2D)*0.2, -g*0.2);
            }
        }
        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.x, vec3d.y + (double)0.045f, vec3d.z);
        if (this.age > this.getMaxAge()) {
            this.discard();
        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_STONE_BREAK;
    }

    public boolean isFireImmune() {
        return true;
    }

    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.discard(); 
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (ConfigConstructor.can_projectiles_apply_posture_loss && entityHitResult.getEntity() instanceof LivingEntity target) {
            PostureData.addPosture((IEntityDataSaver) target, this.getPostureLoss());
            if (target.isUndead()) {
                this.setDamage(this.getDamage() + ConfigConstructor.silver_bullet_undead_bonus_damage);
            }
        }
        super.onEntityHit(entityHitResult);
        this.discard();
    }

    public int getMaxAge() {
        return 200;
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
        return ItemRegistry.SILVER_BULLET.getDefaultStack();
    }

    public void setPostureLoss(int postureLoss) {
        this.postureLoss = postureLoss;
    }

    public int getPostureLoss() {
        return postureLoss;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("postureLoss")) {
            this.setPostureLoss(nbt.getInt("postureLoss"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("postureLoss", this.getPostureLoss());
    }
}
