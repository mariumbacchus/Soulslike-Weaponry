package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.util.WeaponUtil;

public abstract class UltraHeavyWeapon extends DetonateGroundItem implements IUltraHeavy {

    private final boolean isHeavy;

    public UltraHeavyWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, boolean isHeavy) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.isHeavy = isHeavy;
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.HEAVY);
    }

    @Override
    public boolean isHeavy() {
        return isHeavy;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            this.gainStrength(attacker);
        }
        return super.postHit(stack, target, attacker);
    }
}
