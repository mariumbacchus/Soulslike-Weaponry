package net.soulsweaponry.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public abstract class ModdedBow extends BowItem {

    public ModdedBow(Properties pProperties) {
        super(pProperties);
    }

    public abstract float getReducedPullTime();
    //TODO Kan hende man må bruke mixin i methoden som henter pull time/status til buer i stedet for å bruke egen ModdedBow.getReducedPullTime pga compatibility
    public float getModdedPullProgress(int useTicks) {
        float f = (float)useTicks / (20.0f - this.getReducedPullTime());
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    public void shootProjectile(Level world, ItemStack stack, ItemStack arrowStack, Player player, float pullProgress, AbstractArrow projectile, float powerModifier, float velModifier) {
        projectile.setPos(player.getX(), player.getY() + 1.5F, player.getZ());
        projectile.setOwner(player);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, pullProgress * velModifier, 1.0F);
        if (pullProgress == 1.0F) {
            projectile.setCritArrow(true);
        }
        double power = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack); // Normal bow damage: 2.5 -> 5 (power V) -> 10 with crit (excluding velocity amp)
        projectile.setBaseDamage(projectile.getBaseDamage() + power * powerModifier);
        int punch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
        if (punch > 0) {
            projectile.setKnockback(punch);
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
            projectile.setSecondsOnFire(8);
        }
        stack.hurtAndBreak(1, player, (p_43296_) -> p_43296_.broadcastBreakEvent(player.getUsedItemHand()));

        boolean creativeAndInfinity = player.isCreative() || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
        boolean bl2 = creativeAndInfinity && arrowStack.is(Items.ARROW);
        if (bl2 || player.isCreative() && (arrowStack.is(Items.SPECTRAL_ARROW) || arrowStack.is(Items.TIPPED_ARROW))) {
            projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        world.addFreshEntity(projectile);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + pullProgress * 0.5F);
        if (!bl2 && !player.isCreative()) {
            arrowStack.shrink(1);
            if (arrowStack.isEmpty()) {
                player.getInventory().removeItem(arrowStack);
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
    }
}
