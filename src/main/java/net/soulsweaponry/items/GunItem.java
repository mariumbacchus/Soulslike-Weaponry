package net.soulsweaponry.items;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.EnchantRegistry;
import org.jetbrains.annotations.Nullable;

public abstract class GunItem extends BowItem implements IConfigDisable {

    public static final Predicate<ItemStack> SILVER_PROJECTILE = (stack) -> stack.isOf(ItemRegistry.SILVER_BULLET);

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
        if (this.isDisabled()) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.gun_posture_loss").append(Text.literal(String.valueOf(this.getPostureLoss(stack)))).formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.gun_damage").append(Text.literal(String.valueOf(this.getBulletDamage(stack)))).formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.gun_cooldown").append(Text.literal(String.valueOf(this.getCooldown(stack)))).formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.gun_bullets_used").append(Text.literal(String.valueOf(this.bulletsNeeded()))).formatted(Formatting.GRAY));
            if (this.getMaxUseTime(stack) != 0) {
                tooltip.add(Text.translatable("tooltip.soulsweapons.gun_max_use_time").append(Text.literal(String.valueOf(this.getMaxUseTime(stack)))).formatted(Formatting.GRAY));
            }
        }
        else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
