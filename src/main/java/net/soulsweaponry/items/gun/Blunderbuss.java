package net.soulsweaponry.items.gun;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EnchantRegistry;

public class Blunderbuss extends GunItem {

    public Blunderbuss(Settings settings) {
        super(settings);
    }

    @Override
    public int getPostureLoss(ItemStack stack) {
        int lvl = EnchantmentHelper.getLevel(EnchantRegistry.VISCERAL, stack);
        return (int) (ConfigConstructor.blunderbuss_posture_loss + lvl * ConfigConstructor.blunderbuss_posture_loss_per_enchant_level);
    }

    @Override
    public float getBulletDamage(ItemStack stack) {
        return ConfigConstructor.blunderbuss_damage;
    }

    @Override
    public float getBulletVelocity(ItemStack stack) {
        return ConfigConstructor.blunderbuss_velocity;
    }

    @Override
    public float getBulletDivergence(ItemStack stack) {
        return ConfigConstructor.blunderbuss_divergence;
    }

    @Override
    public int getCooldown(ItemStack stack) {
        return (int) (ConfigConstructor.blunderbuss_cooldown - this.getReducedCooldown(stack));
    }

    @Override
    public int getBulletsNeeded(ItemStack stack) {
        return EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0 ? super.getBulletsNeeded(stack) : (int) ConfigConstructor.blunderbuss_bullets_needed;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_blunderbuss;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.isDisabled(stack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        ItemStack itemStack = this.canShoot(user, stack);
        if (itemStack != null) {
            int projectileCount = (int) (ConfigConstructor.blunderbuss_projectile_amount + EnchantmentHelper.getLevel(Enchantments.POWER, stack) / 2f);
            for (int i = 0; i < projectileCount; i++) {
                PersistentProjectileEntity entity = this.createSilverBulletEntity(world, user, stack);
                world.spawnEntity(entity);
            }
            this.spawnShotParticles(world, user, 50, 0.2f);
            this.postShot(world, user, stack);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_hunter_blunderbuss;
    }
}