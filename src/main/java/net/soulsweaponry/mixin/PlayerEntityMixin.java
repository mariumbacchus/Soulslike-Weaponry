package net.soulsweaponry.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.soulsweaponry.entity.mobs.ShieldBreaker;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    
    @Inject(method = "takeShieldHit", at = @At("TAIL"))
    protected void interceptTakeShieldHit(LivingEntity attacker, CallbackInfo info) {
        if (attacker instanceof ShieldBreaker) {
            if (((ShieldBreaker)attacker).disablesShield()) {
                ((PlayerEntity)(Object)this).disableShield(true);
            }
        }
    }
}
