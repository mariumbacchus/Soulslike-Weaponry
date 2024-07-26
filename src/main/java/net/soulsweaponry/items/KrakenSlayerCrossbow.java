package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.entity.projectile.KrakenSlayerProjectile;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class KrakenSlayerCrossbow extends ModdedCrossbow {

    public KrakenSlayerCrossbow(Settings settings) {
        super(settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.FAST_PULL, WeaponUtil.TooltipAbilities.THIRD_SHOT);
    }

    @Override
    public void shootProjectiles(World world, LivingEntity entity, Hand hand, ItemStack stack, float speed, float divergence) {
        if (this.isEmpowered(stack)) {
            List<ItemStack> list = this.getProjectiles(stack);
            float[] soundPitches = this.getSoundPitches(entity.getRandom());
            boolean bl = entity instanceof PlayerEntity && ((PlayerEntity)entity).getAbilities().creativeMode;
            for (int i = 0; i < list.size(); i++) {
                ItemStack arrowStack = list.get(i);
                if (arrowStack.isEmpty()) continue;
                if (i == 0) {
                    KrakenSlayerProjectile projectile = new KrakenSlayerProjectile(world, entity);
                    this.shoot(world, entity, hand, stack, projectile, arrowStack, soundPitches[i], bl, speed, divergence, 0.0f);
                    continue;
                }
                if (i == 1) {
                    KrakenSlayerProjectile projectile = new KrakenSlayerProjectile(world, entity);
                    this.shoot(world, entity, hand, stack, projectile, arrowStack, soundPitches[i], bl, speed, divergence, -10.0f);
                    continue;
                }
                if (i != 2) continue;
                KrakenSlayerProjectile projectile = new KrakenSlayerProjectile(world, entity);
                this.shoot(world, entity, hand, stack, projectile, arrowStack, soundPitches[i], bl, speed, divergence, 10.0f);
            }
            this.postShoot(world, entity, stack);
        } else {
            super.shootProjectiles(world, entity, hand, stack, speed, divergence);
        }
    }

    public boolean isEmpowered(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains("firedShots") && stack.getNbt().getInt("firedShots") >= 2) {
            stack.getNbt().putInt("firedShots", 0);
            return true;
        } else {
            if (stack.hasNbt()) {
                if (stack.getNbt().contains("firedShots")) {
                    stack.getNbt().putInt("firedShots", stack.getNbt().getInt("firedShots") + 1);
                } else {
                    stack.getNbt().putInt("firedShots", 1);
                }
            }
            return false;
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
            kraken.setTrueDamage(CommonConfig.KRAKEN_SLAYER_BONUS_DAMAGE.get());
        }
        projectile.setDamage(projectile.getDamage() + bonus);
    }

    @Override
    public float getReducedPullTime() {
        return CommonConfig.KRAKEN_SLAYER_REDUCED_PULL_TIME.get();
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return CommonConfig.DISABLE_USE_KRAKEN_SLAYER_CROSSBOW.get();
    }
}