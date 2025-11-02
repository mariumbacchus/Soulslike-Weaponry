package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.stoppedusing.ShootMoonlight;

public class PureMoonlightGreatsword extends ModdedSword {

    private static final ShootMoonlight SHOOT_MOONLIGHT = new ShootMoonlight(
            (int) ConfigConstructor.pure_moonlight_greatsword_projectile_amount,
            ConfigConstructor.pure_moonlight_greatsword_bonus_projectile_amount_per_level,
            ConfigConstructor.pure_moonlight_greatsword_projectile_velocity,
            ConfigConstructor.pure_moonlight_greatsword_projectile_damage,
            ConfigConstructor.pure_moonlight_greatsword_projectile_bonus_damage_per_level
    );

    public PureMoonlightGreatsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.pure_moonlight_greatsword_damage, ConfigConstructor.pure_moonlight_greatsword_attack_speed, settings);
        this.addAbility(SHOOT_MOONLIGHT);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_pure_moonlight_greatsword;
    }
}