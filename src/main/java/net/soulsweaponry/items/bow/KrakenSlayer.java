package net.soulsweaponry.items.bow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.projectile_damage.api.IProjectileWeapon;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.arrow.TrueDamageArrow;
import net.soulsweaponry.items.ModdedBow;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

public class KrakenSlayer extends ModdedBow {

    public KrakenSlayer(Settings settings) {
        super(settings);
        this.addTooltipAbility(TooltipAbilities.FAST_PULL, TooltipAbilities.THIRD_SHOT);
        ((IProjectileWeapon)this).setProjectileDamage(ConfigConstructor.kraken_slayer_damage);
        ((IProjectileWeapon)this).setCustomLaunchVelocity((double) ConfigConstructor.kraken_slayer_max_velocity);
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_kraken_slayer_bow;
    }

    @Override
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        if (bowStack.hasNbt() && bowStack.getNbt().contains("firedShots") && bowStack.getNbt().getInt("firedShots") >= 2) {
            TrueDamageArrow projectile = new TrueDamageArrow(world, shooter);
            projectile.setTrueDamage(ConfigConstructor.kraken_slayer_bonus_true_damage + EnchantmentHelper.getLevel(Enchantments.POWER, bowStack));
            bowStack.getNbt().putInt("firedShots", 0);
            return projectile;
        } else {
            if (bowStack.hasNbt() && bowStack.getNbt().contains("firedShots")) {
                bowStack.getNbt().putInt("firedShots", bowStack.getNbt().getInt("firedShots") + 1);
            } else {
                bowStack.getOrCreateNbt().putInt("firedShots", 1);
            }
        }
        return null;
    }

    @Override
    public int getPullTime() {
        return (int) ConfigConstructor.kraken_slayer_pull_time_ticks;
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
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return null;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if (WeaponUtil.isModLoaded("epicfight")) {
            return UseAction.BOW;
        }
        for (UseAction action : UseAction.values()) {
            if (action.toString().equals(ConfigConstructor.kraken_slayer_bow_use_animation)) {
                return action;
            }
        }
        return UseAction.SPEAR;
    }
}