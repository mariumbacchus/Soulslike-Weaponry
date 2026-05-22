package net.soulsweaponry.items.abilities.predicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record FrostMoonNeeded(int itemLevelForRemoval) implements IAbility {

    @Override
    public boolean preventUsePredicate(ItemStack stack, PlayerEntity user) {
        return !user.hasStatusEffect(EffectRegistry.FROST_MOON) && !user.isCreative() && WeaponUtil.getUpgradeLevel(stack) < this.itemLevelForRemoval;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        if (WeaponUtil.getUpgradeLevel(stack) >= this.itemLevelForRemoval) {
            return List.of();
        }
        return List.of(
                Text.translatable("tooltip.soulsweapons.frost_moon_needed").formatted(Formatting.DARK_AQUA),
                Text.translatable("tooltip.soulsweapons.frost_moon_needed.1").formatted(Formatting.GRAY)
        );
    }
}
