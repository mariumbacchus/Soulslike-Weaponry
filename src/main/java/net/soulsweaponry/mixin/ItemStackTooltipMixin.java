package net.soulsweaponry.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
@Environment(EnvType.CLIENT)
public class ItemStackTooltipMixin {

    /**
     * Recolors this mod's attribute modifiers safely after vanilla builds them.
     */
    @Inject(
            method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void sw$colorMine(
            PlayerEntity player,
            TooltipContext context,
            CallbackInfoReturnable<List<Text>> cir,
            List<Text> list
    ) {
        if (list.isEmpty()) {
            return;
        }
        Text last = list.get(list.size() - 1);
        if (!(last instanceof MutableText mutable)) {
            return;
        }

        String str = last.getString();
        if (str == null) {
            return;
        }
        if (!str.contains("upgrade")) {
            return;
        }
        mutable.formatted(Formatting.AQUA);
    }
}
