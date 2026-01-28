package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.statboost.BasicStatBoost;

import java.util.List;

public class Draugr extends ModdedSword {

    private static final BasicStatBoost NIGHT_BONUS = new BasicStatBoost(
            ((stack, world, entity, slot, selected) -> world.getDimension().hasSkyLight() && world.isNight()),
            ConfigConstructor.draugr_bonus_damage_at_night,
            ConfigConstructor.draugr_bonus_damage_per_level_at_night,
            ConfigConstructor.draugr_bonus_attack_speed_at_night,
            ConfigConstructor.draugr_bonus_attack_speed_per_level_at_night,
            List.of(
                    Text.translatable("tooltip.soulsweapons.night_prowler").formatted(Formatting.DARK_AQUA),
                    Text.translatable("tooltip.soulsweapons.night_prowler.1").formatted(Formatting.GRAY)
            )
    );

    public Draugr(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.draugr_normal_damage, ConfigConstructor.draugr_attack_speed, settings);
        this.addAbility(NIGHT_BONUS);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_draugr;
    }

    @Override
    public List<Text> getAdditionalItemTooltips() {
        return List.of(
                Text.translatable("tooltip.soulsweapons.draugr_info.part_1").formatted(Formatting.DARK_GRAY)
        );
    }
}