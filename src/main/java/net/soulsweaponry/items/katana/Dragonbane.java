package net.soulsweaponry.items.katana;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.bonusdamage.DragonBonus;
import net.soulsweaponry.items.abilities.posthit.ChainLightningAbility;

public class Dragonbane extends ModdedSword {

    private static final ChainLightningAbility CHAIN_LIGHTNING_ABILITY = new ChainLightningAbility(
            WeaponConfig.dragonbane_chain_lightning_base_range, WeaponConfig.dragonbane_chain_lightning_bonus_range_per_level,
            WeaponConfig.dragonbane_chain_lightning_base_damage, WeaponConfig.dragonbane_chain_lightning_bonus_damage_per_level
    );
    private static final DragonBonus DRAGON_BONUS = new DragonBonus(
            WeaponConfig.dragonbane_dragons_scourge_base_bonus, WeaponConfig.dragonbane_dragons_scourge_bonus_per_level
    );

    public Dragonbane(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.dragonbane_damage, WeaponConfig.dragonbane_attack_speed, settings);
        this.addAbility(DRAGON_BONUS, CHAIN_LIGHTNING_ABILITY);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_dragonbane;
    }
}