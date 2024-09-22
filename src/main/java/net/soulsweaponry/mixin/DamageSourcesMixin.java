package net.soulsweaponry.mixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.soulsweaponry.entity.projectile.MoonlightArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(DamageSource.class)
public class DamageSourcesMixin {

    @Inject(method = "arrow", at = @At("HEAD"), cancellable = true)
    private static void interceptArrowDamageSource(PersistentProjectileEntity source, Entity attacker, CallbackInfoReturnable<DamageSource> info) {
        if (source instanceof MoonlightArrow) {
            info.setReturnValue(DamageSource.magic(source, attacker));
        }
    }
}