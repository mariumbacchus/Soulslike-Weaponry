package net.soulsweaponry.items.abilities.useonentity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;

public record BasicUseOnEntityAbility(
        ActionResultQuadConsumer<ItemStack, PlayerEntity, LivingEntity, Hand> useOnEntityConsumer,
        List<Text> tooltips
) implements IAbility {

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return this.useOnEntityConsumer.accept(stack, user, entity, hand);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return this.tooltips;
    }
}
