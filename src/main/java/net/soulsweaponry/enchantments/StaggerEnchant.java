package net.soulsweaponry.enchantments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.soulsweaponry.items.UltraHeavyWeapon;

public class StaggerEnchant extends Enchantment {

    public StaggerEnchant(Rarity pRarity, EquipmentSlot... pApplicableSlots) {
        super(pRarity, EnchantmentCategory.WEAPON, pApplicableSlots);
    }

    @Override
    public int getMinCost(int level) {
        return 10 + level * 10;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int pLevel) {
        if (target instanceof LivingEntity living && !living.isDeadOrDying()) {
//            int postureLoss = 5;
//            if (user.getStackInHand(Hand.MAIN_HAND).getItem() instanceof UltraHeavyWeapon heavy && heavy.isHeavy()) {
//                postureLoss *= 2;
//            }
//            postureLoss *= level;
//            PostureData.addPosture((IEntityDataSaver) living, postureLoss);TODO add posture data and posture loss mechanic
        }
        super.doPostAttack(attacker, target, pLevel);
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof UltraHeavyWeapon || super.canEnchant(pStack);
    }
}
