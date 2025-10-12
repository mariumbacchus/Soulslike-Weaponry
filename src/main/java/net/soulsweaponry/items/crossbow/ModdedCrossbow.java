package net.soulsweaponry.items.crossbow;

import net.fabric_extras.ranged_weapon.api.CustomCrossbow;
import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.abilities.ICooldownItem;
import net.soulsweaponry.items.bow.ModdedBow;
import net.soulsweaponry.util.TooltipAbilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class ModdedCrossbow extends CustomCrossbow implements IConfigDisable, IShootModProjectile, ICooldownItem, ITooltipInfo {

    protected final List<TooltipAbilities> tooltipAbilities = new ArrayList<>();

    public ModdedCrossbow(Settings settings, RangedConfig rangedConfig, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, rangedConfig, repairIngredientSupplier);
    }

    @Override
    public Predicate<ItemStack> getHeldProjectiles() {
        return BOW_PROJECTILES;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this.isDisabled(user.getStackInHand(hand))) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (this.isDisabled(stack)) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        this.appendTooltipAbilities(stack, context, tooltip, type);
        super.appendTooltip(stack, context, tooltip, type);
    }

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

    public static RangedConfig createConfig(int pullTime, float damage, float bonusVelocity) {
        return ModdedBow.createConfig(pullTime, damage, bonusVelocity);
    }
}