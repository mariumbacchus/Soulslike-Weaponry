package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class SkywardStrikes extends ShootMoonlight {

    public SkywardStrikes(int projectileAmount, float bonusProjectilesPerLvl, float speed, float damage, float bonusDamagePerLvl) {
        super(projectileAmount, bonusProjectilesPerLvl, speed, damage, bonusDamagePerLvl);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>(super.getTooltipAbilities(stack));
        tooltip.set(0, Text.translatable("tooltip.soulsweapons.skyward_strikes").formatted(Formatting.AQUA));
        return tooltip;
    }
}
