package net.soulsweaponry.mixin;

import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.soulsweaponry.items.abilities.IHasAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CrossbowItem.class)
public abstract class CrossbowMixin implements IHasAbilities {

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    public void interceptAppendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo info) {
        this.appendTooltipAbilities(stack, tooltip);
    }
}
