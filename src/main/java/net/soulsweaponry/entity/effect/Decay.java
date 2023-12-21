package net.soulsweaponry.entity.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.soulsweaponry.registry.ItemRegistry;

public class Decay extends MobEffect {

    private final EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public Decay() {
        super(MobEffectCategory.HARMFUL, 0x0e0024);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % (Math.max((10 - amplifier * 2), 1)) == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            if (!entity.getItemBySlot(slots[0]).is(ItemRegistry.CHAOS_CROWN.get()) && !entity.getItemBySlot(slots[0]).is(ItemRegistry.CHAOS_HELMET.get())) {
                for (EquipmentSlot slot : slots) {
                    ItemStack stack = player.getItemBySlot(slot);
                    if (!stack.is(ItemRegistry.CHAOS_ROBES.get())) {
                        stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                    }
                }
            } else {
                entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, amplifier));
            }
        }
    }
}
