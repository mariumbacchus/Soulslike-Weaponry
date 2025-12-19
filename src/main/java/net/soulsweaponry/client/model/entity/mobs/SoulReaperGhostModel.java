package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.soulsweaponry.client.renderer.entity.mobs.SoulReaperGhostRenderer;

public class SoulReaperGhostModel extends BipedEntityModel<SoulReaperGhostRenderer.SoulReaperGhostRenderState> {

    public SoulReaperGhostModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setAngles(SoulReaperGhostRenderer.SoulReaperGhostRenderState state) {
        super.setAngles(state);

        this.rightArm.pitch = 80f;
        this.leftArm.pitch = 80f;

        if (state.isInSneakingPose) {
            this.body.pitch = 0.5F;
            this.rightArm.pitch += 0.4F;
            this.leftArm.pitch += 0.4F;
            this.rightLeg.pivotZ = 4.0F;
            this.leftLeg.pivotZ = 4.0F;
            this.rightLeg.pivotY = 12.2F;
            this.leftLeg.pivotY = 12.2F;
            this.head.pivotY = 4.2F;
            this.body.pivotY = 3.2F;
            this.leftArm.pivotY = 5.2F;
            this.rightArm.pivotY = 5.2F;
        }
    }
}