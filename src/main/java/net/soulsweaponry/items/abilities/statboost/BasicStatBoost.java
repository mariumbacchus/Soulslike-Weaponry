package net.soulsweaponry.items.abilities.statboost;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.PentaPredicate;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record BasicStatBoost(
        PentaPredicate<ItemStack, World, Entity, Integer, Boolean> activatePredicate,
        float bonusDamage, float bonusDamagePerLvl,
        float bonusAttackSpeed, float bonusAttackSpeedPerLvl,
        List<Text> tooltip
) implements IAbility {

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) {
            return;
        }
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        double damage = WeaponUtil.getBaseItemAttackDamage(stack);
        double attackSpeed = WeaponUtil.getBaseItemAttackSpeed(stack);
        if (this.activatePredicate.test(stack, world, entity, slot, selected)) {
            damage += this.bonusDamage + this.bonusDamagePerLvl * lvl;
            attackSpeed += this.bonusAttackSpeed + this.bonusAttackSpeedPerLvl * lvl;
        }
        WeaponUtil.modifyStackAttributes(stack, damage, attackSpeed);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return this.tooltip;
    }
}
