package net.soulsweaponry.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.soulsweaponry.util.UpgradeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.UUID;

@Mixin(ItemStack.class)
public abstract class ItemStackTooltipMixin {

    @ModifyArg(
            method = "getTooltip",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;",
                    ordinal = 1
            ),
            index = 0,
            require = 0
    )
    private Formatting soulsweapons$recolorEqualsAttribute(Formatting original, @Local EntityAttributeModifier modifier) {
        return soulsweapons$getUpgradeFormatting(original, modifier);
    }

    @ModifyArg(
            method = "getTooltip",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;",
                    ordinal = 2
            ),
            index = 0
    )
    private Formatting soulsweapons$recolorPositiveAttribute(Formatting original, @Local EntityAttributeModifier modifier) {
        return soulsweapons$getUpgradeFormatting(original, modifier);
    }

    @Unique
    private static Formatting soulsweapons$getUpgradeFormatting(Formatting original, EntityAttributeModifier modifier) {
        return soulsweapons$isUpgradeModifier(modifier.getId()) ? Formatting.AQUA : original;
    }

    @Unique
    private static boolean soulsweapons$isUpgradeModifier(UUID id) {
        return id.equals(UpgradeUtil.UPGRADE_DAMAGE)
                || id.equals(UpgradeUtil.UPGRADE_ATTACK_SPEED)
                || UpgradeUtil.UPGRADE_ARMOR_UUIDS.containsValue(id)
                || UpgradeUtil.UPGRADE_ARMOR_TOUGHNESS_UUIDS.containsValue(id)
                || id.equals(UpgradeUtil.UPGRADE_RANGED_DAMAGE)
                || id.equals(UpgradeUtil.UPGRADE_RANGED_HASTE);
    }
}