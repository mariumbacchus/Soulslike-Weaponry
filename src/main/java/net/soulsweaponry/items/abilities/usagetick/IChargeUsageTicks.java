package net.soulsweaponry.items.abilities.usagetick;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.stoppedusing.IChargeToUse;
import net.soulsweaponry.util.WeaponUtil;

public interface IChargeUsageTicks extends IChargeToUse {

    @Override
    void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks);

    default void stop(LivingEntity user, ItemStack stack, int ticksUsed) {
        if (user instanceof PlayerEntity player) {
            this.applyItemCooldown(stack.getItem(), player, this.getCooldown(stack, ticksUsed));
            stack.damage(3, user, WeaponUtil.getActiveHandSlot(player));
        }
    }

    @Override
    default ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        this.stop(user, stack, 0);
        return stack;
    }

    @Override
    default void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        this.stop(user, stack, WeaponUtil.getChargeTime(stack, remainingUseTicks));
    }

    int getCooldown(ItemStack stack, int ticksUsed);

    @Override
    int getMaxUseTime(ItemStack stack);

    @Override
    default int useActionPriority() {
        return 100;
    }

    @Override
    UseAction getUseAction();
}
