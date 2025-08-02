package net.soulsweaponry.items.bow;

import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.arrow.SilverArrow;
import net.soulsweaponry.items.IPostureLossItem;
import net.soulsweaponry.items.IUndeadBonus;
import net.soulsweaponry.items.ModdedBow;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.function.Supplier;

public class SimonsBowblade extends ModdedBow implements IUndeadBonus, IPostureLossItem {

    public SimonsBowblade(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, new RangedConfig((int) ConfigConstructor.simons_bowblade_pull_time_ticks,
                ConfigConstructor.simons_bowblade_projectile_damage, ConfigConstructor.simons_bowblade_max_velocity),
                repairIngredientSupplier);
        this.addTooltipAbility(TooltipAbilities.RIGHTEOUS, TooltipAbilities.PROJECTILE_POSTURE_LOSS, TooltipAbilities.SLOW_PULL);
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
        arrow.setBonusUndeadDamage(this.getUndeadBonus(bowStack) + WeaponUtil.getLevel(bowStack, Enchantments.FIRE_ASPECT));
        arrow.setPostureLoss(this.getPostureLoss());
        return arrow;
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return this.getUndeadBonusAttackDamage(target, baseAttackDamage, damageSource);
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
        return (int) ConfigConstructor.simons_bowblade_projectile_posture_loss;
    }
}
