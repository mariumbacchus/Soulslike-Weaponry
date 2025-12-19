package net.soulsweaponry.items.armor;

import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.inventorytick.SoulFeast;
import net.soulsweaponry.items.abilities.predicate.FullSetEquipped;
import net.soulsweaponry.registry.ArmorRegistry;

public class ForlornArmor extends ModdedArmor {

    private static final FullSetEquipped SET_BONUS = new FullSetEquipped(
            () -> ArmorRegistry.FORLORN_HELMET,
            () -> ArmorRegistry.FORLORN_CHESTPLATE,
            () -> ArmorRegistry.FORLORN_LEGGINGS,
            () -> ArmorRegistry.FORLORN_BOOTS
    );
    private static final SoulFeast SOUL_FEAST = new SoulFeast(
            ConfigConstructor.forlorn_armor_soul_feast_range,
            ConfigConstructor.forlorn_armor_soul_feast_bonus_range_per_level,
            ConfigConstructor.forlorn_armor_soul_feast_heal,
            ConfigConstructor.forlorn_armor_soul_feast_bonus_heal_per_level
    );

    public ForlornArmor(ArmorMaterial material, EquipmentType type, Settings settings) {
        super(material, type, settings, applyCustomAttributeAbilities(
                ConfigConstructor.forlorn_armor_posture_buildup_resistances,
                ConfigConstructor.forlorn_armor_base_posture_increase,
                ConfigConstructor.forlorn_armor_bleed_buildup_resistances,
                ConfigConstructor.forlorn_armor_bleed_damage_resistances
        ));
        this.addAbility(SET_BONUS, SOUL_FEAST);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_forlorn_armor;
    }
}