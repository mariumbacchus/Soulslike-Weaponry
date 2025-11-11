package net.soulsweaponry.items.abilities.armorattributes;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.AttributeRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

/**
 * Resistances for armor items, feet are at index 0.
 * @param bleedBuildupResistances bleed buildup resistances
 * @param bleedDamageResistances bleed damage resistances
 */
public record BleedResistance(float[] bleedBuildupResistances, float[] bleedDamageResistances) implements IAbility {

    @Override
    public void addArmorAttributeModifiers(AttributeModifiersComponent.Builder builder, EquipmentSlot equipmentSlot, AttributeModifierSlot attributeModifierSlot) {
        if (this.bleedBuildupResistances == null || this.bleedDamageResistances == null) {
            return;
        }
        EntityAttributeModifier bleedBuildup = WeaponUtil.makeAttribute(AttributeRegistry.BLEED_BUILDUP_RESISTANCE, equipmentSlot, this.bleedBuildupResistances);
        EntityAttributeModifier bleedDamage = WeaponUtil.makeAttribute(AttributeRegistry.BLEED_DAMAGE_RESISTANCE, equipmentSlot, this.bleedDamageResistances);
        if (bleedBuildup != null) {
            builder.add(AttributeRegistry.BLEED_BUILDUP_RESISTANCE, bleedBuildup, attributeModifierSlot);
        }
        if (bleedDamage != null) {
            builder.add(AttributeRegistry.BLEED_DAMAGE_RESISTANCE, bleedDamage, attributeModifierSlot);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of();
    }
}
