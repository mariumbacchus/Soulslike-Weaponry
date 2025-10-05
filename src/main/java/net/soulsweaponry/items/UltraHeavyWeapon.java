package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.util.TooltipAbilities;

public abstract class UltraHeavyWeapon extends ChargeToUseItem implements IUltraHeavy, IDetonateGround {

    private final boolean isHeavy;

    public UltraHeavyWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, boolean isHeavy) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.isHeavy = isHeavy;
        this.addTooltipAbility(TooltipAbilities.HEAVY);
    }//TODO make this ability based to there is no need for TooltipAbilities.HEAVY

    @Override
    public boolean isHeavy() {
        return isHeavy;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            this.gainStrength(attacker);
            this.applyPostureLoss(target);
        }
        return super.postHit(stack, target, attacker);
    }
}
