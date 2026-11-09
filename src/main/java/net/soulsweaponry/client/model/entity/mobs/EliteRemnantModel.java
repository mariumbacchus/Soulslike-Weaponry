package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.client.model.ModelPart;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.entity.mobs.EliteRemnant;

public class EliteRemnantModel extends RemnantModel<EliteRemnant> {

    public EliteRemnantModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setAngles(EliteRemnant entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        if (!entity.isEatingGoldenApple()) {
            return;
        }
        boolean leftHand = entity.getMainArm() == Arm.RIGHT;
        ModelPart arm = leftHand ? this.leftArm : this.rightArm;
        float side = leftHand ? 1.0F : -1.0F;
        float bob = MathHelper.sin(animationProgress * 1.5F) * 0.05F;
        arm.pitch = this.head.pitch - 1.5F + bob;
        arm.yaw = this.head.yaw + side * 0.3F;
        arm.roll = side * 0.1F;
    }
}