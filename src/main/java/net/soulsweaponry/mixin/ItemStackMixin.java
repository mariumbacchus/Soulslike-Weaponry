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

    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true) //TODO this needs to be tested THOROUGHLY
    private void soulsweapons$modifyAttributes(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if (WeaponUtil.hasModifiedAttributes(stack) && stack.getItem() instanceof IHasAbilities hasAbilities) {
            cir.setReturnValue(hasAbilities.modifyAttributeModifiers(cir.getReturnValue(), stack, slot));
        }
    }
}
