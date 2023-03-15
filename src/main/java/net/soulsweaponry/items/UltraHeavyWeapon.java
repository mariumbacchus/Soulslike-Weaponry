package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class UltraHeavyWeapon extends SwordItem {

    private final boolean isHeavy;

    public UltraHeavyWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, boolean isHeavy) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.isHeavy = isHeavy;
    }
    
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isHeavy && attacker.hasStatusEffect(StatusEffects.STRENGTH)) {
            attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 200, 1));
        }
        return super.postHit(stack, target, attacker);
    }

    public boolean isHeavy() {
        return this.isHeavy;
    }
}
