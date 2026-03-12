package net.soulsweaponry.items.abilities.armorattributes;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record Luck(float luck) implements IAbility {

    @Override
    public void addArmorAttributeModifiers(ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder, EquipmentSlot equipmentSlot) {
        EntityAttributeModifier luckMod = WeaponUtil.makeAttribute(EntityAttributes.GENERIC_LUCK, equipmentSlot, this.luck);
        builder.put(EntityAttributes.GENERIC_LUCK, luckMod);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of();
    }
}
