package net.soulsweaponry.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EffectRegistry;

public class MoonstoneRing extends Item {

    public MoonstoneRing(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        user.addStatusEffect(new StatusEffectInstance(EffectRegistry.MOON_HERALD, 600, EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack)));
        user.getItemCooldownManager().set(this, 2400);
        stack.damage(1, user, e -> e.sendToolBreakStatus(hand));
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald").formatted(Formatting.AQUA));
            tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald_description_1").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald_description_2").formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
