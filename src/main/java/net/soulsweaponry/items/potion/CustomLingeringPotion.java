package net.soulsweaponry.items.potion;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.potion.Potion;
import net.minecraft.text.Text;

import java.util.List;

public class CustomLingeringPotion extends CustomSplashPotion {

    public CustomLingeringPotion(Settings settings, Potion potion, int splashParticleColor) {
        super(settings, potion, splashParticleColor);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent != null) {
            potionContentsComponent.buildTooltip(tooltip::add, 0.25F, context.getUpdateTickRate());
        }
    }
}
