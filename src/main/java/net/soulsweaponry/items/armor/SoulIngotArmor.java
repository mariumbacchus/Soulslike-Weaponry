package net.soulsweaponry.items.armor;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.inventorytick.BasicInventoryTickAbility;
import net.soulsweaponry.items.abilities.predicate.FullSetEquipped;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class SoulIngotArmor extends ModdedArmor {

    private static final FullSetEquipped SET_BONUS = new FullSetEquipped(
            () -> ArmorRegistry.SOUL_INGOT_HELMET,
            () -> ArmorRegistry.SOUL_INGOT_CHESTPLATE,
            () -> ArmorRegistry.SOUL_INGOT_LEGGINGS,
            () -> ArmorRegistry.SOUL_INGOT_BOOTS
    );
    private static final BasicInventoryTickAbility RESISTANCE = new BasicInventoryTickAbility(
            (stack, world, entity, slot, selected) -> entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,
                    (int) ConfigConstructor.soul_ingot_armor_fortified_resistance_duration,
                    (int) (ConfigConstructor.soul_ingot_armor_fortified_resistance_amp
                            + ConfigConstructor.soul_ingot_armor_fortified_resistance_amp_per_level * WeaponUtil.getUpgradeLevel(stack)),
                    false, false)),
            List.of(
                    Text.translatable("tooltip.soulsweapons.fortified").formatted(Formatting.BLUE),
                    Text.translatable("tooltip.soulsweapons.fortified.1", StatusEffects.RESISTANCE.getName()).formatted(Formatting.GRAY)
            ), 20
    );

    public SoulIngotArmor(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, settings);
        this.addAbility(SET_BONUS, RESISTANCE);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_soul_ingot_armor;
    }

    @Override
    public float[] getBleedBuildupResistances() {
        return ConfigConstructor.soul_ingot_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ConfigConstructor.soul_ingot_bleed_damage_resistances;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ConfigConstructor.soul_ingot_posture_buildup_resistances;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ConfigConstructor.soul_ingot_base_posture_increase;
    }
}