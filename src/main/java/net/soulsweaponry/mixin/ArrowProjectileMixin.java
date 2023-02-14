package net.soulsweaponry.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.EntityHitResult;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.SoundRegistry;

@Mixin(PersistentProjectileEntity.class)
public class ArrowProjectileMixin {
    
    @Inject(at = @At("TAIL"), method = "onEntityHit")
    private void interceptOnHit(EntityHitResult entityHitResult, CallbackInfo info) {
        PersistentProjectileEntity projectile = ((PersistentProjectileEntity)(Object)this);
        if (ConfigConstructor.can_projectiles_apply_posture_break && projectile instanceof ArrowEntity && projectile.getOwner() != null && entityHitResult.getEntity() instanceof LivingEntity && projectile.getOwner() instanceof PlayerEntity) {
            double random = new Random().nextDouble();
            double chance = 0;
            int amplifier = 0;
            LivingEntity target = (LivingEntity) entityHitResult.getEntity();
            for (ItemStack stack : ((ArrowEntity)(Object) this).getOwner().getItemsHand()) {
                if (stack.getItem() instanceof BowItem && EnchantmentHelper.getLevel(EnchantRegistry.VISCERAL, stack) > 0) {
                    chance = (double)EnchantmentHelper.getLevel(EnchantRegistry.VISCERAL, stack)/12;
                    amplifier = EnchantmentHelper.getLevel(EnchantRegistry.VISCERAL, stack);
                    if (chance > random) {
                        if (!target.hasStatusEffect(EffectRegistry.POSTURE_BREAK)) {
                            target.world.playSound(null, target.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT, SoundCategory.PLAYERS, .5f, 1f);
                        }
                        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK, 60, amplifier));
                    }
                }
            }
        }
    }
}
