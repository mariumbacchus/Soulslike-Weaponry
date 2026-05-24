package net.soulsweaponry.items.scythe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.targetdeath.SoulHarvestTransform;
import net.soulsweaponry.registry.WeaponRegistry;

public class DarkinScythePre extends ModdedSword {

    private static final SoulHarvestTransform SOUL_HARVEST_TRANSFORM = new SoulHarvestTransform(
            (int) ConfigConstructor.darkin_scythe_max_souls,
            WeaponRegistry.SHADOW_ASSASSIN_SCYTHE,
            WeaponRegistry.DARKIN_SCYTHE_PRIME,
            ConfigConstructor.darkin_scythe_bonus_damage
    );

    public DarkinScythePre(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.darkin_scythe_damage, ConfigConstructor.darkin_scythe_attack_speed, settings);
        this.addAbility(SOUL_HARVEST_TRANSFORM);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_darkin_scythe;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_darkin_scythe;
    }
}