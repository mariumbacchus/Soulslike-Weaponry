package net.soulsweaponry.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ITooltipInfo;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.TooltipUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ShieldItem.class)
public class ShieldItemMixin {

    @Inject(method = "appendTooltip", at = @At("TAIL"))
    protected void interceptTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo info) {
        if (ConfigConstructor.enable_shield_parry) {
            if (ITooltipInfo.shouldShowInfo()) {
                TooltipUtil.addAbilityTooltip(TooltipAbilities.PARRY, stack, tooltip);
            } else {
                ITooltipInfo.addShowInfoText(tooltip);
            }
        }
    }
}