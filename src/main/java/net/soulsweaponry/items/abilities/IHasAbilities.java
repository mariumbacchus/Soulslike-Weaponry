package net.soulsweaponry.items.abilities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.config.ClientConfig;
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
        return HasAbilitiesHooks.postHit(this, stack, target, attacker);
    }

    default boolean preventUse(ItemStack stack, PlayerEntity player) {
        return this.getAbilities().stream().anyMatch(a -> a.preventUsePredicate(stack, player));
    }

    default TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return HasAbilitiesHooks.use(this, world, user, hand);
    }

    default void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        HasAbilitiesHooks.onStoppedUsing(this, stack, world, user, remainingUseTicks);
    }

    default float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return HasAbilitiesHooks.getBonusAttackDamage(this, target, baseAttackDamage, damageSource);
    }

    default UseAction getUseAction(ItemStack stack) {
        return HasAbilitiesHooks.getUseAction(this, stack);
    }

    default int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return HasAbilitiesHooks.getMaxUseTime(this, stack, user);
    }

    default void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        HasAbilitiesHooks.inventoryTick(this, stack, world, entity, slot, selected);
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

    default void onAttackClickServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (this.isDisabled(stack)) {
            return;
        }
        // Can add sneaking versions of this later
        this.getAbilities().forEach(a -> a.onAttackClickServer(world, stack, player));
    }

    default ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return HasAbilitiesHooks.useOnEntity(this, stack, user, entity, hand);
    }

    default void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        HasAbilitiesHooks.usageTick(this, world, user, stack, remainingUseTicks);
    }

    default ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return HasAbilitiesHooks.finishUsing(this, stack, world, user);
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
