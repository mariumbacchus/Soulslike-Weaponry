package net.soulsweaponry.items.abilities.predicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;

public class FullHealthNeeded implements IAbility {

    @Override
    public boolean preventUsePredicate(ItemStack stack, PlayerEntity user) {
        return user.getHealth() < user.getMaxHealth() && !user.isCreative();
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.max_health_needed").formatted(Formatting.BLUE),
                Text.translatable("tooltip.soulsweapons.max_health_needed.1").formatted(Formatting.GRAY)
        );
    }
}
