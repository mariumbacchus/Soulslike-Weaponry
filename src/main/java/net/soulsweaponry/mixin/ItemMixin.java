package net.soulsweaponry.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Covers ability implementation for all items.
 * <p>
 * Some items override methods from Item class, like SwordItem with postHit method, therefore
 * some additional mixins are made to ensure abilities are still called. RangedWeaponItem
 * for example also overrides onStoppedUsing and use methods, so abilities aren't called there.
 * <p>
 * This is the case for items like
 * <li> {@link net.minecraft.item.MiningToolItem} </li>
 * <li> {@link net.minecraft.item.MaceItem} </li>
 * <li> {@link net.minecraft.item.TridentItem} </li>
 * <li> {@link net.minecraft.item.RangedWeaponItem} </li>
 * <li> {@link net.minecraft.item.BowItem} </li>
 * <li> {@link net.minecraft.item.CrossbowItem} </li>
 * <li> and more... so keep that in mind! </li>
 * <p>
 * So far, only these mixins are made to address this problem, since some items with abilities extending
 * those classes are being used. There are no items nor abilities extending MaceItem so no mixin is made
 * for that yet.
 * <li> {@link MiningToolItemMixin} (postHit) </li>
 * <li> {@link SwordItemMixin} (postHit) </li>
 */
@Mixin(Item.class)
public class ItemMixin implements IHasAbilities {

    @Unique
    protected final List<IAbility> abilities = new ArrayList<>();

    @Override
    public List<IAbility> getAbilities() {
        return this.abilities;
    }

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    public void interceptAppendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo info) {
        this.appendTooltipAbilities(stack, tooltip);
    }

    @Inject(method = "postHit", at = @At("RETURN"), cancellable = true)
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> info) {
        if (this.getAbilities().isEmpty()) {
            return;
        }
        boolean vanilla = info.getReturnValue();
        boolean abilities = IHasAbilities.super.postHit(stack, target, attacker);
        info.setReturnValue(vanilla || abilities);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
        if (this.getAbilities().isEmpty()) {
            return;
        }
        info.setReturnValue(IHasAbilities.super.use(world, user, hand));
    }

    @Inject(method = "onStoppedUsing", at = @At("HEAD"))
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {
        if (this.getAbilities().isEmpty()) {
            return;
        }
        IHasAbilities.super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Inject(method = "getBonusAttackDamage", at = @At("HEAD"), cancellable = true)
    public void getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource, CallbackInfoReturnable<Float> info) {
        if (this.getAbilities().isEmpty()) {
            return;
        }
        info.setReturnValue(IHasAbilities.super.getBonusAttackDamage(target, baseAttackDamage, damageSource));
    }

    @Inject(method = "getUseAction", at = @At("HEAD"), cancellable = true)
    public void getUseAction(ItemStack stack, CallbackInfoReturnable<UseAction> info) {
        if (this.getAbilities().isEmpty()) {
            return;
        }
        info.setReturnValue(IHasAbilities.super.getUseAction(stack));
    }

    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    public void getMaxUseTime(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> info) {
        if (this.getAbilities().isEmpty()) {
            return;
        }
        info.setReturnValue(IHasAbilities.super.getMaxUseTime(stack, user));
    }

    @Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
    public void useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        if (this.getAbilities().isEmpty()) {
            return;
        }
        info.setReturnValue(IHasAbilities.super.useOnEntity(stack, user, entity, hand));
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo info) {
        if (this.getAbilities().isEmpty()) {
            return;
        }
        IHasAbilities.super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Inject(method = "finishUsing", at = @At("HEAD"))
    public void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        if (this.getAbilities().isEmpty()) {
            return;
        }
        IHasAbilities.super.finishUsing(stack, world, user);
    }

    @Inject(method = "usageTick", at = @At("HEAD"))
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo info) {
        if (this.getAbilities().isEmpty()) {
            return;
        }
        IHasAbilities.super.usageTick(world, user, stack, remainingUseTicks);
    }

    // Meant for custom items so just ignore
    @Override
    public boolean isDisabled(ItemStack stack) {
        return false;
    }
}
