package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.TntEntity;

public class ChungusHeadModel extends EntityModel<TntEntity> {

    private final ModelPart main;

    public ChungusHeadModel(ModelPart root) {
        this.main = root.getChild("main");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create().uv(2, 0).cuboid(1.0F, -12.3333F, -0.6667F, 2.0F, 7.0F, 1.0F, new Dilation(0.5F))
                .uv(3, 0).cuboid(-3.0F, -12.3333F, -0.6667F, 2.0F, 7.0F, 1.0F, new Dilation(0.5F))
                .uv(0, 0).cuboid(-4.0F, -4.3333F, -3.6667F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 15.8333F, -0.3333F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(TntEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
