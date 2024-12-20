package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.util.WeaponUtil;

public class TrickWeapon extends ModdedSword implements IUltraHeavy, IUndeadBonus {

    private final float undeadBonus;
    private final boolean isHeavy;
    private final boolean isFireproof;
    private final boolean isDisabled;

    public TrickWeapon(ToolMaterial toolMaterial, int damage, float attackSpeed, Settings settings, boolean isHeavy, float undeadBonus, boolean isFireproof, boolean isDisabled) {
        super(toolMaterial, damage, attackSpeed, settings);
        this.undeadBonus = undeadBonus;
        this.isHeavy = isHeavy;
        this.isFireproof = isFireproof;
        this.isDisabled = isDisabled;
        if (this.isHeavy()) {
            this.addTooltipAbility(WeaponUtil.TooltipAbilities.HEAVY);
        }
        if (this.isRighteous()) {
            this.addTooltipAbility(WeaponUtil.TooltipAbilities.RIGHTEOUS);
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isHeavy) {
            this.gainStrength(attacker);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isHeavy() {
        return this.isHeavy;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return this.isDisabled;
    }

    @Override
    public boolean isFireproof() {
        return this.isFireproof;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String getReduceCooldownEnchantId(ItemStack stack) {
        return null;
    }

    @Override
    public boolean isRighteous() {
        return this.undeadBonus > 0;
    }

    @Override
    public float getUndeadBonus(ItemStack stack) {
        return this.undeadBonus;
    }
}
