package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Featherlight extends UltraHeavyWeapon {

    public Featherlight(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.featherlight_damage, - (4f - ConfigConstructor.featherlight_attack_speed), settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.featherlight").formatted(Formatting.LIGHT_PURPLE));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.featherlight_description_1").formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.featherlight_description_2").formatted(Formatting.GRAY));
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
