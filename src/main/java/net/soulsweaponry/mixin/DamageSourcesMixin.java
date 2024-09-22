package net.soulsweaponry.mixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.soulsweaponry.entity.projectile.MoonlightArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(DamageSources.class)
public class DamageSourcesMixin {

    @Inject(method = "arrow", at = @At("HEAD"), cancellable = true)
    public void interceptArrowDamageSource(PersistentProjectileEntity source, Entity attacker, CallbackInfoReturnable<DamageSource> info) {
        if (source instanceof MoonlightArrow) {
            DamageSources sources = (DamageSources)(Object)this;
            info.setReturnValue(sources.indirectMagic(source, attacker));
        }
    }
}