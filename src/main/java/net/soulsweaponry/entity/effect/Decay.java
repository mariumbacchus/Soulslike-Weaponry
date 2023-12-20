package net.soulsweaponry.entity.effect;

import net.minecraft.server.level.ServerPlayer;
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

    EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public final int finalTicks = 10;
    int tickRate = finalTicks;
    //TODO; why are there getters and setters here? consider fixing

    public Decay() {
        super(MobEffectCategory.HARMFUL, 0x0e0024);
    }

    public int getFinalTickrate() {
        return this.finalTicks;
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int amplifier) {
        this.tickRate--;
        if (tickRate < 0) {
            this.tickRate = this.getFinalTickrate() - amplifier * 2;
            return true;
        } else {
            return false;
        }
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
