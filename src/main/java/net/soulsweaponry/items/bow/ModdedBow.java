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

    protected final List<IAbility> abilities = new ArrayList<>();

    public ModdedBow(Settings settings, RangedConfig config, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, repairIngredientSupplier);
        this.configure(config);
        List<Text> list = new ArrayList<>();
        BasicInfoAbility pullSpeedAbility = new BasicInfoAbility(list);
        float diffSeconds = Math.abs(config.pull_time() - 20f) / 20f;
        String pullTimeSeconds = String.format("%.2f", diffSeconds);
        if (config.pull_time() > 20f) {
            list.add(Text.translatable("tooltip.soulsweapons.slow_pull").formatted(Formatting.RED));
            list.add(Text.translatable("tooltip.soulsweapons.slow_pull.1", pullTimeSeconds).formatted(Formatting.GRAY));
        } else if (config.pull_time() < 20f) {
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

    public static RangedConfig createConfig(int pullTime, float damage, float bonusVelocity) {
        return new RangedConfig(pullTime, damage, bonusVelocity);
    }
}