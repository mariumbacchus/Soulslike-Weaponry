package net.soulsweaponry.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.FrostData;
import net.soulsweaponry.items.abilities.posthit.Permafrost;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LeviathanAxeEntity extends ReturningProjectile implements GeoEntity {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public LeviathanAxeEntity(EntityType<? extends LeviathanAxeEntity> entityType, World world) {
        super(entityType, world);
        this.setItemStack(new ItemStack(WeaponRegistry.LEVIATHAN_AXE));
    }

    public LeviathanAxeEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.LEVIATHAN_AXE_ENTITY_TYPE, owner, world, stack);
    }

    @Override
    public float getDamage(Entity target) {
        return ConfigConstructor.leviathan_axe_projectile_damage + WeaponUtil.getEnchantDamageBonus(this.asItemStack());
    }

    @Override
    public boolean collide(Entity owner, Entity target, DamageSource damageSource, float damage) {
        if (!this.getWorld().isClient && target instanceof MjolnirProjectile) {
            ParticleEvents.mjolnirLeviathanAxeCollision(this.getWorld(), this.getX(), this.getY(), this.getZ());
            this.getWorld().createExplosion(null, this.getX(), this.getY(), this.getZ(), 6.0F, true, World.ExplosionSourceType.TNT);
        }
        boolean damaged = target.damage(damageSource, damage);
        if (damaged) {
            int enchant = EnchantmentHelper.getLevel(Enchantments.SHARPNESS, this.asItemStack());
            if (target instanceof LivingEntity living) {
                FrostData.addFrost(living, (int) ConfigConstructor.leviathan_axe_projectile_frost_buildup_on_collision);
                FrostData.setFrostSource(living, owner);
                living.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING, 200, enchant));
            }
            FrostData.setFrostSource(this, owner);
            Permafrost.iceExplosion(getWorld(), this.getBlockPos(), this, (enchant + 1) * 1.5f, enchant);
        }
        return damaged;
    }

    @Override
    public double getReturnSpeed(ItemStack stack) {
        return ConfigConstructor.leviathan_axe_return_speed + (double) EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack) /2f;
    }

    @Override
    protected boolean canHit(Entity entity) {
        if (entity instanceof MjolnirProjectile) {
            return true;
        } else {
            return super.canHit(entity);
        }
    }

    private PlayState predicate(AnimationState<?> state) {
        try {
            if (!this.inGround || this.isNoClip()) {
                state.getController().setAnimation(RawAnimation.begin().then("spin", Animation.LoopType.LOOP));
            } else {
                state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            }
        } catch (Exception e) {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
}