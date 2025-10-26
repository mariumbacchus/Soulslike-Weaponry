package net.soulsweaponry.items.abilities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.config.ClientConfig;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.IChargeNeeded;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.mixin.KeyBindingAccessor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
                return TypedActionResult.consume(itemStack);
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
        UseAction best = UseAction.NONE;
        int bestPrio = Integer.MIN_VALUE;

        for (IAbility a : getAbilities()) {
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
        return this.hasChargeToUseAbility() ? UseAction.SPEAR : UseAction.NONE;
    }

    default int getMaxUseTime(ItemStack stack, LivingEntity user) {
        boolean sneaking = user.isSneaking();
        boolean offhand = user.getOffHandStack().isOf(stack.getItem());

        List<IAbility> subset = this.getAbilities().stream()
                .filter(a -> {
                    if (sneaking && this.hasSneakToUseAbility()) return a.isSneakAbility();
                    if (offhand && this.hasOffhandToUseAbility()) return a.isOffhandAbility();
                    return !a.isSneakAbility() && !a.isOffhandAbility();
                })
                .toList();

        OptionalInt maxUse = subset.stream()
                .mapToInt(a -> a.getMaxUseTime(stack, user))
                .filter(v -> v >= 0)  // ignore default -1
                .max();

        if (maxUse.isPresent()) {
            return maxUse.getAsInt();
        }
        return this.hasChargeToUseAbility() ? 72000 : 0;
    }

    default void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!this.isDisabled(stack)) {
            this.getAbilities().forEach(a -> a.inventoryTick(stack, world, entity, slot, selected));
        }
    }

    default void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (this.isDisabled(stack)) {
            this.notifyDisabled(player);
            return;
        }
        boolean sneaking = player.isSneaking();
        boolean hasSneakAbility = this.hasSneakToUseAbility();
        boolean offhand = player.getOffHandStack().isOf(stack.getItem());//TODO test this
        boolean hasOffhandAbility = this.hasOffhandToUseAbility();
        this.getAbilities().forEach(a -> {
            if (hasSneakAbility && sneaking) {
                a.sneakingUseKeybindAbilityClient(world, stack, player, hand);
            } else if (hasOffhandAbility && offhand) {
                a.offhandUseKeybindAbilityClient(world, stack, player, hand);
            } else {
                a.useKeybindAbilityClient(world, stack, player, hand);
            }
        });
    }

    default void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (this.isDisabled(stack)) {
            return; // Disabled item notification is given on client side
        }
        boolean sneaking = player.isSneaking();
        boolean hasSneakAbility = this.hasSneakToUseAbility();
        boolean offhand = player.getOffHandStack().isOf(stack.getItem());//TODO test this
        boolean hasOffhandAbility = this.hasOffhandToUseAbility();
        this.getAbilities().forEach(a -> {
            if (hasSneakAbility && sneaking) {
                a.sneakingUseKeybindAbilityServer(world, stack, player, hand);
            } else if (hasOffhandAbility && offhand) {
                a.offhandUseKeybindAbilityServer(world, stack, player, hand);
            } else {
                a.useKeybindAbilityServer(world, stack, player, hand);
            }
        });
    }

    default ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (this.isDisabled(stack)) {
            return ActionResult.FAIL;
        }
        boolean sawSuccess = false;
        boolean sawConsume = false;
        boolean sawConsumePartial = false;
        boolean sawSuccessNoItemUsed = false;
        boolean sawFail = false;

        for (var a : this.getAbilities()) {
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

    default void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        boolean sneaking = user.isSneaking();
        boolean hasSneakAbility = this.hasSneakToUseAbility();
        boolean offhand = user.getOffHandStack().isOf(stack.getItem());//TODO test this
        boolean hasOffhandAbility = this.hasOffhandToUseAbility();
        this.getAbilities().forEach(a -> {
            if (hasSneakAbility && sneaking) {
                a.sneakingUsageTick(world, user, stack, remainingUseTicks);
            } else if (hasOffhandAbility && offhand) {
                a.offhandUsageTick(world, user, stack, remainingUseTicks);
            } else {
                a.usageTick(world, user, stack, remainingUseTicks);
            }
        });
    }

    default ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (this.isDisabled(stack)) {
            return stack;
        }
        this.getAbilities().forEach(a -> a.finishUsing(stack, world, user));
        return stack;
    }

    /**
     * Adds all tooltip abilities listed in {@link IAbility#getTooltipAbilities(ItemStack)} to the item tooltip.
     * Additional lore is also applied if the weapons have it.
     */
    default void appendTooltipAbilities(List<Text> tooltip, ItemStack stack) {
        if (this.isDisabled(stack)) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        List<Text> tooltipAbilities = new ArrayList<>();
        List<Text> lore = new ArrayList<>();
        this.getAbilities().forEach(ability -> {
            List<Text> setup = new ArrayList<>();
            setup.addAll(ability.getTooltipAbilities(stack));
            setup.addAll(ability.getBonusAbilityTooltip(stack));
            tooltipAbilities.addAll(setup);
        });
        this.getAbilities().forEach(ability -> lore.addAll(ability.getLoreTooltips(stack)));
        if (stack.getItem() instanceof IHasLore hasLore) {
            // Lore from items specifically
            lore.addAll(hasLore.getLore());
        }
        if (stack.getItem() instanceof IItemSpecificTooltip iItemSpecificTooltip) {
            // Info specific items have, such as Chungus Staff reminding you it can only be traded to get, not crafted
            tooltipAbilities.addAll(iItemSpecificTooltip.getItemSpecificTooltip());
        }
        if (!tooltipAbilities.isEmpty()) {
            if (shouldShowInfo()) {
                tooltip.addAll(tooltipAbilities);
            } else {
                addShowInfoText(tooltip);
            }
        }
        if (!lore.isEmpty()) {
            if (shouldShowLore()) {
                tooltip.addAll(lore);
            } else {
                addShowLoreText(tooltip);
            }
        }
    }

    /**
     * @return Whether the info button is being held when hovering an item, button is ALT if Epic Fight mod
     * is installed or SHIFT otherwise by default, can be changed in controls settings.
     */
    static boolean shouldShowInfo() {
        if (ClientConfig.always_show_item_tooltip) {
            return true;
        }
        if (KeyBindRegistry.showItemTooltip.isUnbound()) {
            boolean epicFight = WeaponUtil.isModLoaded("epicfight");
            return epicFight ? Screen.hasAltDown() : Screen.hasShiftDown();
        }
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), ((KeyBindingAccessor)KeyBindRegistry.showItemTooltip).getBoundKey().getCode());
    }

    static boolean shouldShowLore() {
        if (ClientConfig.always_show_item_lore) {
            return true;
        }
        if (KeyBindRegistry.showItemLore.isUnbound()) {
            return Screen.hasControlDown();
        }
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), ((KeyBindingAccessor)KeyBindRegistry.showItemLore).getBoundKey().getCode());
    }

    static Text getShowInfoKeyText() {
        if (KeyBindRegistry.showItemTooltip.isUnbound()) {
            boolean epicFight = WeaponUtil.isModLoaded("epicfight");
            return epicFight ? Text.translatable("key.keyboard.left.alt") : Text.translatable("key.keyboard.left.shift");
        }
        return KeyBindRegistry.showItemTooltip.getBoundKeyLocalizedText();
    }

    static Text getShowLoreKeyText() {
        if (KeyBindRegistry.showItemLore.isUnbound()) {
            return Text.translatable("key.keyboard.left.control");
        }
        return KeyBindRegistry.showItemLore.getBoundKeyLocalizedText();
    }

    static MutableText formatKeybindText(Text input) {
        MutableText text = input.copy();
        String upper = text.getString().toUpperCase(Locale.ROOT);
        return Text.literal(upper).formatted(Formatting.YELLOW);
    }

    static void addShowInfoText(List<Text> tooltip) {
        MutableText keyText = formatKeybindText(getShowInfoKeyText());
        tooltip.add(Text.translatable("tooltip.soulsweapons.show_item_info", keyText));
    }

    static void addShowLoreText(List<Text> tooltip) {
        MutableText keyText = formatKeybindText(getShowLoreKeyText());
        tooltip.add(Text.translatable("tooltip.soulsweapons.show_item_lore", keyText));
    }
}
