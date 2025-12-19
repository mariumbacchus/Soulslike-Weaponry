package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.client.renderer.entity.mobs.DarkSorcererRenderer;

public class DarkSorcererModel extends BipedEntityModel<DarkSorcererRenderer.DarkSorcererRenderState> {

    public DarkSorcererModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setAngles(DarkSorcererRenderer.DarkSorcererRenderState state) {
        super.setAngles(state);

        if (state.beaming) {
            this.head.yaw = state.yawDegrees * ((float)Math.PI / 180F);
            this.head.pitch = state.pitch * ((float)Math.PI / 180F);

            this.rightArm.pivotZ = 0.0F;
            this.rightArm.pivotX = -5.0F;
            this.leftArm.pivotZ = 0.0F;
            this.leftArm.pivotX = 5.0F;

            float t = state.age;
            this.rightArm.pitch = MathHelper.cos(t * 0.6662F) * 0.25F;
            this.leftArm.pitch = MathHelper.cos(t * 0.6662F) * 0.25F;
            this.rightArm.roll = 2.3561945F;
            this.leftArm.roll = -2.3561945F;
            this.rightArm.yaw = 0.0F;
            this.leftArm.yaw = 0.0F;
        }
    }
}
