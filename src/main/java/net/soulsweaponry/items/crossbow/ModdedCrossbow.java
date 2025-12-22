package net.soulsweaponry.items.crossbow;

import net.fabric_extras.ranged_weapon.api.CustomCrossbow;
import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.BasicInfoAbility;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.bow.ModdedBow;

import java.util.ArrayList;
import java.util.List;

public abstract class ModdedCrossbow extends CustomCrossbow implements IHasAbilities {

    protected final List<IAbility> abilities = new ArrayList<>();

    public ModdedCrossbow(Settings settings, RangedConfig rangedConfig, TagKey<Item> repairTag) {
        super(settings, rangedConfig, repairTag);
        List<Text> list = new ArrayList<>();
        BasicInfoAbility pullSpeedAbility = new BasicInfoAbility(list);
        String pullBonus = String.format("%.2f", rangedConfig.pull_time_bonus());
        if (rangedConfig.pull_time_bonus() > 0) {
            list.add(Text.translatable("tooltip.soulsweapons.slow_pull").formatted(Formatting.RED));
            list.add(Text.translatable("tooltip.soulsweapons.slow_pull.1", pullBonus).formatted(Formatting.GRAY));
        } else if (rangedConfig.pull_time_bonus() < 0) {
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
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (this.isDisabled(user.getStackInHand(hand))) {
            this.notifyDisabled(user);
            return ActionResult.FAIL;
        }
        return super.use(world, user, hand);
    }

    public static RangedConfig createConfig(int pullTime, float damage, float bonusVelocity) {
        return ModdedBow.createConfig(pullTime, damage, bonusVelocity);
    }
}