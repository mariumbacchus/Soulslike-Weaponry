package net.soulsweaponry.items.abilities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
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
import net.soulsweaponry.mixin.KeyBindingAccessor;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.TooltipUtil;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public interface IHasAbilities extends IConfigDisable {

    List<IAbility> getAbilities();

    default void addAbility(IAbility... abilities) {
        Collections.addAll(this.getAbilities(), abilities);
    }

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

    default boolean preventUse(ItemStack stack, PlayerEntity player) {
        return this.getAbilities().stream().anyMatch(a -> a.preventUsePredicate(stack, player));
    }

    default TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this.isDisabled(user.getStackInHand(hand))) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        ItemStack itemStack = user.getStackInHand(hand);

        boolean sneaking = user.isSneaking();
        boolean offhand = hand == Hand.OFF_HAND;
        boolean hasSneakAbility = this.hasSneakToUseAbility();
        boolean hasOffhandAbility = this.hasOffhandToUseAbility();

        List<IAbility> abilities = this.getAbilities();
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
            if (ConfigConstructor.prioritize_off_hand_shield_over_weapon && user.getOffHandStack().getItem() instanceof ShieldItem) {
                return TypedActionResult.fail(itemStack);
            } else if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
                return TypedActionResult.fail(itemStack);
            } else if (this.preventUse(itemStack, user)) {
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

    default void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        boolean sneaking = user.isSneaking();
        boolean offhand = user.getOffHandStack().isOf(stack.getItem());
        int fixedTicks = WeaponUtil.getChargeTime(stack, user, remainingUseTicks);

        // Only look at charge abilities when deciding the mode.
        boolean hasSneakChargeAbility = this.getAbilities().stream()
                .anyMatch(a -> a.isChargeToUse() && a.isSneakAbility());
        boolean hasOffhandChargeAbility = this.getAbilities().stream()
                .anyMatch(a -> a.isChargeToUse() && a.isOffhandAbility());

        for (IAbility a : this.getAbilities()) {
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

        List<IAbility> abilities = this.getAbilities();
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
                        if (sneaking && this.hasSneakToUseAbility()) return a.isSneakAbility();
                        if (offhand && this.hasOffhandToUseAbility()) return a.isOffhandAbility();
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

    default void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && this.preventUse(stack, player)) {
            return;
        }
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
        boolean offhand = player.getOffHandStack().isOf(stack.getItem());

        List<IAbility> abilities = this.getAbilities();

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


    default void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (this.isDisabled(stack)) {
            return; // Disabled item notification is given on client side
        }

        boolean sneaking = player.isSneaking();
        boolean offhand = player.getOffHandStack().isOf(stack.getItem());

        List<IAbility> abilities = this.getAbilities();
        boolean hasSneakKeybindAbility = abilities.stream()
                .anyMatch(a -> a.isKeybindAbility() && a.isSneakAbility());
        boolean hasOffhandKeybindAbility = abilities.stream()
                .anyMatch(a -> a.isKeybindAbility() && a.isOffhandAbility());

        for (IAbility a : abilities) {
            if (!a.isKeybindAbility()) {
                continue;
            }

            if (sneaking && hasSneakKeybindAbility) {
                if (a.isSneakAbility()) {
                    a.sneakingUseKeybindAbilityServer(world, stack, player, hand);
                }
                continue;
            }

            if (offhand && hasOffhandKeybindAbility) {
                if (a.isOffhandAbility()) {
                    a.offhandUseKeybindAbilityServer(world, stack, player, hand);
                }
                continue;
            }

            if (sneaking && a.isSneakAbility()) {
                a.sneakingUseKeybindAbilityServer(world, stack, player, hand);
            } else if (offhand && a.isOffhandAbility()) {
                a.offhandUseKeybindAbilityServer(world, stack, player, hand);
            } else if (!a.isSneakAbility() && !a.isOffhandAbility()) {
                a.useKeybindAbilityServer(world, stack, player, hand);
            }
        }
    }

    default void onAttackClickClient(ClientWorld world, ItemStack stack, PlayerEntity player) {
        if (this.isDisabled(stack)) {
            return;
        }
        // Can add sneaking versions of this later
        this.getAbilities().forEach(a -> a.onAttackClickClient(world, stack, player));
    }

    default void onAttackClickServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (this.isDisabled(stack)) {
            return;
        }
        // Can add sneaking versions of this later
        this.getAbilities().forEach(a -> a.onAttackClickServer(world, stack, player));
    }

    default ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (this.isDisabled(stack) || this.preventUse(stack, user)) {
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
        boolean offhand = user.getOffHandStack().isOf(stack.getItem());

        boolean hasSneakChargeAbility = this.getAbilities().stream()
                .anyMatch(a -> a.isChargeToUse() && a.isSneakAbility());
        boolean hasOffhandChargeAbility = this.getAbilities().stream()
                .anyMatch(a -> a.isChargeToUse() && a.isOffhandAbility());

        for (IAbility a : this.getAbilities()) {
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


    default ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (this.isDisabled(stack)) {
            return stack;
        }
        this.getAbilities().forEach(a -> a.finishUsing(stack, world, user));
        return stack;
    }

    /**
     * Used in armor items only.
     * @param vanillaBuilder the vanilla attributes to add custom ones to
     * @param equipmentSlot equipment slot the armor item is meant for
     * @return builder with the additional attributes
     */
    default AttributeModifiersComponent.Builder applyArmorAttributeModifiers(AttributeModifiersComponent vanillaBuilder, EquipmentSlot equipmentSlot) {
        AttributeModifiersComponent.Builder builder = WeaponUtil.createAndCopyAttributes(vanillaBuilder);
        AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(equipmentSlot);
        this.getAbilities().forEach(ability -> {
            ability.addArmorAttributeModifiers(builder, equipmentSlot, slot);
        });
        return builder;
    }

    /**
     * Mainly used in {@link net.minecraft.item.RangedWeaponItem} items.
     * Will pick out the first ability overriding the predicate to not return null.
     * Normally used for Silver Bullet abilities used for Gun items.
     */
    default Predicate<ItemStack> getProjectiles() {
        return this.getAbilities().stream()
                .map(IAbility::getProjectiles)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse((stack) -> stack.isOf(ItemRegistry.SILVER_BULLET));
    }

    default void appendTooltipAbilities(ItemStack stack, List<Text> tooltip) {
        this.applyTooltipAbilities(tooltip, stack);
        TooltipUtil.addAbilityTooltip(TooltipUtil.TooltipAbilities.TRICK_WEAPON, stack, tooltip);
        int lvl = stack.getOrDefault(ComponentRegistry.ITEM_UPGRADE_LEVEL, 0);
        if (lvl > 0) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.level", lvl).formatted(Formatting.DARK_GRAY));
        }
    }

    /**
     * Adds all tooltip abilities listed in {@link IAbility#getTooltipAbilities(ItemStack)} to the item tooltip.
     * Additional lore is also applied if the weapons have it.
     */
    default void applyTooltipAbilities(List<Text> tooltip, ItemStack stack) {
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
        // Lore from items specifically
        lore.addAll(this.getItemLore());
        // Info specific items have, such as Chungus Staff reminding you it can only be traded to get, not crafted
        tooltipAbilities.addAll(this.getAdditionalItemTooltips());
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

    default List<Text> getItemLore() {
        return List.of();
    }

    default List<Text> getAdditionalItemTooltips() {
        return List.of();
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
