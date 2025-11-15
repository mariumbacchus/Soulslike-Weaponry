package net.soulsweaponry.items.bow;

import net.fabric_extras.ranged_weapon.api.CustomBow;
import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.BasicInfoAbility;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class ModdedBow extends CustomBow implements IHasAbilities {

    protected final List<IAbility> abilities = new ArrayList<>();

    public ModdedBow(Settings settings, RangedConfig config, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, config, repairIngredientSupplier);
        List<Text> list = new ArrayList<>();
        BasicInfoAbility pullSpeedAbility = new BasicInfoAbility(list);
        String pullBonus = String.format("%.2f", config.pull_time_bonus());
        if (config.pull_time_bonus() > 0) {
            list.add(Text.translatable("tooltip.soulsweapons.slow_pull").formatted(Formatting.RED));
            list.add(Text.translatable("tooltip.soulsweapons.slow_pull.1", pullBonus).formatted(Formatting.GRAY));
        } else if (config.pull_time_bonus() < 0) {
            list.add(Text.translatable("tooltip.soulsweapons.fast_pull").formatted(Formatting.WHITE));
            list.add(Text.translatable("tooltip.soulsweapons.fast_pull.1", pullBonus).formatted(Formatting.GRAY));
        }
        this.addAbility(pullSpeedAbility);
    }

    @Override
    public List<IAbility> getAbilities() {
        return this.abilities;
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
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean vanilla = super.postHit(stack, target, attacker);
        boolean abilities = IHasAbilities.super.postHit(stack, target, attacker);
        return vanilla || abilities;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        IHasAbilities.super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        this.appendTooltipAbilities(tooltip, stack);
        super.appendTooltip(stack, context, tooltip, type);
    }

    public static RangedConfig createConfig(int pullTime, float damage, float bonusVelocity) {
        return new RangedConfig(damage, (float) (pullTime - BowItem.TICKS_PER_SECOND) / BowItem.TICKS_PER_SECOND, bonusVelocity);
    }
}
