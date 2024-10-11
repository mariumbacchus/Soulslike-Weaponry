package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.entity.mobs.DarkSorcerer;

public class DarkSorcererModel <T extends DarkSorcerer> extends BipedEntityModel<T> {

    public DarkSorcererModel(ModelPart root) {
        super(root);
    }

    public void setAngles(T entity, float f, float g, float h, float i, float j) {
        super.setAngles(entity, f, g, h, i, j);
        if (entity.getBeaming()) {
            this.head.yaw = i * 0.017453292F;
            this.head.pitch = j * 0.017453292F;
            this.rightArm.pivotZ = 0.0F;
            this.rightArm.pivotX = -5.0F;
            this.leftArm.pivotZ = 0.0F;
            this.leftArm.pivotX = 5.0F;
            this.rightArm.pitch = MathHelper.cos(h * 0.6662F) * 0.25F;
            this.leftArm.pitch = MathHelper.cos(h * 0.6662F) * 0.25F;
            this.rightArm.roll = 2.3561945F;
            this.leftArm.roll = -2.3561945F;
            this.rightArm.yaw = 0.0F;
            this.leftArm.yaw = 0.0F;
        }
    }
}
