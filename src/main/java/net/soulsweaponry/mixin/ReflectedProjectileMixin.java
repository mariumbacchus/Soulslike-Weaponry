package net.soulsweaponry.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.soulsweaponry.entity.projectile.noclip.AbsorbedProjectilesOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class ReflectedProjectileMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void soulsweaponry$expireReflectedProjectile(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof ProjectileEntity
                && !entity.getWorld().isClient
                && entity.getCommandTags().contains(AbsorbedProjectilesOrb.REFLECTED_TAG)
                && entity.age >= AbsorbedProjectilesOrb.REFLECTED_LIFETIME) {
            entity.discard();
            ci.cancel();
        }
    }
}