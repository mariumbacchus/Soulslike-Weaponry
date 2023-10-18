package net.soulsweaponry.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.UltraHeavyWeapon;
import net.soulsweaponry.util.IEntityDataSaver;
import net.soulsweaponry.util.PostureData;

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
        if (target instanceof LivingEntity living && !living.isDead()) {
            int postureLoss = 5;
            if (user.getStackInHand(Hand.MAIN_HAND).getItem() instanceof UltraHeavyWeapon heavy && heavy.isHeavy()) {
                postureLoss *= 2;
            }
            postureLoss *= level;
            PostureData.addPosture((IEntityDataSaver) living, postureLoss);
        }
        super.onTargetDamaged(user, target, level);
    }

    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof UltraHeavyWeapon || super.isAcceptableItem(stack);
    }
}
