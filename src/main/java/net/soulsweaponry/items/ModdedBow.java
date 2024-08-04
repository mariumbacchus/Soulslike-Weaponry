package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class ModdedBow extends BowItem implements IShootModProjectile, IConfigDisable {

    protected final List<WeaponUtil.TooltipAbilities> tooltipAbilities = new ArrayList<>();

    public ModdedBow(Settings settings) {
        super(settings);
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.isDisabled(stack)) {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.disabled"));
        }
        if (Screen.hasShiftDown()) {
            for (WeaponUtil.TooltipAbilities ability : this.getTooltipAbilities()) {
                WeaponUtil.addAbilityTooltip(ability, stack, tooltip);
            }
            tooltip.addAll(Arrays.asList(this.getAdditionalTooltips()));
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    public List<WeaponUtil.TooltipAbilities> getTooltipAbilities() {
        return this.tooltipAbilities;
    }

    public void addTooltipAbility(WeaponUtil.TooltipAbilities... abilities) {
        Collections.addAll(this.tooltipAbilities, abilities);
    }

    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }
}