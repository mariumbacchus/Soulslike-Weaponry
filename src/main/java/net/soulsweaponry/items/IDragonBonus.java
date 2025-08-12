package net.soulsweaponry.items;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.util.ModTags;
import net.soulsweaponry.util.WeaponUtil;

/**
 * Items of this interface deals more damage to entities inside {@link net.soulsweaponry.util.ModTags.Entities#DRAGONS} tag
 */
public interface IDragonBonus {

    float getBaseDragonBonus(ItemStack stack);

    default float getTotalDragonBonus(ItemStack stack) {
        return this.getBaseDragonBonus(stack) + WeaponUtil.getLevel(stack, Enchantments.SWEEPING_EDGE);
    }

    default float getDragonBonus(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if (target.getType().isIn(ModTags.Entities.DRAGONS)) {
            if (damageSource.getAttacker() instanceof PlayerEntity player) {
                ItemStack stack = player.getMainHandStack();
                if (this instanceof IConfigDisable config && config.isDisabled(stack)) {
                    return 0;
                }
                return this.getTotalDragonBonus(stack);
            }
        }
        return 0;
    }
}
