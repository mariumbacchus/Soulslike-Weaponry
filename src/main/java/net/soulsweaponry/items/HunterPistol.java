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
    public float getBulletDamage(ItemStack stack) {
        return ConfigConstructor.hunter_pistol_damage;
    }

    @Override
    public float getBulletVelocity(ItemStack stack) {
        return ConfigConstructor.hunter_pistol_velocity;
    }

    @Override
    public float getBulletDivergence(ItemStack stack) {
        return ConfigConstructor.hunter_pistol_divergence;
    }

    @Override
    public int getCooldown(ItemStack stack) {
        return ConfigConstructor.hunter_pistol_cooldown - this.getReducedCooldown(stack);
    }

    @Override
    public int bulletsNeeded() {
        return ConfigConstructor.hunter_pistol_bullets_needed;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_hunter_pistol;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this.isDisabled(user.getStackInHand(hand))) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        ItemStack stack = user.getStackInHand(hand);
        boolean bl = user.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
        ItemStack itemStack = user.getProjectileType(stack);
        if (!itemStack.isEmpty() || bl) {
            if (itemStack.isEmpty()) {
                itemStack = new ItemStack(ItemRegistry.SILVER_BULLET);
            }
            boolean bl2 = bl && itemStack.isOf(ItemRegistry.SILVER_BULLET);
            Vec3d pov = user.getRotationVector();
            Vec3d particleBox = pov.multiply(1).add(user.getPos());
            if (world.isClient) {
                for (int k = 0; k < 10; k++) {
                    world.addParticle(ParticleTypes.FLAME, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .25, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .25);
                    world.addParticle(ParticleTypes.SMOKE, true, particleBox.x, particleBox.y + 1.5F, particleBox.z, pov.x + user.getRandom().nextDouble() - .25, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .25);
                }
            }

            PersistentProjectileEntity entity = this.createSilverBulletEntity(world, user, stack);
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

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_hunter_pistol;
    }
}