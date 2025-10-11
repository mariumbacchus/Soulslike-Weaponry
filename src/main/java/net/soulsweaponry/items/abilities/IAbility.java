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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.config.ClientConfig;
import net.soulsweaponry.items.ICooldownItem;
import net.soulsweaponry.mixin.KeyBindingAccessor;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * TODO
 * Build upon this when needed, remember to actually call each method when adding them!
 */
public interface IAbility extends ICooldownItem {

    default void onMainHandEquip(PlayerEntity player, ItemStack stack) {}
    default void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {}
    default float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) { return 0f; }

    /**
     * Called when {@link #isChargeToUse()} returns {@code true}.
     * Will not be called if the user is sneaking or if the item is in offhand,
     * if so {@link #sneakingOnStoppedUsing(ItemStack, World, LivingEntity, int)}
     * or {@link #offhandOnStoppedUsing(ItemStack, World, LivingEntity, int)}
     * will be called instead.
     * @param stack itemstack used
     * @param world world
     * @param user user wielding the stack
     * @param remainingUseTicks remaining use ticks, this automatically calls {@link WeaponUtil#getChargeTime(ItemStack, LivingEntity, int)}
     *                          to get accurate ticks based on mods installed (epic fight mod messes things up for example), so no need
     *                          to call it again
     */
    default void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {}

    /**
     * Called when {@link #isChargeToUse()} returns {@code true} and the user is sneaking.
     * Remember to override {@link #isSneakAbility()} to return {@code true} when overriding this.
     * @param stack itemstack used
     * @param world world
     * @param user user wielding the stack
     * @param remainingUseTicks remaining use ticks, this automatically calls {@link WeaponUtil#getChargeTime(ItemStack, LivingEntity, int)}
     *                          to get accurate ticks based on mods installed (epic fight mod messes things up for example), so no need
     *                          to call it again
     */
    default void sneakingOnStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {}

    /**
     * Called when {@link #isChargeToUse()} returns {@code true} and only if the item is in offhand.
     * Remember to override {@link #isOffhandAbility()} to return {@code true} when overriding this.
     * @deprecated This should be avoided since many weapons can't be equipped in offhand with Better Combat installed. Try turning it into a keybind or sneaking ability instead.
     * @param stack itemstack used
     * @param world world
     * @param user user wielding the stack
     * @param remainingUseTicks remaining use ticks, this automatically calls {@link WeaponUtil#getChargeTime(ItemStack, LivingEntity, int)}
     *                          to get accurate ticks based on mods installed (epic fight mod messes things up for example), so no need
     *                          to call it again
     */
    @Deprecated
    default void offhandOnStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {}

    /**
     * Called when {@link #isChargeToUse()} returns {@code false}.
     * Will not be called if the user is sneaking, if so {@link #sneakingUse(World, PlayerEntity, Hand, ItemStack)}
     * will be called instead.
     * @param world world
     * @param user user
     * @param hand hand used
     * @param stack stack in the hand used
     * @return the typed action result
     */
    default TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) { return TypedActionResult.pass(user.getStackInHand(hand)); }

    /**
     * Called when {@link #isChargeToUse()} returns {@code false} and the user is sneaking.
     * Remember to override {@link #isSneakAbility()} to return {@code true} when overriding this.
     * @param world world
     * @param user user
     * @param hand hand used
     * @param stack stack in the hand used
     * @return the typed action result
     */
    default TypedActionResult<ItemStack> sneakingUse(World world, PlayerEntity user, Hand hand, ItemStack stack) { return TypedActionResult.pass(user.getStackInHand(hand)); }

    /**
     * Called when {@link #isChargeToUse()} returns {@code false} and the item is in offhand.
     * Remember to override {@link #isOffhandAbility()} to return {@code true} when overriding this.
     * @deprecated This should be avoided since many weapons can't be equipped in offhand with Better Combat installed. Try turning it into a keybind or sneaking ability instead.
     * @param world world
     * @param user user
     * @param hand hand used
     * @param stack stack in the hand used
     * @return the typed action result
     */
    @Deprecated
    default TypedActionResult<ItemStack> offhandUse(World world, PlayerEntity user, Hand hand, ItemStack stack) { return TypedActionResult.pass(user.getStackInHand(hand)); }

    /**
     * NOTE: This must return true if the ability needs to be charged (hold down right click) for a certain time to be
     * used, for example shooting moonlight beams with Moonlight Greatsword.
     * <p>
     * If this returns false, then the {@link #use(World, PlayerEntity, Hand, ItemStack)} method will be called for all abilities.
     * <p>
     * If this returns true, then the {@link #onStoppedUsing(ItemStack, World, LivingEntity, int)} method will be called after releasing the charge on the item
     * and the {@link #use(World, PlayerEntity, Hand, ItemStack)} call is skipped for all other abilities as well.
     * <p>
     * In other words, either only {@code use()} is called or only {@code onStoppedUsing()} is called for all abilities depending on the return of this.
     */
    default boolean isChargeToUse() { return false; }

    /**
     * If true, both {@link #use(World, PlayerEntity, Hand, ItemStack)} and {@link #onStoppedUsing(ItemStack, World, LivingEntity, int)}
     * will be called even if the user is sneaking.
     * <p>
     * This way, if no ability returns true, regular use methods are called, meaning those are the
     * only abilities that exist on the item and should still trigger, preventing nothing from happening at all.
     */
    default boolean isSneakAbility() { return false; }

    /**
     * If true, calls for both {@link #use(World, PlayerEntity, Hand, ItemStack)} and {@link #onStoppedUsing(ItemStack, World, LivingEntity, int)}
     * will be prevented.
     * <p>
     * This way, if no ability returns true, regular use methods are called, meaning those are the
     * only abilities that exist on the item and should still trigger, preventing nothing from happening at all.
     */
    default boolean isOffhandAbility() { return false; }

    /**
     * Called when the user is damaged when wielding this item.
     */
    default void onUserDamaged(DamageSource source, float amount, ItemStack stack, LivingEntity user, LivingEntity attacker) {}

    /**
     * Called whenever the user dies.
     */
    default void onUserDeath(DamageSource damageSource, ItemStack stack, LivingEntity user, LivingEntity attacker) {}

    /**
     * Called every tick the item is in the users inventory.
     */
    default void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {}

    /**
     * Server side effects when pressing the {@link KeyBindRegistry#keybindAbility}.
     * @param world server world
     * @param stack item stack
     * @param player (server) player
     */
    default void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {}

    /**
     * Client side effects when pressing the {@link KeyBindRegistry#keybindAbility}.
     * @param world client world
     * @param stack item stack
     * @param player client player
     */
    default void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {}

    /**
     * Called whenever an entity dies and the last damage source was the attacker.
     */
    default void onTargetDeath(DamageSource damageSource, ItemStack stack, LivingEntity target, LivingEntity attacker) {}

    /**
     * Called when the target is damaged by the user of the item having this ability.
     */
    default void onTargetDamaged(DamageSource source, float amount, ItemStack stack, LivingEntity target, LivingEntity attacker) {}

    List<Text> getTooltipAbilities(ItemStack stack);

    /**
     * Override this when you want to add additional tooltip right after the main tooltip.
     */
    default List<Text> getBonusAbilityTooltip(ItemStack stack) {
        return List.of();
    }

    default List<Text> getLoreTooltips(ItemStack stack) {
        return List.of();
    }

    /**
     * Adds all tooltip abilities listed in {@link #getTooltipAbilities(ItemStack)} to the
     * item tooltip. {@link WeaponUtil} handles the displaying of {@link TooltipAbilities}.
     */
    static void appendTooltipAbilities(List<IAbility> abilities, List<Text> tooltip, ItemStack stack) {
        List<Text> tooltipAbilities = new ArrayList<>();
        List<Text> lore = new ArrayList<>();
        abilities.forEach(ability -> {
            List<Text> setup = new ArrayList<>();
            setup.addAll(ability.getTooltipAbilities(stack));
            setup.addAll(ability.getBonusAbilityTooltip(stack));
            tooltipAbilities.addAll(setup);
        });
        abilities.forEach(ability -> lore.addAll(ability.getLoreTooltips(stack)));
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
