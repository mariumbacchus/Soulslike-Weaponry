package net.soulsweaponry.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.soulsweaponry.registry.EnchantmentRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public abstract class GunItem extends BowItem {

    public GunItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (stack -> stack.is(ItemRegistry.SILVER_BULLET.get()));
    }

    public int getReducedCooldown(ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.FAST_HANDS, stack) * 8;
    }

    public abstract int getPostureLoss(ItemStack stack);
    public abstract int getDamage(ItemStack stack);
    public abstract int getCooldown(ItemStack stack);
    public abstract int bulletsNeeded();
    public int getMaxUseTime(ItemStack stack) {
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.gun_posture_loss").append(new TextComponent(String.valueOf(this.getPostureLoss(stack)))).withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.gun_damage").append(new TextComponent(String.valueOf(this.getDamage(stack)))).withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.gun_cooldown").append(new TextComponent(String.valueOf(this.getCooldown(stack)))).withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.gun_bullets_used").append(new TextComponent(String.valueOf(this.bulletsNeeded()))).withStyle(ChatFormatting.GRAY));
            if (this.getMaxUseTime(stack) != 0) {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.gun_max_use_time").append(new TextComponent(String.valueOf(this.getMaxUseTime(stack)))).withStyle(ChatFormatting.GRAY));
            }
        }
        else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }
}
