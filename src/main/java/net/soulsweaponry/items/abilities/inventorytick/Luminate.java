package net.soulsweaponry.items.abilities.inventorytick;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.ComponentRegistry;

import java.util.List;
import java.util.Optional;

/**
 * TODO for another time: Make the item light up if it is active like how Dynamic Lights or shaders do it
 * Remember to add an item model predicate if the texture should change too.
 */
public class Luminate implements IAbility {

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        this.setActive(stack, this.isHostileAround(world, entity));
    }

    private boolean isHostileAround(World world, Entity holder) {
        for (Entity around : world.getOtherEntities(holder, holder.getBoundingBox().expand(16))) {
            if (around instanceof HostileEntity) {
                return true;
            }
        }
        return false;
    }

    private void setActive(ItemStack stack, boolean bl) {
        stack.set(ComponentRegistry.LUMINATE, bl);
    }

    public static boolean isActive(ItemStack stack) {
        return Optional.ofNullable(stack.get(ComponentRegistry.LUMINATE)).orElse(false);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.luminate").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.luminate.1").formatted(Formatting.GRAY)
        );
    }
}
