package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.bonusdamage.SpiderBonus;
import net.soulsweaponry.items.abilities.inventorytick.Luminate;

public class Sting extends ModdedSword {

    private static final SpiderBonus SPIDER_BONUS = new SpiderBonus(
            ConfigConstructor.sting_spiders_bane_bonus_arthropod_damage,
            ConfigConstructor.sting_spiders_bane_bonus_arthropod_damage_per_level
    );
    private static final Luminate LUMINATE = new Luminate();

    public Sting(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.sting_damage, ConfigConstructor.sting_attack_speed, settings);
        this.addAbility(SPIDER_BONUS, LUMINATE);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_sting;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_sting;
    }
}