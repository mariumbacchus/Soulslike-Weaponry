package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

public abstract class DetonateGroundItem extends ChargeToUseItem {

    public DetonateGroundItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public void detonateGroundEffect(LivingEntity user, int power, float fallDistance, World world, ItemStack stack) {
        float expansion = this.getBaseExpansion() + this.getExpansionModifier() * fallDistance/10;
        power += fallDistance/5;
        Box box = user.getBoundingBox().expand(expansion);
        List<Entity> entities = world.getOtherEntities(user, box);
        for (Entity targets : entities) {
            if (targets instanceof LivingEntity livingEntity) {
                livingEntity.damage(CustomDamageSource.obliterateDamageSource(user), power + EnchantmentHelper.getAttackDamage(stack, livingEntity.getGroup()));
                livingEntity.addVelocity(0, fallDistance/this.getLaunchDivisor(), 0);
                if (this.shouldHeal()) user.heal(ConfigConstructor.lifesteal_item_base_healing - 1 + (ConfigConstructor.lifesteal_item_heal_scales ? power/10f : 0));
                for (StatusEffectInstance effect : this.applyEffects()) {
                    livingEntity.addStatusEffect(effect);
                }
                if (this instanceof Nightfall nightfall) {
                    nightfall.spawnRemnant(livingEntity, user);
                }
            }
        }
        world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
        float pDistance = fallDistance >= 25 ? fallDistance/25 : 1;
        if (!world.isClient) {
            ParticleHandler.particleOutburstMap(world, MathHelper.floor(200 * pDistance), user.getX(), user.getY(), user.getZ(), ParticleEvents.BASE_GRAND_SKYFALL_MAP, pDistance);
            for (ParticleEffect particle : this.getParticles().keySet()) {
                ParticleHandler.particleOutburst(world, MathHelper.floor(200 * pDistance), user.getX(), user.getY(), user.getZ(), particle, this.getParticles().get(particle), pDistance);
            }
        }
    }

    public abstract float getBaseExpansion();
    public abstract float getExpansionModifier();
    public abstract float getLaunchDivisor();
    public abstract boolean shouldHeal();
    public abstract StatusEffectInstance[] applyEffects();
    public abstract Map<ParticleEffect, Vec3d> getParticles();

    /**
     * Called in the fall damage mixin methods. {@link net.soulsweaponry.mixin.LivingEntityMixin#interceptFallDamage(float, float, DamageSource, CallbackInfoReturnable)}
     * and {@link net.soulsweaponry.mixin.PlayerEntityMixin#interceptFallDamage(float, float, DamageSource, CallbackInfoReturnable)}.
     * <p>Callback info/cancellation of the fall damage is handled in respective methods in the mixins.</p>
     * @param entity wielder of the weapon
     * @param fallDistance distance the user fell
     * @param source damage source (most likely fall damage)
     * @return whether the event was successful, i.e. had the effect, damage was from falling and the ground was detonated
     */
    public static boolean triggerCalculateFall(LivingEntity entity, float fallDistance, DamageSource source) {
        if (source.isFromFalling() && entity.hasStatusEffect(EffectRegistry.CALCULATED_FALL)) {
            StatusEffectInstance effect = entity.getStatusEffect(EffectRegistry.CALCULATED_FALL);
            DetonateGroundItem item = WeaponRegistry.COMET_SPEAR;
            ItemStack stack = new ItemStack(item);
            for (Hand hand : Hand.values()) {
                ItemStack itemStack = entity.getStackInHand(hand);
                if (stack.getItem() instanceof DetonateGroundItem) {
                    item = (DetonateGroundItem) stack.getItem();
                    stack = itemStack;
                }
            }
            item.detonateGroundEffect(entity, effect.getAmplifier(), fallDistance, entity.getWorld(), stack);
            entity.removeStatusEffect(EffectRegistry.CALCULATED_FALL);
            //Removes, then re-adds for half a second so that "dream_on" advancement may trigger
            entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL, 10, 0));
            return true;
        }
        return false;
    }
}