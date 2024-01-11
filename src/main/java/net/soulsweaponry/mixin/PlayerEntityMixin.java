package net.soulsweaponry.mixin;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.sound.SoundCategory;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.DetonateGroundItem;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParryData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.soulsweaponry.items.UmbralTrespassItem.SHOULD_DAMAGE_RIDING;
import static net.soulsweaponry.items.UmbralTrespassItem.TICKS_BEFORE_DISMOUNT;


@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    public void interceptFallDamage(float fallDistance, float damageMultiplier, DamageSource source, CallbackInfoReturnable<Boolean> info) {
        if (DetonateGroundItem.triggerCalculateFall(((PlayerEntity)(Object)this), fallDistance, source)) {
            info.setReturnValue(false);
            info.cancel();
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
        } catch (Exception ignored) {}
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void interceptDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        PlayerEntity player = ((PlayerEntity) (Object)this);
        try {
            if (player.getDataTracker().get(SHOULD_DAMAGE_RIDING)) {
                info.setReturnValue(false);
            }
        } catch (Exception ignored) {}
        int frames = ParryData.getParryFrames(player);
        if (frames >= 1 && frames <= ConfigConstructor.shield_parry_frames && !source.isUnblockable()) {
            player.world.sendEntityStatus(player, EntityStatuses.BLOCK_WITH_SHIELD);
            if (source.isProjectile() && source.getSource() instanceof ProjectileEntity) {
                info.setReturnValue(false);
                return;
            }
            if (source.getAttacker() instanceof LivingEntity attacker) {
                if (!attacker.hasStatusEffect(EffectRegistry.POSTURE_BREAK)) {
                    attacker.world.playSound(null, attacker.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT, SoundCategory.PLAYERS, .5f, 1f);
                }
                attacker.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK, 60, 0));
                attacker.takeKnockback(0.4f,  player.getX() - attacker.getX(), player.getZ() - attacker.getZ());
                info.setReturnValue(false);
            }
        }
    }
}
