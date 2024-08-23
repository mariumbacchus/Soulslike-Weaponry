package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
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
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.ItemRegistry;

public class HunterCannon extends GunItem {

    public HunterCannon(Settings settings) {
        super(settings);
    }

    @Override
    public int getPostureLoss(ItemStack stack) {
        int lvl = EnchantmentHelper.getLevel(EnchantRegistry.VISCERAL, stack);
        return ConfigConstructor.hunter_cannon_posture_loss * (lvl == 0 ? 1 : lvl);
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
        return ConfigConstructor.hunter_cannon_cooldown - 4 * this.getReducedCooldown(stack) + EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) * 50;
    }

    @Override
    public int bulletsNeeded() {
        return ConfigConstructor.hunter_cannon_bullets_needed;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_hunter_cannon;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.isDisabled(stack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        boolean bl = user.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
        ItemStack itemStack = user.getProjectileType(stack);
        int bulletsNeeded = this.bulletsNeeded();
        if (!itemStack.isEmpty() && itemStack.getCount() >= bulletsNeeded || bl) {
            if (itemStack.isEmpty()) {
                itemStack = new ItemStack(ItemRegistry.SILVER_BULLET.get());
            }
            boolean bl2 = bl && itemStack.isOf(ItemRegistry.SILVER_BULLET.get());
            Vec3d pov = user.getRotationVector();
            Vec3d particleBox = pov.multiply(1).add(user.getPos());
            if (world.isClient) {
                for (int k = 0; k < 50; k++) {
                    world.addParticle(ParticleTypes.FLAME, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .5, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .5);
                    world.addParticle(ParticleTypes.SMOKE, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .5, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .5);
                }
            }

            PersistentProjectileEntity entity = this.createSilverBulletEntity(world, user, stack);
            world.spawnEntity(entity);
            world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
            stack.damage(bulletsNeeded, user, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
            if (!bl2 && !user.getAbilities().creativeMode) {
                itemStack.decrement(bulletsNeeded);
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
            if (!user.isCreative()) user.getItemCooldownManager().set(this, this.getCooldown(stack));
            return TypedActionResult.success(stack, world.isClient());
        }
        return TypedActionResult.fail(stack); 
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
