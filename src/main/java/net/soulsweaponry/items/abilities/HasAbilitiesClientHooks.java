package net.soulsweaponry.items.abilities;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class HasAbilitiesClientHooks {

    public static void useKeybindAbilityClient(IHasAbilities self, ClientWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (self.isDisabled(stack)) {
            self.notifyDisabled(player);
            return;
        }

        boolean sneaking = player.isSneaking();
        boolean offhand = player.getOffHandStack().isOf(stack.getItem());

        List<IAbility> abilities = self.getAbilities();

        // Only look at abilities that actually care about the keybind
        boolean hasSneakKeybindAbility = abilities.stream()
                .anyMatch(a -> a.isKeybindAbility() && a.isSneakAbility());
        boolean hasOffhandKeybindAbility = abilities.stream()
                .anyMatch(a -> a.isKeybindAbility() && a.isOffhandAbility());

        for (IAbility a : abilities) {
            if (!a.isKeybindAbility()) {
                continue; // this ability doesn't care about the keybind at all
            }

            if (sneaking && hasSneakKeybindAbility) {
                // Sneaking override mode, only sneaking keybind abilities fire
                if (a.isSneakAbility()) {
                    a.sneakingUseKeybindAbilityClient(world, stack, player, hand);
                }
                continue;
            }

            if (offhand && hasOffhandKeybindAbility) {
                // Offhand override mode, only offhand keybind abilities fire
                if (a.isOffhandAbility()) {
                    a.offhandUseKeybindAbilityClient(world, stack, player, hand);
                }
                continue;
            }

            // No sneaking/offhand keybind override in effect, let abilities behave as they are flagged
            if (sneaking && a.isSneakAbility()) {
                a.sneakingUseKeybindAbilityClient(world, stack, player, hand);
            } else if (offhand && a.isOffhandAbility()) {
                a.offhandUseKeybindAbilityClient(world, stack, player, hand);
            } else if (!a.isSneakAbility() && !a.isOffhandAbility()) {
                a.useKeybindAbilityClient(world, stack, player, hand);
            }
        }
    }

    public static void onAttackClickClient(IHasAbilities self, ClientWorld world, ItemStack stack, PlayerEntity player) {
        if (self.isDisabled(stack)) {
            return;
        }
        // Can add sneaking versions of this later
        self.getAbilities().forEach(a -> a.onAttackClickClient(world, stack, player));
    }
}
