package net.soulsweaponry.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.ICooldownItem;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.util.TooltipAbilities;

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
        boolean vanilla = super.postHit(stack, target, attacker);
        boolean abilities = IHasAbilities.super.postHit(stack, target, attacker);
        return vanilla || abilities;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return IHasAbilities.super.use(world, user, hand);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        IHasAbilities.super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return IHasAbilities.super.getBonusAttackDamage(target, baseAttackDamage, damageSource);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return IHasAbilities.super.getUseAction(stack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return IHasAbilities.super.getMaxUseTime(stack, user);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        IHasAbilities.super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return IHasAbilities.super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return IHasAbilities.super.finishUsing(stack, world, user);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        IHasAbilities.super.usageTick(world, user, stack, remainingUseTicks);
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
        this.appendTooltipAbilities(tooltip, stack);

        this.appendTooltipAbilities(stack, context, tooltip, type);//TODO remove when removing ITooltipInfo
        super.appendTooltip(stack, context, tooltip, type);
    }
}