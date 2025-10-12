package net.soulsweaponry.items.crossbow;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.bow.KrakenSlayer;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.function.Supplier;

public class KrakenSlayerCrossbow extends ModdedCrossbow {

    public KrakenSlayerCrossbow(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, createConfig((int) ConfigConstructor.kraken_slayer_crossbow_pull_time_ticks,
                ConfigConstructor.kraken_slayer_crossbow_damage, ConfigConstructor.kraken_slayer_crossbow_bonus_velocity),
                repairIngredientSupplier);
        this.addTooltipAbility(TooltipAbilities.FAST_PULL, TooltipAbilities.THIRD_SHOT);
    }

    @Override
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        float bonus =  WeaponUtil.getLevel(bowStack, Enchantments.QUICK_CHARGE) / 4f;
        PersistentProjectileEntity newProjectile = KrakenSlayer.getKrakenSlayerProjectile(world, bowStack, shooter, originalArrow.getDamage() + bonus, ConfigConstructor.kraken_slayer_bonus_true_damage);
        originalArrow.setDamage(originalArrow.getDamage() + bonus);
        return newProjectile;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_kraken_slayer_crossbow;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return null;
    }
}