package net.soulsweaponry.items.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ComponentRegistry;

import java.util.Optional;

public interface ISharpened extends IAbility {

    static boolean isEmpowered(ItemStack stack) {
        return getEmpoweredAttacks(stack) > 0;
    }

    static Integer getEmpoweredAttacks(ItemStack stack) {
        return Optional.ofNullable(stack.get(ComponentRegistry.SHARPENED_STRIKES)).orElse(0);
    }

    default void reduceEmpowered(ItemStack stack, World world, LivingEntity attacker) {
        if (isEmpowered(stack)) {
            stack.set(ComponentRegistry.SHARPENED_STRIKES, getEmpoweredAttacks(stack) - 1);
            if (getEmpoweredAttacks(stack) <= 0) {
                world.playSound(null, attacker.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, .75f, 1f);
            }
        }
    }

    int getMaxEmpoweredStrikes(ItemStack stack);
}
