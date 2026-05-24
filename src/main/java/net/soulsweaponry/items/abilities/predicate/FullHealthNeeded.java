package net.soulsweaponry.items.abilities.predicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record FullHealthNeeded(int itemLevelForRemoval) implements IAbility {

    @Override
    public boolean preventUsePredicate(ItemStack stack, PlayerEntity user) {
        return user.getHealth() < user.getMaxHealth() && !user.isCreative() && WeaponUtil.getUpgradeLevel(stack) < this.itemLevelForRemoval;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        if (WeaponUtil.getUpgradeLevel(stack) >= this.itemLevelForRemoval) {
            return List.of();
        }
        return List.of(
                Text.translatable("tooltip.soulsweapons.max_health_needed").formatted(Formatting.BLUE),
                Text.translatable("tooltip.soulsweapons.max_health_needed.1").formatted(Formatting.GRAY)
        );
    }
}
