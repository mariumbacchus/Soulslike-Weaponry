package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.entity.mobs.Remnant;

public class RemnantModel <T extends Remnant> extends BipedEntityModel<T> {

    public RemnantModel(ModelPart root) {
        super(root);
    }

    public void setAngles(T entity, float f, float g, float h, float i, float j) {
        super.setAngles((T)entity, f, g, h, i, j);
        if (this.isAttacking(entity)) {
            this.rightArm.pitch = 80f;
            if (entity.getMainHandStack() == ItemStack.EMPTY) {
                this.rightArm.pitch = 80f;
                this.leftArm.pitch = 80f;
            }
        }
    }

    public boolean isAttacking(T entity) {
        return entity.isAttacking();
    }
}
