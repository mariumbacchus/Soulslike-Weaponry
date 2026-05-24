package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.NbtHelper;
import net.soulsweaponry.util.NbtIds;

import java.util.List;

public class InvisibleItem implements IAbility {

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        boolean invisible = isInvisible(stack);
        NbtHelper.putBoolean(stack, NbtIds.INVISIBLE, !invisible);
        this.applyItemCooldown(stack, user, 20);
        user.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.75f);
        return TypedActionResult.success(stack);
    }

    public static boolean isInvisible(ItemStack stack) {
        return NbtHelper.getBoolean(stack, NbtIds.INVISIBLE, false);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.transparent").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.transparent.1").formatted(Formatting.GRAY)
        );
    }
}
