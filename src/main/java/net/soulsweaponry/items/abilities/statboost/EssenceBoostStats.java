package net.soulsweaponry.items.abilities.statboost;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IHasEssence;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record EssenceBoostStats(float maxDamageBoost, float maxAttackSpeedBoost, int maxEssence) implements IHasEssence {

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) {
            return;
        }
        float damage = WeaponUtil.getBaseAttackDamage(stack);
        float attackSpeed = WeaponUtil.getBaseAttackSpeed(stack);
        float per = (float) IHasEssence.getEssence(stack) / this.maxEssence;
        damage += this.maxDamageBoost * per;
        attackSpeed += this.maxAttackSpeedBoost * per;
        WeaponUtil.modifyStackAttributes(stack, damage, attackSpeed);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.essence_boost").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.essence_boost.1").formatted(Formatting.GRAY)
        );
    }

    @Override
    public int getMaxEssence() {
        return this.maxEssence;
    }
}
