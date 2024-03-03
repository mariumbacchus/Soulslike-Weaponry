package net.soulsweaponry.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class DraupnirSpearEntity extends PersistentProjectileEntity implements IAnimatable {

    private static final TrackedData<Boolean> ENCHANTED;
    private ItemStack stack;
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private boolean dealtDamage;

    public DraupnirSpearEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.stack = new ItemStack(WeaponRegistry.DRAUPNIR_SPEAR);
    }

    public DraupnirSpearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.DRAUPNIR_SPEAR_TYPE, owner, world);
        this.stack = stack.copy();
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
    }

    public void detonate() {
        if (this.getOwner() != null && this.getBlockPos() != null && !world.isClient) {
            float power = ConfigConstructor.draupnir_spear_detonate_power + ((float) EnchantmentHelper.getLevel(Enchantments.SHARPNESS, asItemStack()) / 2.5f);
            this.world.createExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), power, false, Explosion.DestructionType.NONE);
            if (power > 2f) {
                for (Entity entity : world.getOtherEntities(this.getOwner(), this.getBoundingBox().expand(power))) {
                    if (entity instanceof LivingEntity living) {
                        living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, MathHelper.floor(power - 2)));
                    }
                }
            }
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = ConfigConstructor.draupnir_spear_projectile_damage;
        if (entity == null) {
            return;
        }
        if (entity instanceof LivingEntity livingEntity) {
            f += EnchantmentHelper.getAttackDamage(stack, livingEntity.getGroup());
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

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ENCHANTED, false);
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        if (this.age > ConfigConstructor.draupnir_spear_max_age) {
            this.remove(RemovalReason.DISCARDED);
        }
        super.tick();
    }

    @Override
    protected ItemStack asItemStack() {
        return this.stack;
    }

    public boolean isEnchanted() {
        return (Boolean)this.dataTracker.get(ENCHANTED);
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "attacks", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected float getDragInWater() {
        return 0.99F;
    }

    static {
        ENCHANTED = DataTracker.registerData(DraupnirSpearEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }
}