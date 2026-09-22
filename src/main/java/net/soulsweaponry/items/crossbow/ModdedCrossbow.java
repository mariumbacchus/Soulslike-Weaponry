package net.soulsweaponry.items.crossbow;

import net.fabric_extras.ranged_weapon.api.CustomCrossbow;
import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.BasicInfoAbility;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.bow.ModdedBow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class ModdedCrossbow extends CustomCrossbow implements IHasAbilities {

    /** Speed of a bolt shot from a vanilla crossbow, the baseline {@link RangedConfig#velocity_bonus()} is added to. */
    public static final float BASELINE_VELOCITY = 3.15f;

    protected final List<IAbility> abilities = new ArrayList<>();

    public ModdedCrossbow(Settings settings, RangedConfig rangedConfig, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, rangedConfig, repairIngredientSupplier);
        List<Text> list = new ArrayList<>();
        BasicInfoAbility pullSpeedAbility = new BasicInfoAbility(list);
        float diffSeconds = Math.abs(rangedConfig.pull_time_bonus());
        String pullTimeSeconds = String.format("%.2f", diffSeconds);
        if (rangedConfig.pull_time_bonus() > 0f) {
            list.add(Text.translatable("tooltip.soulsweapons.slow_pull").formatted(Formatting.RED));
            list.add(Text.translatable("tooltip.soulsweapons.slow_pull.1", pullTimeSeconds).formatted(Formatting.GRAY));
        } else if (rangedConfig.pull_time_bonus() < 0f) {
            list.add(Text.translatable("tooltip.soulsweapons.fast_pull").formatted(Formatting.WHITE));
            list.add(Text.translatable("tooltip.soulsweapons.fast_pull.1", pullTimeSeconds).formatted(Formatting.GRAY));
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

    /** As {@link ModdedBow#createConfig(int, float, float)}, but against the crossbow's faster baseline speed. */
    public static RangedConfig createConfig(int pullTime, float damage, float velocity) {
        return ModdedBow.createConfig(pullTime, damage, velocity, BASELINE_VELOCITY);
    }
}
