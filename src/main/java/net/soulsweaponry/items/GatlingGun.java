package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;

public class GatlingGun extends GunItem {
    private int damageAmount;

    public GatlingGun(Settings settings) {
        super(settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks < this.getMaxUseTime(stack) - this.startupCooldownVariable(stack) && remainingUseTicks % (8 - EnchantmentHelper.getLevel(EnchantRegistry.FAST_HANDS, stack)) == 0) {
            if (user instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity)user;
                boolean bl = playerEntity.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
                ItemStack itemStack = playerEntity.getProjectileType(stack);
                if (!itemStack.isEmpty() || bl) {
                    if (itemStack.isEmpty()) {
                        itemStack = new ItemStack(ItemRegistry.SILVER_BULLET);
                    }
                    
                    boolean bl2 = bl && itemStack.isOf(ItemRegistry.SILVER_BULLET);
                    int power = ConfigConstructor.gatling_gun_damage + EnchantmentHelper.getLevel(Enchantments.POWER, stack)/2;
                    int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                    Vec3d pov = playerEntity.getRotationVector();
                    Vec3d particleBox = pov.multiply(1).add(playerEntity.getPos());
                    if (world.isClient) {
                        for (int k = 0; k < 8 + EnchantmentHelper.getLevel(EnchantRegistry.FAST_HANDS, stack); k++) {
                            world.addParticle(ParticleTypes.FLAME, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .25, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .25);
                            world.addParticle(ParticleTypes.SMOKE, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .25, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .25);
                        }
                    }
    
                    SilverBulletEntity entity = new SilverBulletEntity(world, playerEntity);
                    entity.setPos(user.getX(), user.getEyeY(), user.getZ());
                    entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 3.0F, 3.0F);
                    entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
    
                    entity.setDamage(power);
                    if (punch > 0) {
                        entity.setPunch(punch);
                    }
                    if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                        entity.setOnFireFor(8);
                    }
                    world.spawnEntity(entity);
                    world.playSound(playerEntity, user.getBlockPos(), SoundRegistry.GATLING_GUN_BARRAGE_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                    this.damageAmount++;
                    /* stack.damage(1, user, (p_220045_0_) -> {
                        p_220045_0_.sendToolBreakStatus(playerEntity.getActiveHand());;
                    }); */

                    if (!bl2 && !playerEntity.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            playerEntity.getInventory().removeOne(itemStack);
                        }
                    }
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }
    
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 7200;
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        world.playSound(null, user.getBlockPos(), SoundRegistry.GATLING_GUN_STOP_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        if (user instanceof PlayerEntity) {
            stack.damage(this.damageAmount, (PlayerEntity)user, (p_220045_0_) -> {
                p_220045_0_.sendToolBreakStatus(((PlayerEntity)user).getActiveHand());;
            });
        }
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        this.damageAmount = 0;
        world.playSound(user, user.getBlockPos(), SoundRegistry.GATLING_GUN_STARTUP_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        } 
         else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    private int startupCooldownVariable(ItemStack itemStack) {
        return ConfigConstructor.gatling_gun_startup_time - 2*this.getReducedCooldown(itemStack);
    }
}
