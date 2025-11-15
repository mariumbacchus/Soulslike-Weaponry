package net.soulsweaponry.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.soulsweaponry.config.ConfigConstructor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ShieldItem.class)
public class ShieldItemMixin {

    @Inject(method = "appendTooltip", at = @At("TAIL"))
    protected void interceptTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo info) {
        if (ConfigConstructor.enable_shield_parry) {
            /*if (ITooltipInfo.shouldShowInfo()) { TODOO
                TooltipUtil.addAbilityTooltip(TooltipAbilities.PARRY, stack, tooltip);
            } else {
                //ITooltipInfo.addShowInfoText(tooltip);
            }*/
        }
    }
}
