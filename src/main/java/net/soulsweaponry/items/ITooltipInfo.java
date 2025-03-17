package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.TooltipUtil;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public interface ITooltipInfo {

    List<TooltipAbilities> getTooltipAbilities();
    Text[] getAdditionalTooltips();
    void addTooltipAbility(TooltipAbilities... abilities);

    /**
     * Adds all tooltip abilities listed in {@link #getTooltipAbilities()} and {@link #getAdditionalTooltips()} to the
     * item tooltip. {@link WeaponUtil} handles the displaying of {@link TooltipAbilities}.
     */
    default void appendTooltipAbilities(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        boolean epicFight = WeaponUtil.isModLoaded("epicfight");
        if (shouldShowInfo()) {
            for (TooltipAbilities ability : this.getTooltipAbilities()) {
                TooltipUtil.addAbilityTooltip(ability, stack, tooltip);
            }
            tooltip.addAll(Arrays.asList(this.getAdditionalTooltips()));
        } else {
            if (epicFight) {
                tooltip.add(Text.translatable("tooltip.soulsweapons.alt"));
            } else {
                tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
            }
        }
    }

    /**
     * @return whether the info button is being held when hovering an item, button is
     * ALT if Epic Fight mod is installed, SHIFT otherwise.
     */
    static boolean shouldShowInfo() {
        boolean epicFight = WeaponUtil.isModLoaded("epicfight");
        return !epicFight ? Screen.hasShiftDown() : Screen.hasAltDown();
    }
}