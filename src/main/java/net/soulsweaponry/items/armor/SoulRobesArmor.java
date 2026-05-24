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
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class SoulRobesArmor extends ModdedArmor {

    private static final FullSetEquipped SET_BONUS = new FullSetEquipped(
            ArmorRegistry.SOUL_ROBES_HELMET,
            ArmorRegistry.SOUL_ROBES_CHESTPLATE,
            ArmorRegistry.SOUL_ROBES_LEGGINGS,
            ArmorRegistry.SOUL_ROBES_BOOTS
    );
    private static final BasicInventoryTickAbility MAGIC_RESISTANCE = new BasicInventoryTickAbility(
            (stack, world, entity, slot, selected) -> {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION,
                                (int) ConfigConstructor.soul_robes_armor_fortified_night_vision_duration,
                                (int) (ConfigConstructor.soul_robes_armor_fortified_night_vision_amp),
                                false, false
                        )
                );
                entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.MAGIC_RESISTANCE.get(),
                                (int) ConfigConstructor.soul_robes_armor_fortified_magic_resistance_duration,
                                (int) (ConfigConstructor.soul_robes_armor_fortified_magic_resistance_amp
                                        + ConfigConstructor.soul_robes_armor_fortified_magic_resistance_amp_per_level * WeaponUtil.getUpgradeLevel(stack)),
                                false, false
                        )
                );

            },
            List.of(
                    Text.translatable("tooltip.soulsweapons.fortified").formatted(Formatting.BLUE),
                    Text.translatable(
                            "tooltip.soulsweapons.fortified.1",
                            StatusEffects.NIGHT_VISION.getName().copy().append(", ").append(EffectRegistry.MAGIC_RESISTANCE.get().getName())
                    ).formatted(Formatting.GRAY)
            ), 20
    );

    public SoulRobesArmor(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, settings);
        this.addAbility(SET_BONUS, MAGIC_RESISTANCE);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_soul_robes_armor;
    }

    @Override
    public float[] getBleedBuildupResistances() {
        return ConfigConstructor.soul_robes_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ConfigConstructor.soul_robes_bleed_damage_resistances;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ConfigConstructor.soul_robes_posture_buildup_resistances;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ConfigConstructor.soul_robes_base_posture_increase;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_soul_robes_set;
    }
}