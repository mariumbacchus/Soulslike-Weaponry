package net.soulsweaponry.items.abilities;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

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
     * @deprecated This should be avoided since many weapons can't be equipped in offhand with Better Combat installed.
     * Try turning it into a keybind or sneaking ability instead.
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
     * Will not be called if {@link #isSneakAbility()} returns {@code true}.
     * @param world server world
     * @param stack item stack
     * @param player (server) player
     */
    default void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {}

    /**
     * Server side effects when pressing the {@link KeyBindRegistry#keybindAbility} and the player is sneaking.
     * Will only be called if {@link #isSneakAbility()} returns {@code true}.
     * @param world server world
     * @param stack item stack
     * @param player (server) player
     */
    default void sneakingUseKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {}

    /**
     * Server side effects when pressing the {@link KeyBindRegistry#keybindAbility} and the item is in offhand.
     * Remember to override {@link #isOffhandAbility()} to return {@code true} when overriding this.
     * @deprecated This should be avoided since many weapons can't be equipped in offhand with Better Combat installed.
     * Try turning it into a keybind or sneaking ability instead.
     * @param world server world
     * @param stack item stack
     * @param player (server) player
     */
    @Deprecated
    default void offhandUseKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {}

    /**
     * Client side effects when pressing the {@link KeyBindRegistry#keybindAbility}.
     * Will not be called if {@link #isSneakAbility()} returns {@code true}.
     * @param world client world
     * @param stack item stack
     * @param player client player
     */
    default void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {}

    /**
     * Client side effects when pressing the {@link KeyBindRegistry#keybindAbility} and the player is sneaking.
     * Will only be called if {@link #isSneakAbility()} returns {@code true}.
     * @param world client world
     * @param stack item stack
     * @param player client player
     */
    default void sneakingUseKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {}

    /**
     * Client side effects when pressing the {@link KeyBindRegistry#keybindAbility} and the item is in offhand.
     * Remember to override {@link #isOffhandAbility()} to return {@code true} when overriding this.
     * @deprecated This should be avoided since many weapons can't be equipped in offhand with Better Combat installed.
     * Try turning it into a keybind or sneaking ability instead.
     * @param world client world
     * @param stack item stack
     * @param player client player
     */
    @Deprecated
    default void offhandUseKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {}

    /**
     * Called whenever an entity dies and the last damage source was the attacker.
     */
    default void onTargetDeath(DamageSource damageSource, ItemStack stack, LivingEntity target, LivingEntity attacker) {}

    /**
     * Called when the target is damaged by the user of the item having this ability.
     */
    default void onTargetDamaged(DamageSource source, float amount, ItemStack stack, LivingEntity target, LivingEntity attacker) {}

    /**
     * When a target triggers the bleed buildup (taking massive bleed damage), it sends a signal
     * out 20x their bounding box size to other entities to call this method if they hold an
     * {@link IHasAbilities} item.
     * @param stack held ability item stack
     * @param target target that bled
     * @param attacker wielder of the stack
     */
    default void onTargetBleedTrigger(ItemStack stack, LivingEntity target, LivingEntity attacker) {}

    default ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return ActionResult.PASS;
    }

    List<Text> getTooltipAbilities(ItemStack stack);

    /**
     * Override this when you want to add additional tooltip right after the main tooltip.
     */
    default List<Text> getBonusAbilityTooltip(ItemStack stack) {
        return List.of();
    }

    /**
     * Override this when you want to add lore tooltips which are specific to the ability instead
     * of item. If item specific, make the item implement {@link IHasLore} instead.
     */
    default List<Text> getLoreTooltips(ItemStack stack) {
        return List.of();
    }
}
