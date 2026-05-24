package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.statboost.FirstHitBigDamage;

public class CrucibleSword extends ModdedSword {

    private static final FirstHitBigDamage DOOM = new FirstHitBigDamage(
            ConfigConstructor.crucible_sword_empowered_bonus_damage,
            ConfigConstructor.crucible_sword_empowered_bonus_damage_per_level,
            (int) ConfigConstructor.crucible_sword_empowered_min_cooldown,
            (int) ConfigConstructor.crucible_sword_empowered_cooldown,
            (int) ConfigConstructor.crucible_sword_empowered_reduced_cooldown_per_level,
            ((world, player, stack) -> !world.isClient && world.getDimension().ultrawarm()),
            ConfigConstructor.crucible_sword_empowered_cooldown_modifier_in_ultrawarm_dimension
    );

    public CrucibleSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.crucible_sword_normal_damage, ConfigConstructor.crucible_sword_attack_speed, settings);
        this.addAbility(DOOM);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_crucible_sword;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_crucible_sword;
    }
}