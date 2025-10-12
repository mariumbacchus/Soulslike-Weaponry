package net.soulsweaponry.items.bow;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.arrow.TrueDamageArrow;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.function.Supplier;

public class KrakenSlayer extends ModdedBow {

    public KrakenSlayer(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, createConfig((int) ConfigConstructor.kraken_slayer_pull_time_ticks,
                ConfigConstructor.kraken_slayer_damage, ConfigConstructor.kraken_slayer_bonus_velocity),
                repairIngredientSupplier);
        this.addTooltipAbility(TooltipAbilities.FAST_PULL, TooltipAbilities.THIRD_SHOT);
    }

    @Override
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        return getKrakenSlayerProjectile(world, bowStack, shooter, originalArrow.getDamage(), ConfigConstructor.kraken_slayer_bonus_true_damage + WeaponUtil.getLevel(bowStack, Enchantments.POWER));
    }

    public static PersistentProjectileEntity getKrakenSlayerProjectile(World world, ItemStack bowStack, LivingEntity shooter, double damage, float trueDamage) {
        Integer firedShots = bowStack.get(ComponentRegistry.AMOUNT_USED);
        if (firedShots != null) {
            if (firedShots >= 2) {
                TrueDamageArrow projectile = new TrueDamageArrow(world, shooter, Items.ARROW.getDefaultStack(), bowStack);
                projectile.setTrueDamage(trueDamage);
                projectile.setDamage(damage);
                bowStack.set(ComponentRegistry.AMOUNT_USED, 0);
                return projectile;
            } else {
                bowStack.set(ComponentRegistry.AMOUNT_USED, firedShots + 1);
            }
        } else {
            bowStack.set(ComponentRegistry.AMOUNT_USED, 1);
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
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return null;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        for (UseAction action : UseAction.values()) {
            if (action.toString().equals(ConfigConstructor.kraken_slayer_bow_use_animation)) {
                return action;
            }
        }
        return UseAction.SPEAR;
    }
}