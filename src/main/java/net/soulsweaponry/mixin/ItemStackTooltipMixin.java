package net.soulsweaponry.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemStack.class)
@Environment(EnvType.CLIENT)
public class ItemStackTooltipMixin {

    /**
     * Makes this mod's custom attributes a different color. This is because a new row of attributes would
     * be added instead of adding to existing lines, like instead of +2 and +3 resulting in a single +5, it would be 2 separate
     * rows of +2 and +3 with the same generic name, so to make the distinction clearer the color is different.
     */
    @ModifyArg(
            method = "appendAttributeModifierTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;"
            ),
            index = 0
    )
    private Formatting sw$colorUpgradeAttributeTooltip(Formatting original, @Local(argsOnly = true) EntityAttributeModifier mod) {
        Identifier id = mod.id();
        return id != null && SoulsWeaponry.ModId.equals(id.getNamespace()) && id.getPath().contains("upgrade") ? Formatting.AQUA : original;
    }
}
