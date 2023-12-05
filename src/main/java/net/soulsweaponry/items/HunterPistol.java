package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.ItemRegistry;

public class HunterPistol extends GunItem {

    public HunterPistol(Settings settings) {
        super(settings);
    }

    @Override
    public int getPostureLoss(ItemStack stack) {
        int lvl = EnchantmentHelper.getLevel(EnchantRegistry.VISCERAL, stack);
        return ConfigConstructor.hunter_pistol_posture_loss + lvl * 3;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return ConfigConstructor.hunter_pistol_damage + EnchantmentHelper.getLevel(Enchantments.POWER, stack) / 2;
    }

    @Override
    public int getCooldown(ItemStack stack) {
        return ConfigConstructor.hunter_pistol_cooldown - this.getReducedCooldown(stack);
    }

    @Override
    public int bulletsNeeded() {
        return 1;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        boolean bl = user.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
        ItemStack itemStack = user.getArrowType(stack);
        if (!itemStack.isEmpty() || bl) {
            if (itemStack.isEmpty()) {
                itemStack = new ItemStack(ItemRegistry.SILVER_BULLET);
            }
            
            boolean bl2 = bl && itemStack.isOf(ItemRegistry.SILVER_BULLET);
            int power = this.getDamage(stack);
            int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
            Vec3d pov = user.getRotationVector();
            Vec3d particleBox = pov.multiply(1).add(user.getPos());

            if (world.isClient) {
                for (int k = 0; k < 10; k++) {
                    world.addParticle(ParticleTypes.FLAME, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .25, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .25);
                    world.addParticle(ParticleTypes.SMOKE, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .25, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .25);
                }
            }

            SilverBulletEntity entity = new SilverBulletEntity(world, user);
            entity.setPos(user.getX(), user.getEyeY(), user.getZ());
            entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 3.0F, 1.0F);
            entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            entity.setPostureLoss(this.getPostureLoss(stack));
            entity.setDamage(power);
            if (punch > 0) {
                entity.setPunch(punch);
            }
            if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                entity.setOnFireFor(8);
            }
            world.spawnEntity(entity);
            world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f,1f);
            stack.damage(1, user, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
            if (!bl2 && !user.getAbilities().creativeMode) {
                itemStack.decrement(this.bulletsNeeded());
                if (itemStack.isEmpty()) {
                    user.getInventory().removeOne(itemStack);
                }
            }

            user.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!user.isCreative()) user.getItemCooldownManager().set(this, this.getCooldown(stack));
            return TypedActionResult.success(stack, world.isClient());
        }
        return TypedActionResult.fail(stack); 
    }
}
