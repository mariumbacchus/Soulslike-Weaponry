package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

public abstract class ModdedBow extends BowItem implements IReducedPullTime {

    public ModdedBow(Settings settings) {
        super(settings);
    }

    public abstract float getReducedPullTime();

    public float getModdedPullProgress(int useTicks) {
        float f = (float)useTicks / (20.0f - this.getReducedPullTime());
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    public void shootProjectile(World world, ItemStack stack, ItemStack arrowStack, PlayerEntity player, float pullProgress, PersistentProjectileEntity projectile, float powerModifier, float velModifier) {
        projectile.setPos(player.getX(), player.getY() + 1.5F, player.getZ());
        projectile.setOwner(player);
        projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, pullProgress * velModifier, 1.0F);
        if (pullProgress == 1.0F) {
            projectile.setCritical(true);
        }
        double power = EnchantmentHelper.getLevel(Enchantments.POWER, stack); // Normal bow damage: 2.5 -> 5 (power V) -> 10 with crit (excluding velocity amp)
        projectile.setDamage(projectile.getDamage() + power * powerModifier);
        int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
        if (punch > 0) {
            projectile.setPunch(punch);
        }
        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
            projectile.setOnFireFor(8);
        }
        stack.damage(1, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));

        boolean creativeAndInfinity = player.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
        boolean bl2 = creativeAndInfinity && arrowStack.isOf(Items.ARROW);
        if (bl2 || player.getAbilities().creativeMode && (arrowStack.isOf(Items.SPECTRAL_ARROW) || arrowStack.isOf(Items.TIPPED_ARROW))) {
            projectile.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }
        world.spawnEntity(projectile);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + pullProgress * 0.5F);
        if (!bl2 && !player.getAbilities().creativeMode) {
            arrowStack.decrement(1);
            if (arrowStack.isEmpty()) {
                player.getInventory().removeOne(arrowStack);
            }
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }
}