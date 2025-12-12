package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;

public record ArmorBreaker(int bonusArmorStackDamage) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        }) {
            ItemStack armorStack = target.getEquippedStack(slot);
            if (!armorStack.isEmpty() && armorStack.isDamageable()) {
                armorStack.damage(
                        this.bonusArmorStackDamage,
                        target,
                        slot
                );
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.armor_breaker").formatted(Formatting.WHITE),
                Text.translatable("tooltip.soulsweapons.armor_breaker.1").formatted(Formatting.GRAY)
        );
    }
}
