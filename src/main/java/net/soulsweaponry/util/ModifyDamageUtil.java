package net.soulsweaponry.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.arrow.TrueDamageArrow;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.items.IDragonBonus;
import net.soulsweaponry.items.ILifeGuard;
import net.soulsweaponry.items.axe.LeviathanAxe;
import net.soulsweaponry.items.sword.MehrunesRazor;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.ArrayList;
import java.util.List;

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
        if (source.getAttacker() instanceof LivingEntity attacker) {
            List<ItemStack> stacks = new ArrayList<>();
            stacks.add(attacker.getMainHandStack());
            if (WeaponUtil.isFightModLoaded()) {
                stacks.add(attacker.getOffHandStack());
            }
            for (ItemStack heldStack : stacks) {
                Item item = heldStack.getItem();
                // Bonus damage to dragons
                if (entity.getType().isIn(ModTags.Entities.DRAGONS) &&
                        item instanceof IDragonBonus dragonBonus &&
                        !(item instanceof IConfigDisable configDisable && configDisable.isDisabled(heldStack))) {

                    newAmount += dragonBonus.getDragonBonus(heldStack);
                }
                // Mehrunes’ Razor missing health bonus
                if (item instanceof MehrunesRazor) {
                    float ratio = entity.getMaxHealth() >= ConfigConstructor.mehrunes_razor_missing_health_trigger_cap ? ConfigConstructor.mehrunes_razor_missing_health_chance_over_health_cap : ConfigConstructor.mehrunes_razor_missing_health_chance_under_health_cap;
                    if (attacker.getRandom().nextDouble() <= ratio) {
                        double missing = entity.getMaxHealth() - entity.getHealth();
                        float bonus = (float) Math.min(
                                missing * ConfigConstructor.mehrunes_razor_missing_health_modifier
                                * (entity instanceof PlayerEntity ? ConfigConstructor.mehrunes_razor_missing_health_modifier_against_players : 1f),
                                ConfigConstructor.mehrunes_razor_missing_health_max_bonus_damage
                        );
                        newAmount += bonus;
                    }
                }
            }
        }
        if (entity.hasStatusEffect(EffectRegistry.DECAY) && !entity.getEquippedStack(EquipmentSlot.HEAD).isOf(ArmorRegistry.CHAOS_CROWN) && !entity.getEquippedStack(EquipmentSlot.HEAD).isOf(ArmorRegistry.CHAOS_HELMET)) {
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
            float baseAdded = entity instanceof PlayerEntity ? ConfigConstructor.posture_break_player_damage_per_amp : ConfigConstructor.posture_break_damage_per_amp;
            float totalAdded = baseAdded * (amplifier + 1);
            newAmount += totalAdded;
            newAmount += ConfigConstructor.posture_break_percent_health_damage * entity.getMaxHealth();
            entity.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.CRIT_HIT_EVENT, SoundCategory.HOSTILE, .5f, 1f);
            entity.removeStatusEffect(EffectRegistry.POSTURE_BREAK);
            if (entity.hasStatusEffect(StatusEffects.SLOWNESS)) entity.removeStatusEffect(StatusEffects.SLOWNESS);
            if (entity.hasStatusEffect(StatusEffects.WEAKNESS)) entity.removeStatusEffect(StatusEffects.WEAKNESS);
            if (entity.hasStatusEffect(StatusEffects.MINING_FATIGUE)) entity.removeStatusEffect(StatusEffects.MINING_FATIGUE);
            if (entity.hasStatusEffect(EffectRegistry.FREEZING)) {
                LeviathanAxe.iceExplosion(entity.getWorld(), entity.getBlockPos(), entity.getAttacker(), entity.getStatusEffect(EffectRegistry.FREEZING).getAmplifier());
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
        // Inflict percent of damage to held ILifeGuard item instead of damage to the user (not stackable)
        for (Hand hand : Hand.values()) {
            ItemStack stack = entity.getStackInHand(hand);
            if (stack.getItem() instanceof ILifeGuard guard) {
                float damage = Math.max(0, (float) (newAmount * (1D - guard.getLifeGuardPercent(stack))));
                int rounded = Math.round(newAmount);
                newAmount = damage;
                int j = Math.min(rounded, 10);
                for (int i = 0; i < j; i++) {
                    ParticleHandler.singleParticle(entity.getWorld(), ParticleTypes.SOUL, entity.getParticleX(1f), entity.getRandomBodyY(), entity.getParticleZ(1f), 0, 0, 0);
                }
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.PLAYERS, 1f, 1f);
                // Chance to save the player if it's holding ILifeGuard item
                if (entity.getHealth() - newAmount < 0 && !entity.getWorld().isClient && guard.getLifeSaveChance(stack) < entity.getRandom().nextDouble()) {
                    ParticleHandler.particleSphereList(entity.getWorld(), 500, entity.getX(), entity.getY(), entity.getZ(), 0.4f, ParticleTypes.SCULK_SOUL, ParticleTypes.SMOKE);
                    entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 1f, 1f);
                    for (Entity entity1 : entity.getWorld().getOtherEntities(entity, entity.getBoundingBox().expand(2.5D))) {
                        if (entity1 instanceof LivingEntity living) {
                            living.damage(entity.getDamageSources().explosion(entity, entity), guard.getLifeSaveExplosionDamage(stack));
                            double x = entity.getX() - living.getX();
                            double z = entity.getZ() - living.getZ();
                            living.takeKnockback(guard.getLifeSaveExplosionKnockback(stack), x, z);
                        }
                    }
                    newAmount = 0f;
                    rounded += guard.getLifeSaveStackDamage(stack);
                }
                if (rounded > 0) {
                    stack.damage(rounded, entity, (p) -> p.sendToolBreakStatus(hand));
                }
                break;
            }
        }
        return newAmount;
    }
}
