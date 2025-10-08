package net.soulsweaponry.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.util.TooltipAbilities;

public class TrickWeapon extends ModdedSword implements IUndeadBonus {

    private final float undeadBonus;
    private final boolean isDisabled;

    public TrickWeapon(ToolMaterial toolMaterial, int damage, float attackSpeed, Settings settings, float undeadBonus, boolean isDisabled) {
        super(toolMaterial, damage, attackSpeed, settings);
        this.undeadBonus = undeadBonus;
        this.isDisabled = isDisabled;
        if (this.isRighteous()) {
            this.addTooltipAbility(TooltipAbilities.RIGHTEOUS);
        }
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return this.isDisabled;
    }

    @Override
    public boolean isRighteous() {
        return this.undeadBonus > 0;
    }

    @Override
    public float getUndeadBonus(ItemStack stack) {
        return this.undeadBonus;
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return this.getUndeadBonusAttackDamage(target, baseAttackDamage, damageSource);
    }
}
