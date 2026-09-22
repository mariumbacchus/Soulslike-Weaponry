package net.soulsweaponry.items.bow;

import net.fabric_extras.ranged_weapon.api.CustomBow;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class ModdedBow extends CustomBow implements IHasAbilities {

    /** Ticks a vanilla bow/crossbow pull takes, the baseline {@link RangedConfig#pull_time_bonus()} is added to. */
    public static final float BASELINE_PULL_TIME_TICKS = 20f;
    /** Speed of an arrow shot from a fully drawn vanilla bow, the baseline {@link RangedConfig#velocity_bonus()} is added to. */
    public static final float BASELINE_VELOCITY = 3f;

    protected final List<IAbility> abilities = new ArrayList<>();

    public ModdedBow(Settings settings, RangedConfig config, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, config, repairIngredientSupplier);
        List<Text> list = new ArrayList<>();
        BasicInfoAbility pullSpeedAbility = new BasicInfoAbility(list);
        float diffSeconds = Math.abs(config.pull_time_bonus());
        String pullTimeSeconds = String.format("%.2f", diffSeconds);
        if (config.pull_time_bonus() > 0f) {
            list.add(Text.translatable("tooltip.soulsweapons.slow_pull").formatted(Formatting.RED));
            list.add(Text.translatable("tooltip.soulsweapons.slow_pull.1", pullTimeSeconds).formatted(Formatting.GRAY));
        } else if (config.pull_time_bonus() < 0f) {
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

    /**
     * Keeps this mod's config units — pull time in ticks, velocity as an absolute projectile speed —
     * and converts them to what {@link RangedConfig} takes since Ranged Weapon API 2.x: a pull time
     * bonus in seconds on top of the vanilla pull, and a velocity bonus on top of the weapon type's
     * baseline speed. A velocity at or below the baseline means "no custom velocity", as it did
     * under the old absolute field.
     */
    public static RangedConfig createConfig(int pullTime, float damage, float velocity) {
        return createConfig(pullTime, damage, velocity, BASELINE_VELOCITY);
    }

    public static RangedConfig createConfig(int pullTime, float damage, float velocity, float baselineVelocity) {
        float pullTimeBonus = (pullTime - BASELINE_PULL_TIME_TICKS) / BASELINE_PULL_TIME_TICKS;
        float velocityBonus = Math.max(0f, velocity - baselineVelocity);
        return new RangedConfig(damage, pullTimeBonus, velocityBonus);
    }
}