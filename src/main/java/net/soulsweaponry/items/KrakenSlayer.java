package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.projectile_damage.api.IProjectileWeapon;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.KrakenSlayerProjectile;
import net.soulsweaponry.util.WeaponUtil;

public class KrakenSlayer extends ModdedBow {

    public KrakenSlayer(Settings settings) {
        super(settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.FAST_PULL, WeaponUtil.TooltipAbilities.THIRD_SHOT);
        ((IProjectileWeapon)this).setProjectileDamage(ConfigConstructor.kraken_slayer_damage);
        ((IProjectileWeapon)this).setCustomLaunchVelocity((double) ConfigConstructor.kraken_slayer_max_velocity);
    }

    @Override
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        if (bowStack.hasNbt() && bowStack.getNbt().contains("firedShots") && bowStack.getNbt().getInt("firedShots") >= 2) {
            KrakenSlayerProjectile projectile = new KrakenSlayerProjectile(world, shooter, arrowStack);
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
    public int getPullTime() {
        return ConfigConstructor.kraken_slayer_pull_time_ticks;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_kraken_slayer_bow;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_kraken_slayer_bow;
    }

    @Override
    public int getReduceCooldownEnchantLevel(ItemStack stack) {
        return 0;
    }
}