package net.soulsweaponry.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.entitydata.ParryData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends LivingEntity> {

    @Unique
    private float parryProgress;

    @Inject(at = @At("HEAD"), method = "animateArms")
    protected void animateArms(T entity, float animationProgress, CallbackInfo info) {
        var model = ((BipedEntityModel<?>)(Object)this);
        // Parry animation TODO rewrite, also make use other hand if item is in other hand
        if (entity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity) {
            int ticks = ParryData.getParryTicks(abstractClientPlayerEntity);
            int maxTicks = ParryData.getMaxParryTicks(abstractClientPlayerEntity);
            if (ticks >= 1) {
                this.parryProgress = ticks == 1 ? 0.1f : parryProgress;
                float added = (1f / (float) maxTicks) / 6f;
                this.parryProgress = Math.min(this.parryProgress + added, 1f);
                ModelPart modelPart = model.leftArm;
                float f = parryProgress;
                model.body.yaw = MathHelper.sin(MathHelper.sqrt(f) * ((float)Math.PI * 2)) * 0.2f;
                model.leftArm.pivotZ = -MathHelper.sin(model.body.yaw) * 5.0f;
                model.leftArm.pivotX = MathHelper.cos(model.body.yaw) * 5.0f;
                model.leftArm.yaw += model.body.yaw;
                model.leftArm.pitch += model.body.yaw;
                f = 1.0f - parryProgress;
                f *= f;
                f *= f;
                f = 1.0f - f;
                float g = MathHelper.sin(f * (float)Math.PI);
                float h = MathHelper.sin(parryProgress * (float)Math.PI) * -(model.head.pitch - 0.7f) * 0.75f;
                modelPart.pitch -= g * 1.2f + h;
                modelPart.yaw += model.body.yaw * 2.0f;
                modelPart.roll += MathHelper.sin(parryProgress * (float)Math.PI) * -0.8f; //0.4
            }
        }
    }
}