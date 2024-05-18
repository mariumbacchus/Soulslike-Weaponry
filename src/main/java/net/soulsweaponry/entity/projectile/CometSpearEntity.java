package net.soulsweaponry.entity.projectile;

import net.soulsweaponry.config.CommonConfig;
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
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class CometSpearEntity extends PersistentProjectileEntity implements IAnimatable, IAnimationTickable {

    private static final TrackedData<Boolean> ENCHANTED;
    private ItemStack spearStack;
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private boolean dealtDamage;

    public CometSpearEntity(EntityType<? extends CometSpearEntity> entityType, World world) {
        super(entityType, world);
        this.spearStack = new ItemStack(WeaponRegistry.COMET_SPEAR.get());
    }

    public CometSpearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.COMET_SPEAR_ENTITY_TYPE.get(), owner, world);
        this.spearStack = new ItemStack(WeaponRegistry.COMET_SPEAR.get());
        this.spearStack = stack.copy();
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ENCHANTED, false);
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if (this.age > 100) {
            this.remove(RemovalReason.DISCARDED);
        }
        super.tick();
    }

    @Override
    public int tickTimer() {
        return age;
    }

    @Override
    public void registerControllers(AnimationData data) {        
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
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
        float f = CommonConfig.COMET_SPEAR_PROJECTILE_DAMAGE.get();
        if (entity == null) {
            return;
        }
        if (entity instanceof LivingEntity livingEntity) {
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
            if (entity instanceof LivingEntity livingEntity2) {
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
        ENCHANTED = DataTracker.registerData(CometSpearEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }
}
