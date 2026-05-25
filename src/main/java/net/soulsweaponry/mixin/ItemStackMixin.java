package net.soulsweaponry.mixin;

import com.google.common.collect.Multimap;
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

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true)
    private void soulsweapons$modifyAttributes(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if (!(stack.getItem() instanceof IHasAbilities hasAbilities)) {
            return;
        }
        boolean hasCustom = WeaponUtil.hasModifiedAttributes(stack);
        boolean hasUpgrade = WeaponUtil.getUpgradeLevel(stack) > 0;
        if (hasCustom || hasUpgrade) {
            cir.setReturnValue(hasAbilities.modifyAttributeModifiers(cir.getReturnValue(), stack, slot));
        }
    }
}