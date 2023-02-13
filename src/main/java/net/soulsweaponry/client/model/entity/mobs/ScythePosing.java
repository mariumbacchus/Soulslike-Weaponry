package net.soulsweaponry.client.model.entity.mobs;

import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.fabricmc.api.EnvType;

@Environment(value=EnvType.CLIENT)
public class ScythePosing {
    
    public static void hold(ModelPart holdingArm, ModelPart otherArm, ModelPart head, boolean rightArmed) {
        ModelPart modelPart = rightArmed ? holdingArm : otherArm;
        ModelPart modelPart2 = rightArmed ? otherArm : holdingArm;
        modelPart.yaw = (rightArmed ? -0.1f : 0.1f) + head.yaw;
        modelPart2.yaw = (rightArmed ? 0.4f : -0.4f) + head.yaw;
        modelPart.pitch = -2.0707964f + head.pitch + 0.1f;
        modelPart2.pitch = -1.2f + head.pitch;
    }

    public static <T extends LivingEntity> void meleeAttack(ModelPart leftArm, ModelPart rightArm, T actor, float swingProgress, float oldAnimationProgress) {
        float animationProgress = oldAnimationProgress;
        float f = MathHelper.sin(swingProgress * (float)Math.PI);
        float g = MathHelper.sin((1.0f - (1.0f - swingProgress) * (1.0f - swingProgress)) * (float)Math.PI);
        leftArm.roll = 0.0f;
        rightArm.roll = 0.0f;
        leftArm.yaw = .5f;
        rightArm.yaw = 0;
        if (actor.getMainArm() == Arm.RIGHT) {
            leftArm.pitch = -0.5f + MathHelper.cos(animationProgress * 0.09f) * 0.15f;
            rightArm.pitch = -1.5f + MathHelper.cos(animationProgress * 0.19f) * 0.5f;
            leftArm.pitch += f * 1 - g * 1f;
            rightArm.pitch += f * 1 - g * 1f;
        } else {
            leftArm.pitch = -0.0f + MathHelper.cos(animationProgress * 0.19f) * 0.5f;
            rightArm.pitch = -1.8849558f + MathHelper.cos(animationProgress * 0.09f) * 0.15f;
            leftArm.pitch += f * 1.2f - g * 0.4f;
            rightArm.pitch += f * 2.2f - g * 0.4f;
        }
        CrossbowPosing.swingArms(leftArm, rightArm, animationProgress);
    }
}
