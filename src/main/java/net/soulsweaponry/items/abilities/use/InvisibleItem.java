package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.ComponentRegistry;

import java.util.List;
import java.util.Optional;

public class InvisibleItem implements IAbility {

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        Boolean invisible = stack.get(ComponentRegistry.INVISIBLE);
        if (invisible != null) {
            stack.set(ComponentRegistry.INVISIBLE, !invisible);
        } else {
            stack.set(ComponentRegistry.INVISIBLE, true);
        }
        this.applyItemCooldown(stack, user, 20);
        user.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.75f);
        return ActionResult.SUCCESS;
    }

    public static boolean isInvisible(ItemStack stack) {
        return Optional.ofNullable(stack.get(ComponentRegistry.INVISIBLE)).orElse(false);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.transparent").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.transparent.1").formatted(Formatting.GRAY)
        );
    }
}
