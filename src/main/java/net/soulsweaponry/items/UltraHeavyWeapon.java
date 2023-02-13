package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class UltraHeavyWeapon extends SwordItem {

    public UltraHeavyWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHit(stack, target, attacker);

        if (attacker.hasStatusEffect(StatusEffects.STRENGTH)) {
            attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 200, 1));
        }

        return true;
    }
}
