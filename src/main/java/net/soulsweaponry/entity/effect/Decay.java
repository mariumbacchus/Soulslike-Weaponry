package net.soulsweaponry.entity.effect;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.entity.mobs.BossEntity;
import net.soulsweaponry.registry.ItemRegistry;

public class Decay extends StatusEffect {

    EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public final int finalTicks = 10;
    int tickRate = finalTicks;

    public Decay() {
        super(StatusEffectCategory.HARMFUL, 0x0e0024);
    }

    public int getTicks() {
        return this.tickRate;
    }

    public int getFinalTickrate() {
        return this.finalTicks;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        this.tickRate--;
        if (tickRate < 0) {
            this.tickRate = this.getFinalTickrate() - amplifier*2;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity) {
            if (!entity.getEquippedStack(slots[0]).isOf(ItemRegistry.CHAOS_CROWN) && !entity.getEquippedStack(slots[0]).isOf(ItemRegistry.CHAOS_HELMET)) {
                PlayerEntity player = ((PlayerEntity)entity);
                for (EquipmentSlot slot : slots) {
                    ItemStack stack = player.getEquippedStack(slot);
                    if (!stack.isOf(ItemRegistry.CHAOS_ROBES)) {
                        stack.damage(amplifier + 1, player, (p_220045_0_) -> p_220045_0_.sendEquipmentBreakStatus(slot));
                    }
                }
            } else {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 50, amplifier));
            }
        }
    }
}
