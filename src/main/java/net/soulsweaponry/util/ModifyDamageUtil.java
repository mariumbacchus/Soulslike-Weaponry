package net.soulsweaponry.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundCategory;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.KrakenSlayerProjectile;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;

public class ModifyDamageUtil {

    /**
     * Modifies the damage taken after other calculations to the given entity, mainly called in {@code LivingEntityMixin} class
     * in {@code modifyAppliedDamage} method.
     * @param entity Entity to modify damage on
     * @param newAmount Amount to be modified
     * @param source Damage source
     * @return new damage amount to be taken
     */
    public static float modifyDamageTaken(LivingEntity entity, float newAmount, DamageSource source) {
        if (entity.hasStatusEffect(EffectRegistry.DECAY.get()) && !entity.getEquippedStack(EquipmentSlot.HEAD).isOf(ItemRegistry.CHAOS_CROWN.get()) && !entity.getEquippedStack(EquipmentSlot.HEAD).isOf(ItemRegistry.CHAOS_HELMET.get())) {
            int amplifier = entity.getStatusEffect(EffectRegistry.DECAY.get()).getAmplifier();
            float amountAdded = newAmount * ((amplifier + 1)*.2f);
            newAmount += amountAdded;
        }
        if ((source.isOf(DamageTypes.MAGIC) || source.isOf(DamageTypes.INDIRECT_MAGIC)) && entity.hasStatusEffect(EffectRegistry.MAGIC_RESISTANCE.get())) {
            int amplifier = entity.getStatusEffect(EffectRegistry.MAGIC_RESISTANCE.get()).getAmplifier();
            float amountReduced = newAmount * ((amplifier + 1)*.2f);
            newAmount -= amountReduced;
        }
        if (entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK.get()) && !source.isIn(DamageTypeTags.IS_PROJECTILE)) {
            int amplifier = entity.getStatusEffect(EffectRegistry.POSTURE_BREAK.get()).getAmplifier();
            float baseAdded = entity instanceof PlayerEntity ? 3f : 8f;
            float totalAdded = baseAdded * (amplifier + 1);
            newAmount += totalAdded;
            entity.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.CRIT_HIT_EVENT.get(), SoundCategory.HOSTILE, .5f, 1f);
            entity.removeStatusEffect(EffectRegistry.POSTURE_BREAK.get());
            if (entity.hasStatusEffect(StatusEffects.SLOWNESS) && entity.getStatusEffect(StatusEffects.SLOWNESS).getDuration() < 100) entity.removeStatusEffect(StatusEffects.SLOWNESS);
            if (entity.hasStatusEffect(StatusEffects.WEAKNESS) && entity.getStatusEffect(StatusEffects.WEAKNESS).getDuration() < 100) entity.removeStatusEffect(StatusEffects.WEAKNESS);
            if (entity.hasStatusEffect(StatusEffects.MINING_FATIGUE) && entity.getStatusEffect(StatusEffects.MINING_FATIGUE).getDuration() < 100) entity.removeStatusEffect(StatusEffects.MINING_FATIGUE);
        }
        if (entity.hasStatusEffect(EffectRegistry.BLIGHT.get()) && entity.getArmor() > 0) {
            int amplifier = entity.getStatusEffect(EffectRegistry.BLIGHT.get()).getAmplifier() + 1; // ln(0) does not end well!
            int armorValue = entity.getArmor();
            // Original value increases in % based on f(x) = (ln(x) * y) / 8, where x is the amplifier level, y is the armor of the target.
            double increase = newAmount * (((Math.log(amplifier) * armorValue) / 6f) / 10f);
            newAmount += increase;
        }
        if (source.getSource() instanceof KrakenSlayerProjectile projectile) {
            float trueDamage = projectile.getTrueDamage();
            newAmount += entity instanceof PlayerEntity ? trueDamage * ConfigConstructor.kraken_slayer_player_true_damage_taken_modifier : trueDamage;
        }
        return newAmount;
    }
}