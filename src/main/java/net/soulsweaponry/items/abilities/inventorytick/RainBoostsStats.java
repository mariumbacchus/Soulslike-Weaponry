package net.soulsweaponry.items.abilities.inventorytick;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record RainBoostsStats(float bonusAttackDamage, float bonusAttackSpeed) implements IAbility {

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) {
            return;
        }
        float damage = WeaponUtil.getBaseAttackDamage(stack);
        float attackSpeed = WeaponUtil.getBaseAttackSpeed(stack);
        if (world.isRaining()) {
            damage += this.bonusAttackDamage;
            attackSpeed += this.bonusAttackSpeed;
        }
        WeaponUtil.modifyStackAttributes(stack, damage, attackSpeed);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.weatherborn").formatted(Formatting.DARK_AQUA),
                Text.translatable("tooltip.soulsweapons.weatherborn.description.1").formatted(Formatting.GRAY)
        );
    }
}
