package net.soulsweaponry.items.bow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.projectile_damage.api.IProjectileWeapon;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.arrow.SilverArrow;
import net.soulsweaponry.items.IPostureLossItem;
import net.soulsweaponry.items.IUndeadBonus;
import net.soulsweaponry.items.ModdedBow;
import net.soulsweaponry.util.TooltipAbilities;

public class SimonsBowblade extends ModdedBow implements IUndeadBonus, IPostureLossItem {

    public SimonsBowblade(Settings settings) {
        super(settings);
        this.addTooltipAbility(TooltipAbilities.RIGHTEOUS, TooltipAbilities.PROJECTILE_POSTURE_LOSS, TooltipAbilities.SLOW_PULL);
        ((IProjectileWeapon)this).setProjectileDamage(ConfigConstructor.simons_bowblade_projectile_damage);
        ((IProjectileWeapon)this).setCustomLaunchVelocity((double) ConfigConstructor.simons_bowblade_max_velocity);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_simons_bowblade;
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
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        SilverArrow arrow = new SilverArrow(shooter, world);
        arrow.setBonusUndeadDamage(this.getUndeadBonus(bowStack) + EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, bowStack));
        arrow.setPostureLoss(this.getPostureLoss());
        return arrow;
    }

    @Override
    public int getPullTime() {
        return ConfigConstructor.simons_bowblade_pull_time_ticks;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_simons_bowblade;
    }

    @Override
    public boolean isRighteous() {
        return true;
    }

    @Override
    public float getUndeadBonus(ItemStack stack) {
        return ConfigConstructor.simons_bowblade_projectile_righteous_undead_bonus_damage;
    }

    @Override
    public int getPostureLoss() {
        return ConfigConstructor.simons_bowblade_projectile_posture_loss;
    }
}