package net.soulsweaponry.client.model.entity.projectile;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.soulsweaponry.entity.projectile.ChargedArrow;

public class ChargedArrowModelUnused extends EntityModel<ChargedArrow>{
    
    private final ModelPart base;

    public ChargedArrowModelUnused(ModelPart modelPart) {
        this.base = modelPart;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("1", ModelPartBuilder.create().uv(0,0).cuboid(-1.0F, 0.0F, -6.0F, 1.0F, 1.0F, 8.0F, false), ModelTransform.NONE);
        modelPartData.addChild("2", ModelPartBuilder.create().uv(10,0).cuboid(-1.0F, -2.0F, 0.0F, 1.0F, 1.0F, 2.0F, false), ModelTransform.NONE);
        modelPartData.addChild("3", ModelPartBuilder.create().uv(10,0).cuboid(-3.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, false), ModelTransform.NONE);
        modelPartData.addChild("4", ModelPartBuilder.create().uv(10,0).cuboid(-1.0F, 2.0F, 0.0F, 1.0F, 1.0F, 2.0F, false), ModelTransform.NONE);
        modelPartData.addChild("5", ModelPartBuilder.create().uv(10,0).cuboid(1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, false), ModelTransform.NONE);
        modelPartData.addChild("6", ModelPartBuilder.create().uv(0,9).cuboid(-3.0F, 0.0F, -1.0F, 5.0F, 1.0F, 1.0F, false), ModelTransform.NONE);
        modelPartData.addChild("7", ModelPartBuilder.create().uv(0,0).cuboid(-1.0F, -2.0F, -1.0F, 1.0F, 5.0F, 1.0F, false), ModelTransform.NONE);
        modelPartData.addChild("tip", ModelPartBuilder.create().uv(10,3).cuboid(-1.0F, 1F, 1F, 1.0F, 2.0F, 2.0F, false), ModelTransform.rotation(-0.7854F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 16, 16); //-6
    }

    @Override
    public void setAngles(ChargedArrow entity, float limbAngle, float limbDistance,
            float animationProgress, float headYaw, float headPitch) {
        
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green,
            float blue, float alpha) {
        ImmutableList.of(this.base).forEach((modelRenderer) -> {
            modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        });
    }
}
