package net.soulsweaponry.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.soulsweaponry.entitydata.posture.PostureData;
import net.soulsweaponry.items.UltraHeavy;

public class StaggerEnchantment extends Enchantment {

    public StaggerEnchantment(Rarity pRarity, EquipmentSlot... pApplicableSlots) {
        super(pRarity, EnchantmentTarget.WEAPON, pApplicableSlots);
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
            if (user.getStackInHand(Hand.MAIN_HAND).getItem() instanceof UltraHeavy heavy && heavy.isHeavy()) {
                postureLoss *= 2;
            }
            postureLoss *= level;
            PostureData.addPosture(living, postureLoss);
        }
        super.onTargetDamaged(user, target, level);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof UltraHeavy || super.isAcceptableItem(stack);
    }
}
