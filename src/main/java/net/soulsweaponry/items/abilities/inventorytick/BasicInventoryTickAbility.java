package net.soulsweaponry.items.abilities.inventorytick;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.PentaConsumer;

import java.util.List;

/**
 * Basic record for inventory tick abilities that do a simple thing such as continuously adding an effect to the entity.
 * @param inventoryTick consumer with the params <ItemStack stack, World world, LivingEntity entity, int slot, boolean selected>
 * @param tooltips
 * @param updateRate each tick the inventoryTick consumer is called, for example if it is 100 then every 100 ticks it is called
 */
public record BasicInventoryTickAbility(PentaConsumer<ItemStack, World, LivingEntity, Integer, Boolean> inventoryTick, List<Text> tooltips, int updateRate) implements IAbility {

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity.age % this.updateRate == 0 && entity instanceof LivingEntity livingEntity) {
            this.inventoryTick.accept(stack, world, livingEntity, slot, selected);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return this.tooltips;
    }
}
