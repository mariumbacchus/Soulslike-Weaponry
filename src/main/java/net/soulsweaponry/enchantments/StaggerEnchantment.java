package net.soulsweaponry.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.UltraHeavyWeapon;

public class StaggerEnchantment extends Enchantment {

    public StaggerEnchantment(Rarity weight, EquipmentSlot[] slotTypes) {
        super(weight, EnchantmentTarget.WEAPON, slotTypes);
    }
    
    @Override
    public int getMinPower(int level) {
        return 10 + level * 10;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (target instanceof LivingEntity) {
            int random = user.getRandom().nextInt(10);
            int chance = level;
            if (user.getHandItems() instanceof UltraHeavyWeapon) {
                chance+= 2;
            }
            if (random < chance) {
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, level));
            }
        }
        super.onTargetDamaged(user, target, level);
    }

    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof UltraHeavyWeapon ? true : super.isAcceptableItem(stack);
    }
}
