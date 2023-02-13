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
    }

    public boolean isAttacking(T entity) {
        return entity.isAttacking();
    }
}
