package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.item.Items;
import net.soulsweaponry.entity.mobs.Forlorn;

public class ForlornModel <T extends Forlorn> extends BipedEntityModel<T>{

    public ForlornModel(ModelPart root) {
        super(root);
    }
    
    public boolean isAttacking(T entity) {
        return entity.isAttacking();
    }

    public void setAngles(T entity, float f, float g, float h, float i, float j) {
        super.setAngles((T)entity, f, g, h, i, j);
        if (entity.getMainHandStack().getItem() == Items.CROSSBOW) {
            CrossbowPosing.hold(this.rightArm, this.leftArm, head, true);
        }
    }
}
