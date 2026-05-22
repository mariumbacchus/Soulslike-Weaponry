package net.soulsweaponry.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.soulsweaponry.entity.projectile.ModPersistentProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {

    /**
     * Redirects the setStuckArrowCount inside {@code PersistentProjectileEntity#onEntityHit}
     * if the projectile doesn't allow it.
     * <p>
     * Would be weird for a moonlight beam to apply arrows visually, huh?
     */
    @Redirect(
            method = "onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;setStuckArrowCount(I)V"
            ),
            require = 0 // Ignore if other mods try to do the same tactic
    )
    private void redirectSetStuckArrowCount(LivingEntity target, int newCount) {
        PersistentProjectileEntity self = (PersistentProjectileEntity)(Object) this;
        if (self instanceof ModPersistentProjectile modPersistentProjectile) {
            if (!modPersistentProjectile.shouldAllowArrowSticking()) {
                return;
            }
        }
        target.setStuckArrowCount(newCount);
    }

    @ModifyVariable(
            method = "onEntityHit",
            at = @At("STORE"),
            ordinal = 0
    )
    private DamageSource soulsweaponry$modifyProjectileDamageSource(DamageSource original, EntityHitResult entityHitResult) {
        PersistentProjectileEntity projectile = (PersistentProjectileEntity)(Object)this;
        if (projectile instanceof ModPersistentProjectile modProjectile) {
            DamageSource customSource = modProjectile.getCustomDamageSource();
            if (customSource != null) {
                return customSource;
            }
        }
        return original;
    }
}
