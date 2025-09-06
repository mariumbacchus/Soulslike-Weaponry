package net.soulsweaponry.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.soulsweaponry.items.abilities.IAbility;
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
        if (!this.isDisabled(stack)) {
            this.getAbilities().forEach(a -> a.postHit(stack, target, attacker));
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if (this.isDisabled(ItemStack.EMPTY)) {
            return 0f;
        }
        return (float) this.getAbilities().stream().mapToDouble(a -> a.getBonusAttackDamage(target, baseAttackDamage, damageSource)).sum();
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