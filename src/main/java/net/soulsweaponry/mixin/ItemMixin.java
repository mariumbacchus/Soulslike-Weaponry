package net.soulsweaponry.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.TooltipUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    public void interceptAppendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo info) {
        TooltipUtil.addAbilityTooltip(TooltipAbilities.TRICK_WEAPON, stack, tooltip);
        int lvl = stack.getOrDefault(ComponentRegistry.ITEM_UPGRADE_LEVEL, 0);
        if (lvl > 0) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.level", lvl).formatted(Formatting.DARK_GRAY));
        }
    }
}
