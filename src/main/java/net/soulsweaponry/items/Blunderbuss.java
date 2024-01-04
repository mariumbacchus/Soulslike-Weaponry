package net.soulsweaponry.items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EnchantmentRegistry;
import net.soulsweaponry.registry.ItemRegistry;

public class Blunderbuss extends GunItem {

    public Blunderbuss(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getPostureLoss(ItemStack stack) {
        int lvl = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.VISCERAL, stack);
        return CommonConfig.BLUNDERBUSS_POSTURE_LOSS.get() + lvl * 2;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return CommonConfig.BLUNDERBUSS_DAMAGE.get() + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack) / 2;
    }

    @Override
    public int getCooldown(ItemStack stack) {
        return CommonConfig.BLUNDERBUSS_COOLDOWN.get() - this.getReducedCooldown(stack);
    }

    @Override
    public int bulletsNeeded() {
        return 2;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        boolean bl = user.isCreative() || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
        ItemStack itemStack = user.getProjectile(stack);
        if (!itemStack.isEmpty() || bl) {
            if (itemStack.isEmpty()) {
                itemStack = new ItemStack(ItemRegistry.SILVER_BULLET.get());
            }
            boolean bl2 = bl && itemStack.is(ItemRegistry.SILVER_BULLET.get());
            int power = this.getDamage(stack);
            int punch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
            Vec3 pov = user.getViewVector(1f);
            Vec3 particleBox = pov.scale(1).add(user.position());
            for (int i = 0; i < 3 + power; i++) {
                /*SilverBulletEntity entity = new SilverBulletEntity(world, user);
                entity.setPos(user.getX(), user.getEyeY(), user.getZ());
                entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 3.0F, 10.0F);
                entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                entity.setPostureLoss(this.getPostureLoss(stack));
                entity.setDamage(power);
                if (punch > 0) {
                    entity.setPunch(punch);
                }
                if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                    entity.setOnFireFor(8);
                }
                world.spawnEntity(entity);*/ //TODO add SilverBulletEntity
            }
            if (world.isClientSide) {
                for (int k = 0; k < 50; k++) {
                    world.addParticle(ParticleTypes.FLAME, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .5, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .5);
                    world.addParticle(ParticleTypes.SMOKE, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .5, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .5);
                }
            }
            world.playSound(user, user.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1f, 1f);
            stack.hurtAndBreak(1, user, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
            if (!bl2 && !user.isCreative()) {
                itemStack.shrink(this.bulletsNeeded());
                if (itemStack.isEmpty()) {
                    user.getInventory().removeItem(itemStack);
                }
            }
            user.awardStat(Stats.ITEM_USED.get(this));
            if (!user.isCreative()) user.getCooldowns().addCooldown(this, this.getCooldown(stack));
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }
}
