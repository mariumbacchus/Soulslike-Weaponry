package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.stoppedusing.ShootMoonlight;

import java.util.List;

public class MoonlightGreatsword extends ModdedSword {

    private static final ShootMoonlight SHOOT_MOONLIGHT = new ShootMoonlight(
            (int) WeaponConfig.moonlight_greatsword_projectile_amount,
            WeaponConfig.moonlight_greatsword_bonus_projectile_amount_per_level,
            WeaponConfig.moonlight_greatsword_projectile_velocity,
            WeaponConfig.moonlight_greatsword_projectile_damage,
            WeaponConfig.moonlight_greatsword_projectile_bonus_damage_per_level
    );

    public MoonlightGreatsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.moonlight_greatsword_damage, WeaponConfig.moonlight_greatsword_attack_speed, settings);
        this.addAbility(SHOOT_MOONLIGHT);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_moonlight_greatsword;
    }

    @Override
    public List<Text> getItemLore() {
        return List.of(
                Text.translatable("tooltip.soulsweapons.moonlight_greatsword.part_1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.moonlight_greatsword.part_2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.moonlight_greatsword.part_3").formatted(Formatting.DARK_GRAY)
        );
    }
}