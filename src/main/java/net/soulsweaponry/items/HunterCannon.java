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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.Cannonball;
import net.soulsweaponry.registry.ItemRegistry;

public class HunterCannon extends GunItem {

    private final int bulletsNeeded = 10;

    public HunterCannon(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        boolean bl = user.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
        ItemStack itemStack = user.getArrowType(stack);
        if (!itemStack.isEmpty() && itemStack.getCount() >= this.bulletsNeeded || bl) {
            if (itemStack.isEmpty()) {
                itemStack = new ItemStack(ItemRegistry.SILVER_BULLET);
            }
            
            boolean bl2 = bl && itemStack.isOf(ItemRegistry.SILVER_BULLET);
            int power = ConfigConstructor.hunter_cannon_damage + EnchantmentHelper.getLevel(Enchantments.POWER, stack);
            int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
            Vec3d pov = user.getRotationVector();
            Vec3d particleBox = pov.multiply(1).add(user.getPos());

            if (world.isClient) {
                for (int k = 0; k < 50; k++) {
                    world.addParticle(ParticleTypes.FLAME, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .5, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .5);
                    world.addParticle(ParticleTypes.SMOKE, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .5, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .5);
                }
            }

            Cannonball entity = new Cannonball(world, user);
            entity.setPos(user.getX(), user.getEyeY(), user.getZ());
            entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 3.0F, 1.0F);
            entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;

            entity.setDamage(power);
            if (punch > 0) {
                entity.setPunch(punch);
            }
            if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                entity.setOnFireFor(8);
            }
            world.spawnEntity(entity);
            
            world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);

            stack.damage(this.bulletsNeeded, user, (p_220045_0_) -> {
                p_220045_0_.sendToolBreakStatus(user.getActiveHand());
            });
            //world.playSound((PlayerEntity)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
            if (!bl2 && !user.getAbilities().creativeMode) {
                itemStack.decrement(this.bulletsNeeded);
                if (itemStack.isEmpty()) {
                    user.getInventory().removeOne(itemStack);
                }
            }

            float f = user.getYaw();
            float g = user.getPitch();
            float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
            float k = -MathHelper.sin(g * 0.017453292F);
            float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
            float m = MathHelper.sqrt(h * h + k * k + l * l);
            float n = 2.0F * ((1.0F + 1F) / 4.0F);
            h *= n / m;
            k *= n / m;
            l *= n / m;
            user.addVelocity(-(double)h, -(double)k, -(double)l);

            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.getItemCooldownManager().set(this, ConfigConstructor.hunter_cannon_cooldown- 4*this.getReducedCooldown(stack) + EnchantmentHelper.getLevel(Enchantments.INFINITY, stack)*50);
            return TypedActionResult.success(stack, world.isClient());
        }
        return TypedActionResult.fail(stack); 
    }
}
