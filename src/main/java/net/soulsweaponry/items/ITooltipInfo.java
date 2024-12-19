package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public interface ITooltipInfo {

    List<WeaponUtil.TooltipAbilities> getTooltipAbilities();
    Text[] getAdditionalTooltips();
    void addTooltipAbility(WeaponUtil.TooltipAbilities... abilities);

    /**
     * Adds all tooltip abilities listed in {@link #getTooltipAbilities()} and {@link #getAdditionalTooltips()} to the
     * item tooltip. {@link WeaponUtil} handles the displaying of {@link WeaponUtil.TooltipAbilities}.
     */
    default void appendTooltipAbilities(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            for (WeaponUtil.TooltipAbilities ability : this.getTooltipAbilities()) {
                WeaponUtil.addAbilityTooltip(ability, stack, tooltip);
            }
            tooltip.addAll(Arrays.asList(this.getAdditionalTooltips()));
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
    }
}
