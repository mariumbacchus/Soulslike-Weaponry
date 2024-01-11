package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class UltraHeavyWeapon extends SwordItem implements UltraHeavy {

    private final boolean isHeavy;

    public UltraHeavyWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, boolean isHeavy) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.isHeavy = isHeavy;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        this.gainStrength(attacker);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isHeavy() {
        return this.isHeavy;
    }
}
