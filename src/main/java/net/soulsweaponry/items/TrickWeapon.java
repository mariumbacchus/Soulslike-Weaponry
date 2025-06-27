package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.util.TooltipAbilities;

public class TrickWeapon extends ModdedSword implements IUltraHeavy, IUndeadBonus {

    private final float undeadBonus;
    private final boolean isHeavy;
    private final boolean isFireproof;
    private final boolean isDisabled;
    private final int postureLoss;

    public TrickWeapon(ToolMaterial toolMaterial, int damage, float attackSpeed, Settings settings, boolean isHeavy, int postureLoss, float undeadBonus, boolean isFireproof, boolean isDisabled) {
        super(toolMaterial, damage, attackSpeed, settings);
        this.undeadBonus = undeadBonus;
        this.isHeavy = isHeavy;
        this.isFireproof = isFireproof;
        this.isDisabled = isDisabled;
        this.postureLoss = postureLoss;
        if (this.isHeavy()) {
            this.addTooltipAbility(TooltipAbilities.HEAVY);
        }
        if (this.isRighteous()) {
            this.addTooltipAbility(TooltipAbilities.RIGHTEOUS);
        }
    }

    public TrickWeapon(ToolMaterial toolMaterial, int damage, float attackSpeed, Settings settings, float undeadBonus, boolean isFireproof, boolean isDisabled) {
        this(toolMaterial, damage, attackSpeed, settings, false, 0, undeadBonus, isFireproof, isDisabled);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isHeavy && !this.isDisabled(stack)) {
            this.gainStrength(attacker);
            this.applyPostureLoss(target);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isHeavy() {
        return this.isHeavy;
    }

    @Override
    public int getPostureLoss() {
        return this.postureLoss;
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
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
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
