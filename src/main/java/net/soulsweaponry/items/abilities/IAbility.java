package net.soulsweaponry.items.abilities;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.predicate.EssenceNeeded;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IAbility extends ICooldownItem {

    /**
     * Called when the player readies his main hand (scrolling to/selecting the slot the item is in)
     */
    default void onMainHandEquip(PlayerEntity player, ItemStack stack) {}

    /**
     * Called when equipping/inserting the item in a slot, such as putting it in the armor slot or switching from
     * offhand to main-hand. This is NOT called when scrolling to/selecting the item and readying it
     * in the main-hand, {@link #onMainHandEquip(PlayerEntity, ItemStack)} is called instead.
     * @param entity owner of the items
     * @param slot equipment slot inserted in
     * @param oldStack old stack that was in the slot
     * @param newStack the new item inserted in the slot (which calls this method)
     */
    default void onEquipStack(LivingEntity entity, EquipmentSlot slot, ItemStack oldStack, ItemStack newStack) {}

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
     * @param ticksUsed ticks used, this automatically calls {@link WeaponUtil#getChargeTime(ItemStack, LivingEntity, int)}
     *                          to get accurate ticks based on mods installed (epic fight mod messes things up for example), so no need
     *                          to call it again
     */
    default void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {}

    /**
     * Called when {@link #isChargeToUse()} returns {@code true} and the user is sneaking.
     * Remember to override {@link #isSneakAbility()} to return {@code true} when overriding this.
     * @param stack itemstack used
     * @param world world
     * @param user user wielding the stack
     * @param ticksUsed ticks used, this automatically calls {@link WeaponUtil#getChargeTime(ItemStack, LivingEntity, int)}
     *                          to get accurate ticks based on mods installed (epic fight mod messes things up for example), so no need
     *                          to call it again
     */
    default void sneakingOnStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {}

    /**
     * Called when {@link #isChargeToUse()} returns {@code true} and only if the item is in offhand.
     * Remember to override {@link #isOffhandAbility()} to return {@code true} when overriding this.
     * @deprecated This should be avoided since many weapons can't be equipped in offhand with Better Combat installed.
     * Try turning it into a keybind or sneaking ability instead.
     * @param stack itemstack used
     * @param world world
     * @param user user wielding the stack
     * @param ticksUsed ticks used, this automatically calls {@link WeaponUtil#getChargeTime(ItemStack, LivingEntity, int)}
     *                          to get accurate ticks based on mods installed (epic fight mod messes things up for example), so no need
     *                          to call it again
     */
    @Deprecated
    default void offhandOnStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {}

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
     * @deprecated This should be avoided since many weapons can't be equipped in offhand with Better Combat installed.
     * Try turning it into a keybind or sneaking ability instead.
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
     * If true, all "sneaking" methods will be called instead of the regular ones, such as {@link #use(World, PlayerEntity, Hand, ItemStack)}
     * and {@link #onStoppedUsing(ItemStack, World, LivingEntity, int)}.
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
     * Called when the user is damaged when this item is equipped.
     * @return whether the target should take damage in the end or not
     */
    default boolean onUserDamaged(DamageSource source, float amount, ItemStack stack, LivingEntity user) {
        return true;
    }

    /**
     * Called whenever the user dies, this means the user is already dead and
     * cannot be revived, use {@link #onUserDamaged(DamageSource, float, ItemStack, LivingEntity, LivingEntity)}
     * if you want to revive the user.
     */
    default void onUserDeath(DamageSource damageSource, ItemStack stack, LivingEntity user, LivingEntity attacker) {}

    /**
     * Called every tick the item is in the users inventory.
     */
    default void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {}

    /**
     * Server side effects when pressing the {@link KeyBindRegistry#keybindAbility}.
     * Will not be called if {@link #isSneakAbility()} returns {@code true}.
     * @param world server world
     * @param stack item stack
     * @param player (server) player
     * @param hand hand, might be null if the method was called from for example an armor slot
     */
    default void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {}

    /**
     * Server side effects when pressing the {@link KeyBindRegistry#keybindAbility} and the player is sneaking.
     * Will only be called if {@link #isSneakAbility()} returns {@code true}.
     * @param world server world
     * @param stack item stack
     * @param player (server) player
     * @param hand hand, might be null if the method was called from for example an armor slot
     */
    default void sneakingUseKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {}

    /**
     * Server side effects when pressing the {@link KeyBindRegistry#keybindAbility} and the item is in offhand.
     * Remember to override {@link #isOffhandAbility()} to return {@code true} when overriding this.
     * @deprecated This should be avoided since many weapons can't be equipped in offhand with Better Combat installed.
     * Try turning it into a keybind or sneaking ability instead.
     * @param world server world
     * @param stack item stack
     * @param player (server) player
     * @param hand hand, might be null if the method was called from for example an armor slot
     */
    @Deprecated
    default void offhandUseKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {}

    /**
     * Client side effects when pressing the {@link KeyBindRegistry#keybindAbility}.
     * Will not be called if {@link #isSneakAbility()} returns {@code true}.
     * @param world client world
     * @param stack item stack
     * @param player client player
     * @param hand hand, might be null if the method was called from for example an armor slot
     */
    default void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {}

    /**
     * Client side effects when pressing the {@link KeyBindRegistry#keybindAbility} and the player is sneaking.
     * Will only be called if {@link #isSneakAbility()} returns {@code true}.
     * @param world client world
     * @param stack item stack
     * @param player client player
     * @param hand hand, might be null if the method was called from for example an armor slot
     */
    default void sneakingUseKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {}

    /**
     * Client side effects when pressing the {@link KeyBindRegistry#keybindAbility} and the item is in offhand.
     * Remember to override {@link #isOffhandAbility()} to return {@code true} when overriding this.
     * @deprecated This should be avoided since many weapons can't be equipped in offhand with Better Combat installed.
     * Try turning it into a keybind or sneaking ability instead.
     * @param world client world
     * @param stack item stack
     * @param player client player
     * @param hand hand, might be null if the method was called from for example an armor slot
     */
    @Deprecated
    default void offhandUseKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {}

    /**
     * Called whenever an entity dies and the last damage source was the attacker,
     * this means the target is already dead and cannot be revived,
     * use {@link #onTargetDamaged(DamageSource, float, ItemStack, LivingEntity)}
     * if you want to revive the target.
     */
    default void onTargetDeath(DamageSource damageSource, ItemStack stack, LivingEntity target, LivingEntity attacker) {}

    /**
     * Called when the target is damaged by the user of the item having this ability.
     * @return whether the target should take damage in the end or not
     */
    default boolean onTargetDamaged(DamageSource source, float amount, ItemStack stack, LivingEntity target) {
        return true;
    }

    /**
     * When a target triggers the bleed buildup (taking massive bleed damage), it sends a signal
     * out 20x their bounding box size to other entities to call this method if they hold an
     * {@link IHasAbilities} item.
     * @param stack held ability item stack
     * @param target target that bled
     * @param attacker wielder of the stack
     */
    default void onTargetBleedTrigger(ItemStack stack, LivingEntity target, LivingEntity attacker) {}

    /**
     * Called when the user interacts with an entity with the item with this ability.
     */
    default ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return ActionResult.PASS;
    }

    /**
     * Called when the user finishes using the item.
     */
    default ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return stack;
    }

    /**
     * If true, <b>prevents</b> any usage ability call, namely the calls:
     * <li> {@link IHasAbilities#use(World, PlayerEntity, Hand)}
     * <li> {@link IHasAbilities#onStoppedUsing(ItemStack, World, LivingEntity, int)}
     * <li> {@link IHasAbilities#inventoryTick(ItemStack, World, Entity, int, boolean)}
     * <li> {@link IHasAbilities#useKeybindAbilityClient(ClientWorld, ItemStack, PlayerEntity, Hand)}
     * <li> {@link IHasAbilities#useKeybindAbilityServer(ServerWorld, ItemStack, PlayerEntity, Hand)}
     * <li> {@link IHasAbilities#useOnEntity(ItemStack, PlayerEntity, LivingEntity, Hand)}
     * <p>
     * Can be overwritten to for example prevent use() call if {@link EssenceNeeded}
     * returns insufficient essence amount, so the ability can't be used.
     */
    default boolean preventUsePredicate(ItemStack stack, PlayerEntity user) {
        return false;
    }

    /**
     * Called each tick when the player uses the item.
     * @param world world
     * @param user living entity user
     * @param stack item stack
     * @param remainingUseTicks remaining use ticks, this automatically calls {@link WeaponUtil#getChargeTime(ItemStack, LivingEntity, int)}
     *                          to get accurate ticks based on mods installed (epic fight mod messes things up for example), so no need
     *                          to call it again
     */
    default void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {}

    /**
     * Called each tick when sneaking and {@link #isSneakAbility()} returns true.
     * @param world world
     * @param user living entity user
     * @param stack item stack
     * @param remainingUseTicks remaining use ticks, this automatically calls {@link WeaponUtil#getChargeTime(ItemStack, LivingEntity, int)}
     *                          to get accurate ticks based on mods installed (epic fight mod messes things up for example), so no need
     *                          to call it again
     */
    default void sneakingUsageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {}

    /**
     * Called each tick when in offhand and {@link #isOffhandAbility()} returns true.
     * @deprecated Avoid calling this due to Better Combat not allowing certain items to be in offhand.
     * @param world world
     * @param user living entity user
     * @param stack item stack
     * @param remainingUseTicks remaining use ticks, this automatically calls {@link WeaponUtil#getChargeTime(ItemStack, LivingEntity, int)}
     *                          to get accurate ticks based on mods installed (epic fight mod messes things up for example), so no need
     *                          to call it again
     */
    @Deprecated
    default void offhandUsageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {}

    /**
     * Called when the player presses the attack button (usually left click).
     * The hand will always be the main hand (unless an obscure mod changes this).
     * @param world client world
     * @param stack stack
     * @param player client player
     */
    default void onAttackClickClient(ClientWorld world, ItemStack stack, PlayerEntity player) {}

    /**
     * Called when the player presses the attack button (usually left click).
     * The hand will always be the main hand (unless an obscure mod changes this).
     * @param world server world
     * @param stack stack
     * @param player server player
     */
    default void onAttackClickServer(ServerWorld world, ItemStack stack, PlayerEntity player) {}

    /**
     * Called at the end of the {@link LivingEntity#damage(DamageSource, float)} method.
     * Use to modify the users damage taken, either increase, decrease or nullify.
     * @return the modified {@param damageTaken} value
     */
    default float modifyUserDamageTaken(LivingEntity user, float damageTaken, DamageSource source, ItemStack stack, Hand hand) {
        return damageTaken;
    }

    /**
     * Called at the end of the {@link LivingEntity#damage(DamageSource, float)} method.
     * Use to modify the targets damage taken, either increase, decrease or nullify.
     * @return the modified {@param damageTaken} value
     */
    default float modifyTargetDamageTaken(LivingEntity target, float damageTaken, DamageSource source, ItemStack stack, LivingEntity attacker, Hand hand) {
        return damageTaken;
    }

    /**
     * Override to give a custom max ues time. Returns -1 by default, meaning the item will
     * default to 72000 ticks if the item has any charge to use ability, else 0.
     * This is calculated inside {@link IHasAbilities}
     */
    default int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return -1;
    }

    /**
     * What animation to show when holding the item.
     */
    default UseAction getUseAction() {
        return UseAction.NONE;
    }

    /**
     * Determine the use action priority, highest wins.
     */
    default int useActionPriority() {
        return 0;
    }

    /**
     * Used in armor items only.
     * @param builder the builder to add attributes to, should have vanilla components before passing in
     * @param equipmentSlot equipment slot the armor item is meant for
     * @param attributeModifierSlot attribute modifier slot the item will apply to
     */
    default void addArmorAttributeModifiers(AttributeModifiersComponent.Builder builder, EquipmentSlot equipmentSlot, AttributeModifierSlot attributeModifierSlot) {}

    /**
     * @return set of status effects the wielder cannot gain at all (trying to apply returns false and fails)
     */
    default Set<RegistryEntry<StatusEffect>> getStatusEffectsImmuneTo() {
        return Set.of();
    }

    /**
     * Called when {@link LivingEntity#canHaveStatusEffect(StatusEffectInstance)} returns false because the
     * effect was inside the {@link #getStatusEffectsImmuneTo()} list.
     * @param entity entity that got the effect
     * @param declinedEffectInstance the declined status effect instance
     */
    default void onStatusEffectDeclined(LivingEntity entity, StatusEffectInstance declinedEffectInstance, ItemStack stack) {}

    List<Text> getTooltipAbilities(ItemStack stack);

    /**
     * Override this when you want to add additional tooltip right after the main tooltip.
     */
    default List<Text> getBonusAbilityTooltip(ItemStack stack) {
        return List.of();
    }

    /**
     * Override this when you want to add lore tooltips which are specific to the ability instead
     * of item. If item specific, override {@link IHasAbilities#getItemLore()} on the item instead.
     */
    default List<Text> getLoreTooltips(ItemStack stack) {
        return List.of();
    }

    /**
     * Notify the player that it is out of range for the ability to trigger if config value allows it.
     */
    default void notifyRange(PlayerEntity player) {
        if (ConfigConstructor.inform_player_about_out_of_range) {
            player.sendMessage(Text.translatable("soulsweapons.weapon.out_of_range"), true);
        }
    }

    default Text getLocalizedEffectInstanceNames(List<StatusEffectInstance> effects) {
        if (effects == null || effects.isEmpty()) {
            return Text.empty();
        }
        var entries = effects.stream()
                .map(StatusEffectInstance::getEffectType)
                .toList();
        return getLocalizedEffectNames(entries);
    }

    default Text getLocalizedEffectNames(Collection<RegistryEntry<StatusEffect>> effects) {
        if (effects == null || effects.isEmpty()) {
            return Text.empty();
        }
        var effectNames = effects.stream()
                .map(eff -> Text.translatable(eff.value().getTranslationKey())
                        .formatted(Formatting.DARK_GREEN))
                .toList();
        return Texts.join(effectNames, Text.literal(", ").formatted(Formatting.GRAY));
    }
}
