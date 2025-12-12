package net.soulsweaponry.items.abilities.predicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;
import java.util.function.Supplier;

/**
 * Requires each armor slot to match the input items for the abilities to work.
 */
public record FullSetEquipped(Supplier<Item> head, Supplier<Item> chest, Supplier<Item> legs, Supplier<Item> boots) implements IAbility {

    @Override
    public boolean preventUsePredicate(ItemStack stack, PlayerEntity user) {
        ItemStack boots = user.getInventory().getArmorStack(0);
        ItemStack leggings = user.getInventory().getArmorStack(1);
        ItemStack chestplate = user.getInventory().getArmorStack(2);
        ItemStack helmet = user.getInventory().getArmorStack(3);
        boolean bootsSlot = boots.isOf(this.boots.get());
        boolean leggingsSlot = leggings.isOf(this.legs.get());
        boolean chestSlot = chestplate.isOf(this.chest.get());
        boolean helmetSlot = helmet.isOf(this.head.get());
        return !bootsSlot || !leggingsSlot || !chestSlot || !helmetSlot || !helmet.equals(stack);
        // Only make the head get called instead of each 4 items so the ability isn't called many times
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.set_bonus").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.set_bonus.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.set_bonus.2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.set_bonus.3",
                        this.head.get().getName(),
                        this.chest.get().getName()
                ).formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.set_bonus.4",
                        this.legs.get().getName(),
                        this.boots.get().getName()
                ).formatted(Formatting.DARK_GRAY)
        );
    }
}
