package net.soulsweaponry.items.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.soulsweaponry.util.NbtHelper;
import net.soulsweaponry.util.NbtIds;

public interface ISharpened extends IAbility {

    static boolean isEmpowered(ItemStack stack) {
        return getEmpoweredAttacks(stack) > 0;
    }

    static int getEmpoweredAttacks(ItemStack stack) {
        return NbtHelper.getInt(stack, NbtIds.SHARPENED_STRIKES, 0);
    }

    default void reduceEmpowered(ItemStack stack, World world, LivingEntity attacker) {
        if (isEmpowered(stack)) {
            NbtHelper.putInt(stack, NbtIds.SHARPENED_STRIKES, getEmpoweredAttacks(stack) - 1);
            if (getEmpoweredAttacks(stack) <= 0) {
                world.playSound(null, attacker.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, .75f, 1f);
            }
        }
    }

    int getMaxEmpoweredStrikes(ItemStack stack);
}
