package net.soulsweaponry.items.abilities.armorattributes;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.AttributeRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

/**
 * Resistances for armor items, feet are at index 0.
 * @param postureBuildupResistances posture buildup resistances
 * @param basePostureIncrease max posture increase
 */
public record PostureResistance(float[] postureBuildupResistances, float[] basePostureIncrease) implements IAbility {

    @Override
    public void addArmorAttributeModifiers(ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder, EquipmentSlot equipmentSlot) {
        if (this.postureBuildupResistances == null || this.basePostureIncrease == null) {
            return;
        }
        EntityAttributeModifier postureBuildup = WeaponUtil.makeAttribute(AttributeRegistry.POSTURE_BUILDUP_RESISTANCE, equipmentSlot, this.postureBuildupResistances);
        EntityAttributeModifier basePostureIncrease = WeaponUtil.makeAttribute(AttributeRegistry.BASE_POSTURE_INCREASE, equipmentSlot, this.basePostureIncrease);
        if (postureBuildup != null) {
            builder.put(AttributeRegistry.POSTURE_BUILDUP_RESISTANCE, postureBuildup);
        }
        if (basePostureIncrease != null) {
            builder.put(AttributeRegistry.BASE_POSTURE_INCREASE, basePostureIncrease);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of();
    }
}
