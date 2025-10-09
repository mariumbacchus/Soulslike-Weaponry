package net.soulsweaponry.items;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.items.abilities.bonusdamage.UndeadBonus;

public class TrickWeapon extends ModdedSword {

    private final boolean isDisabled;

    public TrickWeapon(ToolMaterial toolMaterial, int damage, float attackSpeed, Settings settings, boolean isDisabled, float baseUndeadBonus, float undeadBonusPerLvl) {
        super(toolMaterial, damage, attackSpeed, settings);
        this.isDisabled = isDisabled;
        if (baseUndeadBonus > 0f) {
            UndeadBonus undeadBonusAbility = new UndeadBonus(baseUndeadBonus, undeadBonusPerLvl);
            this.addAbility(undeadBonusAbility);
        }
    }

    public TrickWeapon(ToolMaterial toolMaterial, int damage, float attackSpeed, Settings settings, boolean isDisabled) {
        this(toolMaterial, damage, attackSpeed, settings, isDisabled, 0, 0);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return this.isDisabled;
    }
}
