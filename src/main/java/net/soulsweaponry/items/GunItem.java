package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public abstract class GunItem extends BowItem implements IConfigDisable {

    public static final Predicate<ItemStack> SILVER_PROJECTILE = (stack) -> stack.isOf(ItemRegistry.SILVER_BULLET.get());

    public GunItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public Predicate<ItemStack> getProjectiles() {
        return SILVER_PROJECTILE;
    }

    public int getReducedCooldown(ItemStack stack) {
        return EnchantmentHelper.getLevel(EnchantRegistry.FAST_HANDS, stack) * 8;
    }

    public abstract int getPostureLoss(ItemStack stack);
    public abstract int getBulletDamage(ItemStack stack);
    public abstract int getCooldown(ItemStack stack);
    public abstract int bulletsNeeded();
    public int getMaxUseTime(ItemStack stack) {
        return 0;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.isDisabled(stack)) {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.disabled"));
        }
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.gun_posture_loss").append(new LiteralText(String.valueOf(this.getPostureLoss(stack)))).formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.gun_damage").append(new LiteralText(String.valueOf(this.getBulletDamage(stack)))).formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.gun_cooldown").append(new LiteralText(String.valueOf(this.getCooldown(stack)))).formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.gun_bullets_used").append(new LiteralText(String.valueOf(this.bulletsNeeded()))).formatted(Formatting.GRAY));
            if (this.getMaxUseTime(stack) != 0) {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.gun_max_use_time").append(new LiteralText(String.valueOf(this.getMaxUseTime(stack)))).formatted(Formatting.GRAY));
            }
        }
        else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
