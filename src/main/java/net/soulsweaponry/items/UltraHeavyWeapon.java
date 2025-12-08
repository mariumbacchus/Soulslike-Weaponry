package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.TooltipAbilities;

public abstract class UltraHeavyWeapon extends ChargeToUseItem implements IUltraHeavy, IDetonateGround {

    private final boolean isHeavy;

    public UltraHeavyWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, boolean isHeavy) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.isHeavy = isHeavy;
        this.addTooltipAbility(TooltipAbilities.HEAVY);
    }

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

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return ConfigConstructor.ultra_heavy_disables_shields && this.isHeavy;
    }
}
