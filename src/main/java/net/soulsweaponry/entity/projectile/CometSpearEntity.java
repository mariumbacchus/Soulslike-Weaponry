package net.soulsweaponry.entity.projectile;

import org.jetbrains.annotations.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class CometSpearEntity extends PersistentProjectileEntity implements GeoEntity {

    private static TrackedData<Boolean> ENCHANTED;
    private ItemStack spearStack;
    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private boolean dealtDamage;
    public int returnTimer;
    private int lifeTicks = 100;

    public CometSpearEntity(EntityType<? extends CometSpearEntity> entityType, World world) {
        super(entityType, world);
        this.spearStack = new ItemStack(WeaponRegistry.COMET_SPEAR);
    }

    public CometSpearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.COMET_SPEAR_ENTITY_TYPE, owner, world);
        this.spearStack = new ItemStack(WeaponRegistry.COMET_SPEAR);
        this.spearStack = stack.copy();
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ENCHANTED, false);
    }

    public void tick() {
        this.lifeTicks--;
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if (this.lifeTicks < 0) {
            this.remove(RemovalReason.DISCARDED);
        }
        super.tick();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    protected ItemStack asItemStack() {
        return this.spearStack.copy();
    }

    public boolean isEnchanted() {
        return (Boolean)this.dataTracker.get(ENCHANTED);
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = ConfigConstructor.comet_spear_projectile_damage;
        if (entity == null) {
            return;
        }
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            f += EnchantmentHelper.getAttackDamage(spearStack, livingEntity.getGroup()); /* EnchantmentHelper.getLevel(Enchantments.SHARPNESS, this.spearStack); */
            float healthPercentLeft = livingEntity.getHealth()/livingEntity.getMaxHealth();
            if (healthPercentLeft < 0.2) {
                f *= 2;
            }
        }

        Entity entity2 = this.getOwner();
        DamageSource damageSource = DamageSource.thrownProjectile(this, entity2);
        this.dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_HIT;
        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity2 = (LivingEntity)entity;
                if (entity2 instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity2, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity2);
                }
                this.onHit(livingEntity2);
            }
        }

        this.setVelocity(this.getVelocity().multiply(-0.01D, -0.1D, -0.01D));
        float g = 1.0F;
        this.playSound(soundEvent, g, 1.0F);
    }

    protected boolean tryPickup(PlayerEntity player) {
        return super.tryPickup(player) || this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null) {
            super.onPlayerCollision(player);
        }
    }

    protected float getDragInWater() {
        return 0.99F;
    }

    static {
        ENCHANTED = DataTracker.registerData(DragonslayerSwordspearEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }
}
