package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.ItemRegistry;

import java.util.function.Predicate;

public abstract class GunItem extends BowItem {

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
}
