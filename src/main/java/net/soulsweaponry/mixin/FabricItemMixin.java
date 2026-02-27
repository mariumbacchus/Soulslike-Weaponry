package net.soulsweaponry.mixin;

import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.util.WeaponUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FabricItem.class)
public class FabricItemMixin {

    @Inject(method = "getAttributeModifiers", at = @At("HEAD"), cancellable = true)
    public void modifyAttributeModifiers(ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> info) {
        if (WeaponUtil.hasModifiedAttributes(stack) && stack.getItem() instanceof IHasAbilities hasAbilities) {
            info.setReturnValue(hasAbilities.modifyAttributeModifiers(info.getReturnValue(), stack, slot));
        }
    }
}
