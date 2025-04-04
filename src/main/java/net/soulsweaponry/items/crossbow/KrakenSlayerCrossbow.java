package net.soulsweaponry.items.crossbow;

import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.KrakenSlayerProjectile;
import net.soulsweaponry.items.ModdedCrossbow;
import net.soulsweaponry.util.TooltipAbilities;

import java.util.function.Supplier;

public class KrakenSlayerCrossbow extends ModdedCrossbow {

    public KrakenSlayerCrossbow(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, repairIngredientSupplier);
        this.addTooltipAbility(TooltipAbilities.FAST_PULL, TooltipAbilities.THIRD_SHOT);
        this.configure(new RangedConfig(ConfigConstructor.kraken_slayer_crossbow_pull_time_ticks, ConfigConstructor.kraken_slayer_crossbow_damage, ConfigConstructor.kraken_slayer_crossbow_max_velocity));
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_kraken_slayer_crossbow;
    }

    @Override
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        float bonus =  EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, bowStack) / 4f;
        if (bowStack.hasNbt() && bowStack.getNbt().contains("firedShots") && bowStack.getNbt().getInt("firedShots") >= 2) {
            KrakenSlayerProjectile projectile = new KrakenSlayerProjectile(world, shooter);
            projectile.setTrueDamage(ConfigConstructor.kraken_slayer_bonus_true_damage);
            projectile.setDamage(originalArrow.getDamage() + bonus);
            bowStack.getNbt().putInt("firedShots", 0);
            return projectile;
        } else {
            if (bowStack.hasNbt() && bowStack.getNbt().contains("firedShots")) {
                bowStack.getNbt().putInt("firedShots", bowStack.getNbt().getInt("firedShots") + 1);
            } else {
                bowStack.getOrCreateNbt().putInt("firedShots", 1);
            }
            originalArrow.setDamage(originalArrow.getDamage() + bonus);
        }
        return null;
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