package net.soulsweaponry.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.soulsweaponry.entity.mobs.ShieldBreaker;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.soulsweaponry.items.UmbralTrespassItem.SHOULD_DAMAGE_RIDING;
import static net.soulsweaponry.items.UmbralTrespassItem.TICKS_BEFORE_DISMOUNT;


@Mixin(PlayerEntity.class)
public class PlayerEntityMixin<T> {
    
    @Inject(method = "takeShieldHit", at = @At("TAIL"))
    protected void interceptTakeShieldHit(LivingEntity attacker, CallbackInfo info) {
        if (attacker instanceof ShieldBreaker) {
            if (((ShieldBreaker)attacker).disablesShield()) {
                ((PlayerEntity)(Object)this).disableShield(true);
            }
        }
    }

    @Inject(method = "tickRiding", at = @At("HEAD"))
    public void interceptTickRiding(CallbackInfo info) {
        PlayerEntity player = ((PlayerEntity) (Object)this);
        try {
            if (player.getDataTracker().get(SHOULD_DAMAGE_RIDING)) {
                int time = player.getDataTracker().get(TICKS_BEFORE_DISMOUNT);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 10, 0));
                if (time < 0) {
                    player.stopRiding();
                } else {
                    player.getDataTracker().set(TICKS_BEFORE_DISMOUNT, time - 1);
                }
            }
        } catch (Exception e) {}
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void interceptDamage(DamageSource source, float amount, CallbackInfoReturnable<T> info) {
        PlayerEntity player = ((PlayerEntity) (Object)this);
        try {
            if (player.getDataTracker().get(SHOULD_DAMAGE_RIDING)) {
                info.cancel();
            }
        } catch (Exception e) {}
    }
}
