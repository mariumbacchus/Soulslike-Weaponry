package net.soulsweaponry.items.bow;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.customarrows.SilverArrows;

public class SimonsBowblade extends ModdedBow {

    private static final SilverArrows SILVER_ARROWS = new SilverArrows(
            (int) ConfigConstructor.simons_bowblade_silver_arrows_posture_loss,
            ConfigConstructor.simons_bowblade_silver_arrows_bonus_posture_loss_per_level,
            ConfigConstructor.simons_bowblade_silver_arrows_base_undead_bonus_damage,
            ConfigConstructor.simons_bowblade_silver_arrows_undead_bonus_damage_per_level
    );

    public SimonsBowblade(Settings settings, TagKey<Item> repairTag) {
        super(settings, createConfig((int) ConfigConstructor.simons_bowblade_pull_time_ticks,
                ConfigConstructor.simons_bowblade_projectile_damage, ConfigConstructor.simons_bowblade_bonus_velocity),
                repairTag);
        this.addAbility(SILVER_ARROWS);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_simons_bowblade;
    }
}
