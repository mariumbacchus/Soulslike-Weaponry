package net.soulsweaponry.items.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;
import java.util.OptionalInt;

public final class HasAbilitiesHooks {

    public static boolean postHit(IHasAbilities self, ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!self.isDisabled(stack)) {
            self.getAbilities().forEach(a -> a.postHit(stack, target, attacker));
        }
        return true;
    }

    public static TypedActionResult<ItemStack> use(IHasAbilities self, World world, PlayerEntity user, Hand hand) {
        if (self.isDisabled(user.getStackInHand(hand))) {
            self.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        ItemStack itemStack = user.getStackInHand(hand);

        boolean sneaking = user.isSneaking();
        boolean offhand = hand == Hand.OFF_HAND;
        boolean hasSneakAbility = self.hasSneakToUseAbility();
        boolean hasOffhandAbility = self.hasOffhandToUseAbility();

        List<IAbility> abilities = self.getAbilities();
        boolean hasSneakCharge = abilities.stream()
                .anyMatch(a -> a.isSneakAbility() && a.isChargeToUse());
        boolean hasOffhandCharge = abilities.stream()
                .anyMatch(a -> a.isOffhandAbility() && a.isChargeToUse());
        boolean hasNormalCharge = abilities.stream()
                .anyMatch(a -> !a.isSneakAbility() && !a.isOffhandAbility() && a.isChargeToUse());

        boolean hasChargeInThisMode;
        if (sneaking && hasSneakAbility) {
            // Sneaking mode:
            //  - If there is a sneaking charge ability, use charge.
            //  - Else, fall back to normal charge.
            hasChargeInThisMode = hasSneakCharge || hasNormalCharge;
        } else if (offhand && hasOffhandAbility) {
            // Offhand mode:
            //  - Prefer offhand charge if present, otherwise fall back to normal charge.
            hasChargeInThisMode = hasOffhandCharge || hasNormalCharge;
        } else {
            // Normal (not sneaking/offhand-prioritized) mode:
            //  - Only normal charge counts. Sneak-only charge shouldn't trigger here.
            hasChargeInThisMode = hasNormalCharge;
        }

        // Charging ability
        if (hasChargeInThisMode) {
            if (WeaponConfig.prioritize_off_hand_shield_over_weapon && user.getOffHandStack().getItem() instanceof ShieldItem) {
                return TypedActionResult.fail(itemStack);
            } else if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
                return TypedActionResult.fail(itemStack);
            } else if (self.preventUse(itemStack, user)) {
                return TypedActionResult.fail(itemStack);
            } else {
                user.setCurrentHand(hand);
                return TypedActionResult.consume(itemStack);
            }
        }

        // Not charging, just regular use
        ItemStack out = itemStack;

        boolean sawSuccess = false;
        boolean sawConsume = false;
        boolean sawConsumePartial = false;
        boolean sawSuccessNoItemUsed = false;
        boolean sawFail = false;

        for (var a : abilities) {
            TypedActionResult<ItemStack> r;
            if (hasSneakAbility && sneaking) {
                r = a.sneakingUse(world, user, hand, out);
            } else if (hasOffhandAbility && offhand) {
                r = a.offhandUse(world, user, hand, out);
            } else {
                r = a.use(world, user, hand, out);
            }
            out = r.getValue();
            switch (r.getResult()) {
                case SUCCESS -> sawSuccess = true;
                case CONSUME -> sawConsume = true;
                case CONSUME_PARTIAL -> sawConsumePartial = true;
                case SUCCESS_NO_ITEM_USED -> sawSuccessNoItemUsed = true;
                case FAIL -> sawFail = true;
                case PASS -> {}
            }
        }

        if (sawSuccess) {
            return TypedActionResult.success(out, world.isClient());
        }
        if (sawConsume) {
            return TypedActionResult.consume(out);
        }
        if (sawSuccessNoItemUsed) {
            return new TypedActionResult<>(ActionResult.SUCCESS_NO_ITEM_USED, out);
        }
        if (sawConsumePartial) {
            return new TypedActionResult<>(ActionResult.CONSUME_PARTIAL, out);
        }
        if (sawFail) {
            return TypedActionResult.fail(out);
        }
        return TypedActionResult.pass(out);
    }

    public static void onStoppedUsing(IHasAbilities self, ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        boolean sneaking = user.isSneaking();
        boolean offhand = user.getOffHandStack().isOf(stack.getItem());
        int fixedTicks = WeaponUtil.getChargeTime(stack, user, remainingUseTicks);

        // Only look at charge abilities when deciding the mode.
        boolean hasSneakChargeAbility = self.getAbilities().stream()
                .anyMatch(a -> a.isChargeToUse() && a.isSneakAbility());
        boolean hasOffhandChargeAbility = self.getAbilities().stream()
                .anyMatch(a -> a.isChargeToUse() && a.isOffhandAbility());

        for (IAbility a : self.getAbilities()) {
            if (sneaking && hasSneakChargeAbility) {
                // Sneak mode, only sneaking charge abilities fire, everything else is suppressed.
                if (a.isSneakAbility() && a.isChargeToUse()) {
                    a.sneakingOnStoppedUsing(stack, world, user, fixedTicks);
                }
                continue;
            }

            if (offhand && hasOffhandChargeAbility) {
                // Offhand mode, only offhand charge abilities fire.
                if (a.isOffhandAbility() && a.isChargeToUse()) {
                    a.offhandOnStoppedUsing(stack, world, user, fixedTicks);
                }
                continue;
            }

            // Normal mode, only non-sneak, non-offhand abilities handle onStoppedUsing.
            if (!a.isSneakAbility() && !a.isOffhandAbility()) {
                a.onStoppedUsing(stack, world, user, fixedTicks);
            }
        }
    }

    public static float getBonusAttackDamage(IHasAbilities self, Entity target, float baseAttackDamage, DamageSource damageSource) {
        if (self.isDisabled(ItemStack.EMPTY)) {
            return 0f;
        }
        return (float) self.getAbilities().stream().mapToDouble(a -> a.getBonusAttackDamage(target, baseAttackDamage, damageSource)).sum();
    }

    public static UseAction getUseAction(IHasAbilities self, ItemStack stack) {
        UseAction best = UseAction.NONE;
        int bestPrio = Integer.MIN_VALUE;

        for (IAbility a : self.getAbilities()) {
            if (a.isSneakAbility() || a.isOffhandAbility()) {
                continue;
            }
            UseAction hint = a.getUseAction();
            int pr = a.useActionPriority();
            if (hint != UseAction.NONE && pr > bestPrio) {
                best = hint;
                bestPrio = pr;
            }
        }
        if (best != UseAction.NONE) {
            return best;
        }
        return self.hasChargeToUseAbility() ? UseAction.SPEAR : UseAction.NONE;
    }

    public static int getMaxUseTime(IHasAbilities self, ItemStack stack, LivingEntity user) {
        boolean sneaking = user.isSneaking();
        boolean offhand = user.getOffHandStack().isOf(stack.getItem());

        List<IAbility> abilities = self.getAbilities();
        boolean hasSneakCharge = abilities.stream()
                .anyMatch(a -> a.isSneakAbility() && a.isChargeToUse());
        boolean hasOffhandCharge = abilities.stream()
                .anyMatch(a -> a.isOffhandAbility() && a.isChargeToUse());
        boolean hasNormalCharge = abilities.stream()
                .anyMatch(a -> !a.isSneakAbility() && !a.isOffhandAbility() && a.isChargeToUse());

        // Choose which abilities can actually handle the charge in this context
        List<IAbility> subset;
        if (sneaking && hasSneakCharge) {
            subset = abilities.stream()
                    .filter(a -> a.isSneakAbility() && a.isChargeToUse())
                    .toList();
        } else if (offhand && hasOffhandCharge) {
            subset = abilities.stream()
                    .filter(a -> a.isOffhandAbility() && a.isChargeToUse())
                    .toList();
        } else if (hasNormalCharge) {
            subset = abilities.stream()
                    .filter(a -> !a.isSneakAbility() && !a.isOffhandAbility() && a.isChargeToUse())
                    .toList();
        } else {
            // No charge abilities at all, fall back to original behavior for non-charge custom maxUseTime
            subset = abilities.stream()
                    .filter(a -> {
                        if (sneaking && self.hasSneakToUseAbility()) return a.isSneakAbility();
                        if (offhand && self.hasOffhandToUseAbility()) return a.isOffhandAbility();
                        return !a.isSneakAbility() && !a.isOffhandAbility();
                    })
                    .toList();
        }

        // Abilities override max use time if they want to
        OptionalInt maxUse = subset.stream()
                .mapToInt(a -> a.getMaxUseTime(stack, user))
                .filter(v -> v >= 0)
                .max();

        if (maxUse.isPresent()) {
            return maxUse.getAsInt();
        }

        // Default is 72000 if any of the subset are charge abilities, else 0
        boolean hasCharge = subset.stream().anyMatch(IAbility::isChargeToUse);
        return hasCharge ? 72000 : 0;
    }

    public static void inventoryTick(IHasAbilities self, ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && self.preventUse(stack, player)) {
            return;
        }
        if (!self.isDisabled(stack)) {
            self.getAbilities().forEach(a -> a.inventoryTick(stack, world, entity, slot, selected));
        }
    }

    public static ActionResult useOnEntity(IHasAbilities self, ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (self.isDisabled(stack) || self.preventUse(stack, user)) {
            return ActionResult.FAIL;
        }
        boolean sawSuccess = false;
        boolean sawConsume = false;
        boolean sawConsumePartial = false;
        boolean sawSuccessNoItemUsed = false;
        boolean sawFail = false;

        for (var a : self.getAbilities()) {
            ActionResult result = a.useOnEntity(stack, user, entity, hand);
            switch (result) {
                case SUCCESS -> sawSuccess = true;
                case CONSUME -> sawConsume = true;
                case CONSUME_PARTIAL -> sawConsumePartial = true;
                case SUCCESS_NO_ITEM_USED -> sawSuccessNoItemUsed = true;
                case FAIL -> sawFail = true;
                case PASS -> {}
            }
        }
        if (sawSuccess) {
            return ActionResult.SUCCESS;
        }
        if (sawConsume) {
            return ActionResult.CONSUME;
        }
        if (sawSuccessNoItemUsed) {
            return ActionResult.SUCCESS_NO_ITEM_USED;
        }
        if (sawConsumePartial) {
            return ActionResult.CONSUME_PARTIAL;
        }
        if (sawFail) {
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static void usageTick(IHasAbilities self, World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        boolean sneaking = user.isSneaking();
        boolean offhand = user.getOffHandStack().isOf(stack.getItem());

        boolean hasSneakChargeAbility = self.getAbilities().stream()
                .anyMatch(a -> a.isChargeToUse() && a.isSneakAbility());
        boolean hasOffhandChargeAbility = self.getAbilities().stream()
                .anyMatch(a -> a.isChargeToUse() && a.isOffhandAbility());

        for (IAbility a : self.getAbilities()) {
            if (sneaking && hasSneakChargeAbility) {
                if (a.isSneakAbility() && a.isChargeToUse()) {
                    a.sneakingUsageTick(world, user, stack, remainingUseTicks);
                }
                continue;
            }
            if (offhand && hasOffhandChargeAbility) {
                if (a.isOffhandAbility() && a.isChargeToUse()) {
                    a.offhandUsageTick(world, user, stack, remainingUseTicks);
                }
                continue;
            }
            if (!a.isSneakAbility() && !a.isOffhandAbility()) {
                a.usageTick(world, user, stack, remainingUseTicks);
            }
        }
    }

    public static ItemStack finishUsing(IHasAbilities self, ItemStack stack, World world, LivingEntity user) {
        if (self.isDisabled(stack)) {
            return stack;
        }
        self.getAbilities().forEach(a -> a.finishUsing(stack, world, user));
        return stack;
    }
}
