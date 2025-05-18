package net.soulsweaponry.items.gun;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.SoundRegistry;

public class GatlingGun extends GunItem {

    public GatlingGun(Settings settings) {
        super(settings);
    }

    @Override
    public int getPostureLoss(ItemStack stack) {
        int lvl = EnchantmentHelper.getLevel(EnchantRegistry.VISCERAL, stack);
        return (int) (ConfigConstructor.gatling_gun_posture_loss + lvl * ConfigConstructor.gatling_gun_posture_loss_per_enchant_level);
    }

    @Override
    public float getBulletDamage(ItemStack stack) {
        return ConfigConstructor.gatling_gun_damage;
    }

    @Override
    public float getBulletVelocity(ItemStack stack) {
        return ConfigConstructor.gatling_gun_velocity;
    }

    @Override
    public float getBulletDivergence(ItemStack stack) {
        return ConfigConstructor.gatling_gun_divergence;
    }

    @Override
    public int getCooldown(ItemStack stack) {
        return (int) (ConfigConstructor.gatling_gun_cooldown - 3 * this.getReducedCooldown(stack) + EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) * 30);
    }

    @Override
    public int getBulletsNeeded() {
        return (int) ConfigConstructor.gatling_gun_bullets_needed;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_gatling_gun;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks < this.getMaxUseTime(stack) - 15 && remainingUseTicks % 4 == 0) {
            if (user instanceof PlayerEntity playerEntity) {
                ItemStack itemStack = this.canShoot(playerEntity, stack);
                if (itemStack != null) {
                    PersistentProjectileEntity entity = this.createSilverBulletEntity(world, user, stack);
                    world.spawnEntity(entity);
                    this.spawnShotParticles(world, playerEntity, 2 + EnchantmentHelper.getLevel(EnchantRegistry.FAST_HANDS, stack), 0.15f);
                    world.playSound(playerEntity, user.getBlockPos(), SoundRegistry.GATLING_GUN_BARRAGE_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        } else if (-remainingUseTicks > this.getMaxUseTime(stack)) {
            user.stopUsingItem();
            super.usageTick(world, user, stack, remainingUseTicks);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        this.stop(user, stack, world);
        return super.finishUsing(stack, world, user);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        int lvl = EnchantmentHelper.getLevel(EnchantRegistry.FAST_HANDS, stack);
        return (int) (ConfigConstructor.gatling_gun_max_time * (lvl == 0 ? 1 : lvl));
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        this.stop(user, stack, world);
    }

    private void stop(LivingEntity user, ItemStack stack, World world) {
        world.playSound(null, user.getBlockPos(), SoundRegistry.GATLING_GUN_STOP_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        if (user instanceof PlayerEntity player && !player.isCreative()) {
            player.getItemCooldownManager().set(this, this.getCooldown(stack));
            stack.damage(5, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this.isDisabled(user.getStackInHand(hand))) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(user, user.getBlockPos(), SoundRegistry.GATLING_GUN_STARTUP_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        }
        else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_gatling_gun;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
}