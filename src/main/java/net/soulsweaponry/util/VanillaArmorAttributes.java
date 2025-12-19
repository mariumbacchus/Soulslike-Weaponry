package net.soulsweaponry.util;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.util.Identifier;

public class VanillaArmorAttributes {

    public static AttributeModifiersComponent create(ArmorMaterial material, EquipmentType type) {
        int defense = material.defense().getOrDefault(type, 0);

        AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
        AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(type.getEquipmentSlot());
        Identifier id = Identifier.ofVanilla("armor." + type.getName());

        builder.add(EntityAttributes.ARMOR,
                new EntityAttributeModifier(id, defense, EntityAttributeModifier.Operation.ADD_VALUE),
                slot);

        builder.add(EntityAttributes.ARMOR_TOUGHNESS,
                new EntityAttributeModifier(id, material.toughness(), EntityAttributeModifier.Operation.ADD_VALUE),
                slot);

        if (material.knockbackResistance() > 0.0F) {
            builder.add(EntityAttributes.KNOCKBACK_RESISTANCE,
                    new EntityAttributeModifier(id, material.knockbackResistance(), EntityAttributeModifier.Operation.ADD_VALUE),
                    slot);
        }

        return builder.build();
    }
}
