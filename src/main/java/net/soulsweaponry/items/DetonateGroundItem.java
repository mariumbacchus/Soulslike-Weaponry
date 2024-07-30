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

    public void detonateGroundEffect(LivingEntity user, int amplifier, float fallDistance, World world, ItemStack stack) {
        float expansion = this.getBaseExpansion() + this.getExpansionModifier() * Math.min(this.getMaxExpansion(), fallDistance / 10);
        float power = Math.min(this.getMaxDetonationDamage(), amplifier + fallDistance * this.getFallDamageIncreaseModifier()); // fallDistance was prev. divided by 5
        Box box = user.getBoundingBox().expand(expansion);
        List<Entity> entities = world.getOtherEntities(user, box);
        for (Entity targets : entities) {
            if (targets instanceof LivingEntity livingEntity) {
                boolean canDamageTarget = livingEntity.damage(CustomDamageSource.obliterateDamageSource(user), power + EnchantmentHelper.getAttackDamage(stack, livingEntity.getGroup()));
                if (canDamageTarget || ConfigConstructor.calculated_fall_hits_immune_entities) {
                    livingEntity.addVelocity(0, fallDistance * this.getLaunchModifier(), 0);
                    if (this.shouldHeal()) user.heal(ConfigConstructor.lifesteal_item_base_healing - 1 + (ConfigConstructor.lifesteal_item_heal_scales ? power * this.getHealFromDamageModifier() : 0)); // power was prev. divided by 10
                    this.doCustomEffects(livingEntity, user);
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

    /**
     * @return Modifier to multiply the fall distance (after division of 10) before adding to the total expansion
     * of the bounding box of the player, which determines the total range of the AOE
     */
    public abstract float getExpansionModifier();
    public abstract float getLaunchModifier();
    public abstract float getMaxExpansion();

    /**
     * Not to be confused with {@linkplain ItemStack#getMaxDamage()}
     * @return max possible damage calculated fall AOE can do to each entity
     */
    public abstract float getMaxDetonationDamage();

    /**
     * @return Modifier to multiply the fall distance before adding to total damage
     */
    public abstract float getFallDamageIncreaseModifier();

    /**
     * @return Whether the Calculated Fall detonation should heal the player based on the damage dealt to each target or not
     */
    public abstract boolean shouldHeal();

    /**
     * @return Healing modifier to multiply the total damage dealt to the target.
     * <p>NOTE: This is called on every target hit, so multiple targets = big heal</p>
     */
    public abstract float getHealFromDamageModifier();

    /**
     * Post hit effects for all the targets hit, like applying custom status effects or spawning a
     * {@linkplain net.soulsweaponry.entity.mobs.Remnant} like in {@linkplain Nightfall}'s case.
     * Called for each individual living entity hit.
     * @param target target
     * @param user user
     */
    public abstract void doCustomEffects(LivingEntity target, LivingEntity user);

    /**
     * Additional particles on top of {@linkplain ParticleEvents#BASE_GRAND_SKYFALL_MAP}
     * @return additional particles with vector divider to determine the direction of the particles
     */
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
        if (source.isFromFalling() && entity.hasStatusEffect(EffectRegistry.CALCULATED_FALL.get())) {
            StatusEffectInstance effect = entity.getStatusEffect(EffectRegistry.CALCULATED_FALL.get());
            DetonateGroundItem item = WeaponRegistry.COMET_SPEAR.get();
            ItemStack stack = new ItemStack(item);
            for (Hand hand : Hand.values()) {
                ItemStack itemStack = entity.getStackInHand(hand);
                if (stack.getItem() instanceof DetonateGroundItem) {
                    item = (DetonateGroundItem) stack.getItem();
                    stack = itemStack;
                }
            }
            item.detonateGroundEffect(entity, effect.getAmplifier(), fallDistance, entity.getWorld(), stack);
            entity.removeStatusEffect(EffectRegistry.CALCULATED_FALL.get());
            //Removes, then re-adds for half a second so that "dream_on" advancement may trigger
            entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL.get(), 10, 0));
            return true;
        }
        return false;
    }
}