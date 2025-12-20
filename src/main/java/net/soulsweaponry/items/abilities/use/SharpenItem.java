package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.ISharpened;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.List;
import java.util.Optional;

/**
 * Use to sharpen items if they have an ability that implements {@link ISharpened}.
 */
public class SharpenItem implements IAbility {

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        for (Hand offHand : Hand.values()) {
            ItemStack swordStack = user.getStackInHand(offHand);
            if (swordStack.getItem() instanceof IHasAbilities hasAbilities) {
                Optional<ISharpened> op = hasAbilities.findAbility(ISharpened.class);
                if (op.isPresent() && !ISharpened.isEmpowered(swordStack)) {
                    swordStack.set(ComponentRegistry.SHARPENED_STRIKES, op.get().getMaxEmpoweredStrikes(swordStack));
                    stack.damage(1, user, LivingEntity.getSlotForHand(hand));
                    world.playSound(user, user.getBlockPos(), SoundRegistry.SHARPEN_EVENT, SoundCategory.PLAYERS, .5f, 1f);
                    world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.PLAYERS, .5f, 1f);
                    return ActionResult.SUCCESS.withNewHandStack(user.getStackInHand(hand));
                }
            }
        }
        return ActionResult.FAIL;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.sharpen_item").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.sharpen_item.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.sharpen_item.2").formatted(Formatting.DARK_GRAY, Formatting.ITALIC)
        );
    }
}
