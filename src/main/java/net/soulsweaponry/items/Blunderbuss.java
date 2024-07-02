package net.soulsweaponry.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Blunderbuss extends GunItem {

    public Blunderbuss(Settings settings) {
        super(settings);
    }

    @Override
    public int getPostureLoss(ItemStack stack) {
        int lvl = EnchantmentHelper.getLevel(EnchantRegistry.VISCERAL, stack);
        return ConfigConstructor.blunderbuss_posture_loss + lvl * 2;
    }

    @Override
    public int getBulletDamage(ItemStack stack) {
        return ConfigConstructor.blunderbuss_damage + EnchantmentHelper.getLevel(Enchantments.POWER, stack) / 2;
    }

    @Override
    public int getCooldown(ItemStack stack) {
        return ConfigConstructor.blunderbuss_cooldown - this.getReducedCooldown(stack);
    }

    @Override
    public int bulletsNeeded() {
        return 2;
    }
    
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (ConfigConstructor.disable_use_hunter_blunderbuss) {
            if (ConfigConstructor.inform_player_about_disabled_use){
                user.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.useDisabled","This item is disabled"));
            }
            return TypedActionResult.fail(stack);
        }
        boolean bl = user.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
        ItemStack itemStack = user.getProjectileType(stack);
        if (!itemStack.isEmpty() || bl) {
            if (itemStack.isEmpty()) {
                itemStack = new ItemStack(ItemRegistry.SILVER_BULLET);
            }
            boolean bl2 = bl && itemStack.isOf(ItemRegistry.SILVER_BULLET);
            int power = EnchantmentHelper.getLevel(Enchantments.POWER, stack) / 2;
            int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
            Vec3d pov = user.getRotationVector();
            Vec3d particleBox = pov.multiply(1).add(user.getPos());
            
            for (int i = 0; i < 3 + power; i++) {
                SilverBulletEntity entity = new SilverBulletEntity(world, user, itemStack);
                entity.setPos(user.getX(), user.getEyeY(), user.getZ());
                entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 3.0F, 10.0F);
                entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                entity.setPostureLoss(this.getPostureLoss(stack));
                entity.setDamage(this.getBulletDamage(stack));
                if (punch > 0) {
                    entity.setPunch(punch);
                }
                if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                    entity.setOnFireFor(8);
                }
                world.spawnEntity(entity);
            }
            
            if (world.isClient) {
                for (int k = 0; k < 50; k++) {
                    world.addParticle(ParticleTypes.FLAME, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .5, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .5);
                    world.addParticle(ParticleTypes.SMOKE, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .5, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .5);
                }
            }
            world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
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

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (ConfigConstructor.disable_use_hunter_blunderbuss) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
