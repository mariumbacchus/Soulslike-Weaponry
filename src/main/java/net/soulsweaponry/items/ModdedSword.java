package net.soulsweaponry.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ModdedSword extends SwordItem implements IConfigDisable, ICooldownItem, ITooltipInfo, IHasAbilities {

    protected final float attackSpeed;
    protected final List<TooltipAbilities> tooltipAbilities = new ArrayList<>(); // TODO this can be merged into IAbility with own method calls in that child class, replace list with ability list instead
    protected final List<IAbility> abilities = new ArrayList<>();
    private final float attackDamage;

    public ModdedSword(ToolMaterial toolMaterial, int attackDamage, float ingameAttackSpeed, Settings settings) {
        super(toolMaterial, settings.attributeModifiers(SwordItem.createAttributeModifiers(toolMaterial, attackDamage, - (4f - ingameAttackSpeed))));
        this.attackSpeed = - (4f - ingameAttackSpeed);
        this.attackDamage = attackDamage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public List<IAbility> getAbilities() {
        return this.abilities;
    }

    @Override
    public void addAbility(IAbility... abilities) {
        Collections.addAll(this.abilities, abilities);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            this.getAbilities().forEach(a -> a.postHit(stack, target, attacker));
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
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

            boolean sawSuccess = false;
            boolean sawConsume = false;
            boolean sawConsumePartial = false;
            boolean sawSuccessNoItemUsed = false;
            boolean sawFail = false;

            for (var a : this.getAbilities()) {
                TypedActionResult<ItemStack> r;
                if (sneaking) {
                    r = a.sneakingUse(world, user, hand, out);
                } else if (hand == Hand.OFF_HAND) {
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

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        boolean sneaking = user.isSneaking();
        this.getAbilities().forEach(a -> {
            if (sneaking) {
                a.sneakingOnStoppedUsing(stack, world, user, WeaponUtil.getChargeTime(stack, user, remainingUseTicks));
            } else if (user.getOffHandStack().isOf(this)) {
                a.offhandOnStoppedUsing(stack, world, user, WeaponUtil.getChargeTime(stack, user, remainingUseTicks));
            } else {
                a.onStoppedUsing(stack, world, user, WeaponUtil.getChargeTime(stack, user, remainingUseTicks));
            }
        });
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if (this.isDisabled(ItemStack.EMPTY)) {
            return 0f;
        }
        return (float) this.getAbilities().stream().mapToDouble(a -> a.getBonusAttackDamage(target, baseAttackDamage, damageSource)).sum();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return this.hasChargeToUseAbility() ? UseAction.SPEAR : super.getUseAction(stack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return this.hasChargeToUseAbility() ? 72000 : super.getMaxUseTime(stack, user);
    }

    // TODO everything under needs to be changed/merged with IAbility

    @Override
    public List<TooltipAbilities> getTooltipAbilities() {
        return this.tooltipAbilities;
    }

    @Override
    public void addTooltipAbility(TooltipAbilities... abilities) {
        Collections.addAll(this.tooltipAbilities, abilities);
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (this.isDisabled(stack)) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        IAbility.appendTooltipAbilities(this.getAbilities(), tooltip, stack);
        this.appendTooltipAbilities(stack, context, tooltip, type);//TODO remove when removing ITooltipInfo
        super.appendTooltip(stack, context, tooltip, type);
    }
}