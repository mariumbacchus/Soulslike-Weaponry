package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.soulsweaponry.entity.mobs.SoulReaperGhost;

public class SoulReaperGhostModel <T extends SoulReaperGhost> extends BipedEntityModel<T> {

    public SoulReaperGhostModel(ModelPart root) {
        super(root);
    }

    public void setAngles(T entity, float f, float g, float h, float i, float j) {
        super.setAngles((T)entity, f, g, h, i, j);
        this.rightArm.pitch = 80f;
        this.leftArm.pitch = 80f;

        ModelPart var10000;
        if (entity.isSneaking()) {
            this.body.pitch = 0.5F;
            var10000 = this.rightArm;
            var10000.pitch += 0.4F;
            var10000 = this.leftArm;
            var10000.pitch += 0.4F;
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
