package net.soulsweaponry.mixin;

import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.WeaponRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "getDamageBonus", at = @At("TAIL"), cancellable = true)
    private static void interceptGetDamage(ItemStack stack, MobType group, CallbackInfoReturnable<Float> cir) {
        if (stack.is(WeaponRegistry.STING.get()) && group == MobType.ARTHROPOD) {
            float value = cir.getReturnValue() + CommonConfig.STING_BONUS_ARTHROPOD_DAMAGE.get();
            cir.setReturnValue(value);
        }//TODO add trickweapon and master sword
        if (group == MobType.UNDEAD /*&& (stack.getItem() instanceof TrickWeapon && ((TrickWeapon) stack.getItem()).hasUndeadBonus() || stack.is(WeaponRegistry.MASTER_SWORD))*/) {
            float value = cir.getReturnValue() + CommonConfig.RIGHTEOUS_UNDEAD_BONUS_DAMAGE.get() + (float) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
            cir.setReturnValue(value);
        }
    }
}
