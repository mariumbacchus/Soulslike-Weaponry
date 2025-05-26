package net.soulsweaponry.items.gun;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.Cannonball;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class HunterCannon extends GunItem {

    public HunterCannon(Settings settings) {
        super(settings);
    }

    @Override
    public int getPostureLoss(ItemStack stack) {
        int lvl = EnchantmentHelper.getLevel(EnchantRegistry.VISCERAL, stack);
        return (int) (ConfigConstructor.hunter_cannon_posture_loss + lvl * ConfigConstructor.hunter_cannon_posture_loss_per_enchant_level);
    }

    @Override
    public float getBulletDamage(ItemStack stack) {
        return ConfigConstructor.hunter_cannon_damage;
    }

    @Override
    public float getBulletVelocity(ItemStack stack) {
        return ConfigConstructor.hunter_cannon_velocity;
    }

    @Override
    public float getBulletDivergence(ItemStack stack) {
        return ConfigConstructor.hunter_cannon_divergence;
    }

    @Override
    public int getCooldown(ItemStack stack) {
        return (int) (ConfigConstructor.hunter_cannon_cooldown - 4 * this.getReducedCooldown(stack) + EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) * 50);
    }

    @Override
    public int getBulletsNeeded(ItemStack stack) {
        return EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0 ? super.getBulletsNeeded(stack) : (int) ConfigConstructor.hunter_cannon_bullets_needed;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_hunter_cannon;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this.isDisabled(user.getStackInHand(hand))) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        ItemStack stack = user.getStackInHand(hand);
        ItemStack itemStack = this.canShoot(user, stack);
        if (itemStack != null) {
            this.spawnShotParticles(world, user, 50, 0.4f);
            PersistentProjectileEntity entity = this.createSilverBulletEntity(world, user, stack);
            world.spawnEntity(entity);
            WeaponUtil.launchTarget(user, 2f, true);
            this.postShot(world, user, stack);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public int getStackDamageToApply() {
        return this.getBulletsNeeded(this.getDefaultStack());
    }

    @Override
    public int getProjectileMaxAge() {
        return 120;
    }

    @Override
    public int getProjectileMaxAgeEthereal() {
        return 60;
    }

    @Override
    public SilverBulletEntity getModdedProjectile(World world, LivingEntity shooter, ItemStack gunStack) {
        return new Cannonball(world, shooter);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_hunter_cannon;
    }
}