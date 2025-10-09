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
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.IChargeNeeded;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;
import java.util.Optional;

public interface IHasAbilities extends IConfigDisable {
    // TODO implement into all the abstract item classes (axes, bows, etc.) and use this to build the new ability system off of
    List<IAbility> getAbilities();
    void addAbility(IAbility... abilities);

    default <T extends IAbility> Optional<T> findAbility(Class<T> type) {
        for (IAbility a : getAbilities()) {
            if (type.isInstance(a)) return Optional.of(type.cast(a));
        }
        return Optional.empty();
    }

    /**
     * @return true if any ability is charge to use, meaning it will need the {@link net.minecraft.item.Item#onStoppedUsing(ItemStack, World, LivingEntity, int)}
     * call. This will make the item skip all calls from the {@link net.minecraft.item.Item#use(World, PlayerEntity, Hand)} method for abilities.
     * <p>
     * In other words, either only {@code use()} is called or only {@code onStoppedUsing()} is called.
     */
    default boolean hasChargeToUseAbility() {
        for (IAbility a : getAbilities()) {
            if (a.isChargeToUse()) return true;
        }
        return false;
    }

    /**
     * @return true if any ability requires sneaking to use, meaning regular use methods will be skipped if
     * at least one ability returns true.
     */
    default boolean hasSneakToUseAbility() {
        for (IAbility a : getAbilities()) {
            if (a.isSneakAbility()) return true;
        }
        return false;
    }

    /**
     * @return true if any ability requires the item to be in offhand to use, meaning regular use methods
     * will be skipped if at least one ability returns true.
     */
    default boolean hasOffhandToUseAbility() {
        for (IAbility a : getAbilities()) {
            if (a.isOffhandAbility()) return true;
        }
        return false;
    }

    static <T extends IAbility> Optional<T> getAbility(ItemStack stack, Class<T> type) {
        if (stack.getItem() instanceof IHasAbilities has) {
            return has.findAbility(type);
        }
        return Optional.empty();
    }

    default boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            this.getAbilities().forEach(a -> a.postHit(stack, target, attacker));
        }
        return true;
    }

    default TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this.isDisabled(user.getStackInHand(hand))) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        ItemStack itemStack = user.getStackInHand(hand);
        if (this.hasChargeToUseAbility()) {
            if (ConfigConstructor.prioritize_off_hand_shield_over_weapon && user.getOffHandStack().getItem() instanceof ShieldItem) {
                return TypedActionResult.fail(itemStack);
            } else if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
                return TypedActionResult.fail(itemStack);
            } else if (this instanceof IChargeNeeded charge //TODO change this up later when adding charge needed ability
                    && !charge.isCharged(itemStack)
                    && !user.isCreative()
                    && charge.acceptsMoonHeraldEffect(itemStack)
                    && !user.hasStatusEffect(EffectRegistry.MOON_HERALD)) {
                return TypedActionResult.fail(itemStack);
            } else {
                user.setCurrentHand(hand);
                return TypedActionResult.success(itemStack);
            }
        } else {
            // Presence-based hierarchy, do success if any ability returned success, do consume next if no success and so on...
            ItemStack out = itemStack;
            boolean sneaking = user.isSneaking();
            boolean hasSneakAbility = this.hasSneakToUseAbility();
            boolean offhand = hand == Hand.OFF_HAND;
            boolean hasOffhandAbility = this.hasOffhandToUseAbility();

            boolean sawSuccess = false;
            boolean sawConsume = false;
            boolean sawConsumePartial = false;
            boolean sawSuccessNoItemUsed = false;
            boolean sawFail = false;

            for (var a : this.getAbilities()) {
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
    }

    default void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        boolean sneaking = user.isSneaking();
        boolean hasSneakAbility = this.hasSneakToUseAbility();
        boolean offhand = user.getOffHandStack().isOf(stack.getItem());//TODO test this
        boolean hasOffhandAbility = this.hasOffhandToUseAbility();
        int fixedTicks = WeaponUtil.getChargeTime(stack, user, remainingUseTicks);
        this.getAbilities().forEach(a -> {
            if (hasSneakAbility && sneaking) {
                a.sneakingOnStoppedUsing(stack, world, user, fixedTicks);
            } else if (hasOffhandAbility && offhand) {
                a.offhandOnStoppedUsing(stack, world, user, fixedTicks);
            } else {
                a.onStoppedUsing(stack, world, user, fixedTicks);
            }
        });
    }

    default float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if (this.isDisabled(ItemStack.EMPTY)) {
            return 0f;
        }
        return (float) this.getAbilities().stream().mapToDouble(a -> a.getBonusAttackDamage(target, baseAttackDamage, damageSource)).sum();
    }

    default UseAction getUseAction(ItemStack stack) {
        return this.hasChargeToUseAbility() ? UseAction.SPEAR : UseAction.NONE;
    }

    default int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return this.hasChargeToUseAbility() ? 72000 : 0;
    }

    default void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!this.isDisabled(stack)) {
            this.getAbilities().forEach(a -> a.inventoryTick(stack, world, entity, slot, selected));
        }
    }
}
