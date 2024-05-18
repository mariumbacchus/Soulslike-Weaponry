package net.soulsweaponry.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.registry.WeaponRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "getAttackDamage", at = @At("TAIL"), cancellable = true)
    private static void interceptGetDamage(ItemStack stack, EntityGroup group, CallbackInfoReturnable<Float> cir) {
        if (stack.isOf(WeaponRegistry.STING.get()) && group == EntityGroup.ARTHROPOD) {
            float value = cir.getReturnValue() + CommonConfig.STING_BONUS_ARTHROPOD_DAMAGE.get();
            cir.setReturnValue(value);
        }
        if (group == EntityGroup.UNDEAD && (stack.getItem() instanceof TrickWeapon && ((TrickWeapon) stack.getItem()).hasUndeadBonus() || stack.isOf(WeaponRegistry.MASTER_SWORD.get()))) {
            float value = cir.getReturnValue() + CommonConfig.RIGHTEOUS_UNDEAD_BONUS_DAMAGE.get() + (float) EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack);
            cir.setReturnValue(value);
        }
    }
}
