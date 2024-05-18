package net.soulsweaponry.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

@Mixin(LivingEntity.class)
public interface LivingEntityInvoker {
    
    @Invoker("applyDamage")
    void invokeApplyDamage(DamageSource source, float amount);
}
