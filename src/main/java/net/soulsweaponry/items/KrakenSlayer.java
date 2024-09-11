package net.soulsweaponry.items;

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
import net.soulsweaponry.util.WeaponUtil;

import java.util.function.Supplier;

public class KrakenSlayer extends ModdedBow {

    public KrakenSlayer(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, repairIngredientSupplier);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.FAST_PULL, WeaponUtil.TooltipAbilities.THIRD_SHOT);
        this.configure(new RangedConfig(ConfigConstructor.kraken_slayer_pull_time_ticks, ConfigConstructor.kraken_slayer_damage, ConfigConstructor.kraken_slayer_max_velocity));
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_kraken_slayer_bow;
    }

    @Override
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        if (bowStack.hasNbt() && bowStack.getNbt().contains("firedShots") && bowStack.getNbt().getInt("firedShots") >= 2) {
            KrakenSlayerProjectile projectile = new KrakenSlayerProjectile(world, shooter);
            projectile.setTrueDamage(ConfigConstructor.kraken_slayer_bonus_true_damage + EnchantmentHelper.getLevel(Enchantments.POWER, bowStack));
            bowStack.getNbt().putInt("firedShots", 0);
            return projectile;
        } else {
            if (bowStack.hasNbt()) {
                if (bowStack.getNbt().contains("firedShots")) {
                    bowStack.getNbt().putInt("firedShots", bowStack.getNbt().getInt("firedShots") + 1);
                } else {
                    bowStack.getNbt().putInt("firedShots", 1);
                }
            }
        }
        return null;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_kraken_slayer_bow;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String getReduceCooldownEnchantId(ItemStack stack) {
        return null;
    }
}