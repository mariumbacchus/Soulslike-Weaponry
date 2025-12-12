package net.soulsweaponry.items.abilities.armorattributes;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record Luck(float luck) implements IAbility {

    @Override
    public void addArmorAttributeModifiers(AttributeModifiersComponent.Builder builder, EquipmentSlot equipmentSlot, AttributeModifierSlot attributeModifierSlot) {
        EntityAttributeModifier luckMod = WeaponUtil.makeAttribute(EntityAttributes.GENERIC_LUCK, equipmentSlot, this.luck);
        builder.add(EntityAttributes.GENERIC_LUCK, luckMod, attributeModifierSlot);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of();
    }
}
