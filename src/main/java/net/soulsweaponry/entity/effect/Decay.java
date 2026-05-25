package net.soulsweaponry.entity.effect;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class Decay extends StatusEffect {

    EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public Decay() {
        super(StatusEffectCategory.HARMFUL, 0x0e0024);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % (Math.max((10 - amplifier * 2), 1)) == 0;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            for (EquipmentSlot slot : slots) {
                ItemStack stack = entity.getEquippedStack(slot);
                stack.damage(amplifier + 1, player, (p) -> p.sendEquipmentBreakStatus(slot));
            }
        }
    }
}