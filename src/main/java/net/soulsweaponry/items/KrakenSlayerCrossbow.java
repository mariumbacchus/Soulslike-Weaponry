package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.KrakenSlayerProjectile;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KrakenSlayerCrossbow extends ModdedCrossbow {

    public KrakenSlayerCrossbow(Settings settings) {
        super(settings);
    }

    @Override
    public EntityType<? extends PersistentProjectileEntity> getProjectileType(ItemStack stack, PlayerEntity player, World world) {
        if (stack.hasNbt() && stack.getNbt().contains("firedShots") && stack.getNbt().getInt("firedShots") >= 2) {
            stack.getNbt().putInt("firedShots", 0);
            return EntityRegistry.KRAKEN_SLAYER_PROJECTILE;
        } else {
            if (stack.hasNbt()) {
                if (stack.getNbt().contains("firedShots")) {
                    stack.getNbt().putInt("firedShots", stack.getNbt().getInt("firedShots") + 1);
                } else {
                    stack.getNbt().putInt("firedShots", 1);
                }
            }
            return null;
        }
    }

    @Override
    public float getSpeed(ItemStack stack, PlayerEntity player) {
        return 3.15f;
    }

    @Override
    public float getDivergence(ItemStack stack, PlayerEntity player) {
        return 1f;
    }

    @Override
    public void modifyProjectile(PersistentProjectileEntity projectile, ItemStack stack) {
        float bonus =  EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack) / 4f;
        if (projectile instanceof KrakenSlayerProjectile kraken) {
            kraken.setTrueDamage(ConfigConstructor.kraken_slayer_bonus_true_damage);
        }
        projectile.setDamage(projectile.getDamage() + bonus);
    }

    @Override
    public float getReducedPullTime() {
        return ConfigConstructor.kraken_slayer_reduced_pull_time;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.FAST_PULL, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.THIRD_SHOT, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}