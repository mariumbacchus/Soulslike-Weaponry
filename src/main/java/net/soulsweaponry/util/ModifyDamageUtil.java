package net.soulsweaponry.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.KrakenSlayerProjectile;
import net.soulsweaponry.items.IDragonBonus;
import net.soulsweaponry.items.ILifeGuard;
import net.soulsweaponry.particles.ParticleHandler;
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
    public static float modifyDamageTakenTail(LivingEntity entity, float newAmount, DamageSource source) {
        if (entity.getType().isIn(ModTags.Entities.DRAGONS) && source.getAttacker() instanceof PlayerEntity player && player.getMainHandStack().getItem() instanceof IDragonBonus dragonBonus) {
            newAmount += dragonBonus.getDragonBonus(player.getMainHandStack());
        }
        if (entity.hasStatusEffect(EffectRegistry.DECAY) && !entity.getEquippedStack(EquipmentSlot.HEAD).isOf(ItemRegistry.CHAOS_CROWN) && !entity.getEquippedStack(EquipmentSlot.HEAD).isOf(ItemRegistry.CHAOS_HELMET)) {
            int amplifier = entity.getStatusEffect(EffectRegistry.DECAY).getAmplifier();
            float amountAdded = newAmount * ((amplifier + 1)*.2f);
            newAmount += amountAdded;
        }
        if ((source.isOf(DamageTypes.MAGIC) || source.isOf(DamageTypes.INDIRECT_MAGIC)) && entity.hasStatusEffect(EffectRegistry.MAGIC_RESISTANCE)) {
            int amplifier = entity.getStatusEffect(EffectRegistry.MAGIC_RESISTANCE).getAmplifier();
            float amountReduced = newAmount * ((amplifier + 1)*.2f);
            newAmount -= amountReduced;
        }
        if (entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK) && !source.isIn(DamageTypeTags.IS_PROJECTILE)) {
            int amplifier = entity.getStatusEffect(EffectRegistry.POSTURE_BREAK).getAmplifier();
            float baseAdded = entity instanceof PlayerEntity ? 3f : 8f;
            float totalAdded = baseAdded * (amplifier + 1);
            newAmount += totalAdded;
            entity.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.CRIT_HIT_EVENT, SoundCategory.HOSTILE, .5f, 1f);
            entity.removeStatusEffect(EffectRegistry.POSTURE_BREAK);
            if (entity.hasStatusEffect(StatusEffects.SLOWNESS) && entity.getStatusEffect(StatusEffects.SLOWNESS).getDuration() < 100) entity.removeStatusEffect(StatusEffects.SLOWNESS);
            if (entity.hasStatusEffect(StatusEffects.WEAKNESS) && entity.getStatusEffect(StatusEffects.WEAKNESS).getDuration() < 100) entity.removeStatusEffect(StatusEffects.WEAKNESS);
            if (entity.hasStatusEffect(StatusEffects.MINING_FATIGUE) && entity.getStatusEffect(StatusEffects.MINING_FATIGUE).getDuration() < 100) entity.removeStatusEffect(StatusEffects.MINING_FATIGUE);
        }
        if (entity.hasStatusEffect(EffectRegistry.BLIGHT) && entity.getArmor() > 0) {
            int amplifier = entity.getStatusEffect(EffectRegistry.BLIGHT).getAmplifier() + 1; // ln(0) does not end well!
            int armorValue = entity.getArmor();
            // Original value increases in % based on f(x) = (ln(x) * y) / 8, where x is the amplifier level, y is the armor of the target.
            double increase = newAmount * (((Math.log(amplifier) * armorValue) / 6f) / 10f);
            newAmount += increase;
        }
        if (source.getSource() instanceof KrakenSlayerProjectile projectile) {
            float trueDamage = projectile.getTrueDamage();
            newAmount += entity instanceof PlayerEntity ? trueDamage * ConfigConstructor.kraken_slayer_player_true_damage_taken_modifier : trueDamage;
        }
        // Inflict percent of damage to held ILifeGuard item instead of damage to the user (not stackable)
        for (Hand hand : Hand.values()) {
            ItemStack stack = entity.getStackInHand(hand);
            if (stack.getItem() instanceof ILifeGuard guard) {
                float damage = Math.max(0, (float) (newAmount * (1D - guard.getLifeGuardPercent(stack))));
                int rounded = Math.round(newAmount);
                if (rounded > 0) {
                    stack.damage(rounded, entity, (p) -> p.sendToolBreakStatus(hand));
                }
                newAmount = damage;
                for (int i = 0; i < 4; i++) {
                    ParticleHandler.singleParticle(entity.getWorld(), ParticleTypes.SOUL, entity.getParticleX(1f), entity.getRandomBodyY(), entity.getParticleZ(1f), 0, 0, 0);
                }
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.PLAYERS, 1f, 1f);
                break;
            }
        }
        return newAmount;
    }
}
