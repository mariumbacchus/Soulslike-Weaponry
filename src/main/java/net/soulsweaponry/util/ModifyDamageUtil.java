package net.soulsweaponry.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.arrow.TrueDamageArrow;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.bonusdamage.BonusCritHitDamage;
import net.soulsweaponry.items.abilities.posthit.Permafrost;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.Optional;

public class ModifyDamageUtil {

    /**
     * Modifies the damage taken after other calculations to the given entity, mainly called in {@code LivingEntityMixin} class
     * in {@code modifyAppliedDamage} method.
     * @param entity Entity to modify damage on
     * @param newAmount Amount to be modified
     * @param source Damage source
     * @return new damage amount to be taken
     */
    public static float modifyDamageTakenTail(LivingEntity entity, float newAmount, DamageSource source) {
        if (entity.hasStatusEffect(EffectRegistry.DECAY)) {
            boolean immune = true;
            for (ItemStack stack : entity.getArmorItems()) {
                immune = isDecayImmune(stack);
            }
            if (!immune) {
                for (ItemStack stack : entity.getHandItems()) {
                    immune = isDecayImmune(stack);
                }
            }
            if (!immune) {
                int amplifier = entity.getStatusEffect(EffectRegistry.DECAY).getAmplifier();
                float amountAdded = newAmount * ((amplifier + 1) * 0.2f);
                newAmount += amountAdded;
            }
        }
        if ((source.isOf(DamageTypes.MAGIC) || source.isOf(DamageTypes.INDIRECT_MAGIC)) && entity.hasStatusEffect(EffectRegistry.MAGIC_RESISTANCE)) {
            int amplifier = entity.getStatusEffect(EffectRegistry.MAGIC_RESISTANCE).getAmplifier();
            float amountReduced = newAmount * ((amplifier + 1)*.2f);
            newAmount -= amountReduced;
        }
        if (entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK) && !source.isIn(DamageTypeTags.IS_PROJECTILE)) {
            int amplifier = entity.getStatusEffect(EffectRegistry.POSTURE_BREAK).getAmplifier();
            float baseAdded = entity instanceof PlayerEntity ? ConfigConstructor.posture_break_player_damage_per_amp : ConfigConstructor.posture_break_damage_per_amp;
            float totalAdded = baseAdded * (amplifier + 1) + ConfigConstructor.posture_break_percent_health_damage * entity.getMaxHealth();
            if (source.getAttacker() instanceof LivingEntity living) {
                Optional<BonusCritHitDamage> op = IHasAbilities.getAbility(living.getStackInHand(Hand.MAIN_HAND), BonusCritHitDamage.class);
                if (op.isPresent()) {
                    totalAdded += (float) (totalAdded * op.get().percentBonus());
                }
            }
            newAmount += totalAdded;
            entity.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.CRIT_HIT_EVENT, SoundCategory.HOSTILE, .5f, 1f);
            entity.removeStatusEffect(EffectRegistry.POSTURE_BREAK);
            if (entity.hasStatusEffect(StatusEffects.SLOWNESS)) entity.removeStatusEffect(StatusEffects.SLOWNESS);
            if (entity.hasStatusEffect(StatusEffects.WEAKNESS)) entity.removeStatusEffect(StatusEffects.WEAKNESS);
            if (entity.hasStatusEffect(StatusEffects.MINING_FATIGUE)) entity.removeStatusEffect(StatusEffects.MINING_FATIGUE);
            if (entity.hasStatusEffect(EffectRegistry.FREEZING)) {
                int amp = entity.getStatusEffect(EffectRegistry.FREEZING).getAmplifier();
                Permafrost.iceExplosion(entity.getWorld(), entity.getBlockPos(), entity, (amp + 1) * 1.5f, amp);
                entity.removeStatusEffect(EffectRegistry.FREEZING);
            }
        }
        if (entity.hasStatusEffect(EffectRegistry.BLIGHT) && entity.getArmor() > 0) {
            int amplifier = entity.getStatusEffect(EffectRegistry.BLIGHT).getAmplifier() + 1; // ln(0) does not end well!
            int armorValue = entity.getArmor();
            // Original value increases in % based on f(x) = (ln(x) * y) / 6, where x is the amplifier level, y is the armor of the target.
            float increase = (float) (newAmount * (((Math.log(amplifier) * armorValue) / 6f) / 10f));
            newAmount += increase;
        }
        if (source.getSource() instanceof TrueDamageArrow projectile) {
            float trueDamage = projectile.getTrueDamage();
            newAmount += entity instanceof PlayerEntity ? trueDamage * ConfigConstructor.kraken_slayer_player_true_damage_taken_modifier : trueDamage;
        }
        for (Hand hand : Hand.values()) {
            ItemStack stack = entity.getStackInHand(hand);
            if (stack.getItem() instanceof IHasAbilities hasAbilities) {
                for (IAbility ability : hasAbilities.getAbilities()) {
                    newAmount = ability.modifyUserDamageTaken(entity, newAmount, source, stack, hand);
                }
            }
            if (source.getAttacker() instanceof LivingEntity attacker) {
                ItemStack stackAttacker = attacker.getStackInHand(hand);
                if (stackAttacker.getItem() instanceof IHasAbilities hasAbilities) {
                    for (IAbility ability : hasAbilities.getAbilities()) {
                        newAmount = ability.modifyTargetDamageTaken(entity, newAmount, source, stack, attacker, hand);
                    }
                }
            }
        }
        return newAmount;
    }

    private static boolean isDecayImmune(ItemStack stack) {
        boolean immune = false;
        if (stack.getItem() instanceof IHasAbilities hasAbilities) {
            immune = hasAbilities.getAbilities().stream().anyMatch(a -> a.getStatusEffectsImmuneTo().contains(EffectRegistry.DECAY));
        }
        return immune;
    }
}
