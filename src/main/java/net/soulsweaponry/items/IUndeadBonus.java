package net.soulsweaponry.items;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.EntityTypeTags;
import net.soulsweaponry.util.WeaponUtil;

public interface IUndeadBonus {

    boolean isRighteous();
    float getUndeadBonus(ItemStack stack);

    /**
     * To be called inside {@link net.minecraft.item.Item#getBonusAttackDamage(Entity, float, DamageSource)}
     */
    default float getUndeadBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if (target.getType().isIn(EntityTypeTags.UNDEAD) && this instanceof IUndeadBonus undeadBonus && undeadBonus.isRighteous()) {
            if (damageSource.getAttacker() instanceof PlayerEntity player) {
                ItemStack stack = player.getMainHandStack();
                return undeadBonus.getUndeadBonus(stack) + (float) WeaponUtil.getLevel(stack, Enchantments.FIRE_ASPECT);
            }
        }
        return 0;
    }
}
