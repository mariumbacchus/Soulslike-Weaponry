package net.soulsweaponry.items.bow;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.arrow.SilverArrow;
import net.soulsweaponry.items.IPostureLossItem;
import net.soulsweaponry.items.ModdedBow;
import net.soulsweaponry.items.abilities.bonusdamage.UndeadBonus;
import net.soulsweaponry.util.TooltipAbilities;

import java.util.function.Supplier;

public class SimonsBowblade extends ModdedBow implements IPostureLossItem {

    public SimonsBowblade(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, createConfig((int) ConfigConstructor.simons_bowblade_pull_time_ticks,
                ConfigConstructor.simons_bowblade_projectile_damage, ConfigConstructor.simons_bowblade_bonus_velocity),
                repairIngredientSupplier);
        UndeadBonus undeadBonus = new UndeadBonus(ConfigConstructor.simons_bowblade_projectile_righteous_base_undead_bonus_damage, ConfigConstructor.simons_bowblade_projectile_righteous_undead_bonus_damage_per_level);
        this.addTooltipAbility(TooltipAbilities.PROJECTILE_POSTURE_LOSS, TooltipAbilities.SLOW_PULL);
        //this.addAbility(undeadBonus); TODO make bows and crossbows implement IHasAbilities
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_simons_bowblade;
    }

    @Override
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        SilverArrow arrow = new SilverArrow(shooter, world, arrowStack, bowStack);
        //arrow.setBonusUndeadDamage(this.getUndeadBonus(bowStack) + WeaponUtil.getLevel(bowStack, Enchantments.FIRE_ASPECT));TODO implement when bows implement IHasAbilities
        arrow.setPostureLoss(this.getPostureLoss());
        return arrow;
    }

    @Override
    public int getPostureLoss() {
        return (int) ConfigConstructor.simons_bowblade_projectile_posture_loss;
    }
}
