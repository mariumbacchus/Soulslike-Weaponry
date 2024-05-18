package net.soulsweaponry.client.model.entity.mobs;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.HostileEntity;

public class BigChungusModel<T extends HostileEntity> extends SinglePartEntityModel<T> {

    private final ModelPart root;
    

    public BigChungusModel(ModelPart modelPart) {
        this.root = modelPart;
        //this.base = modelPart.getChild(EntityModelPartNames.BODY);
        //modelPart.getChild(EntityModelPartNames.HEAD);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        //modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 12F, 12F, 12F), ModelTransform.pivot(0F, 0F, 0F));
        //modelPartData.addChild("body", ModelPartBuilder.create().uv(0,20).cuboid(-6F, 7F, -6F, 12F, 7F, 12F, false), ModelTransform.NONE); //offset så størrelse
        //modelPartData.addChild("head", ModelPartBuilder.create().uv(0,16).cuboid(-6F, 3F, -6F, 8F, 10F, 8F, false), ModelTransform.NONE);
        modelPartData.addChild("earRight", ModelPartBuilder.create().uv(2,0).cuboid(1.0F, 0F, -1.0F, 2.0F, 7.0F, 1.0F, false), ModelTransform.NONE);
        modelPartData.addChild("earLeft", ModelPartBuilder.create().uv(3,0).cuboid(-3.0F, 0F, -1.0F, 2.0F, 7.0F, 1.0F, false), ModelTransform.NONE);
        modelPartData.addChild("feetRight", ModelPartBuilder.create().uv(32,0).cuboid(-4.0F, 23F, -3.0F, 2.0F, 1.0F, 6.0F, false), ModelTransform.NONE);
        modelPartData.addChild("feetLeft", ModelPartBuilder.create().uv(32,0).cuboid(2.0F, 23F, -3.0F, 2.0F, 1.0F, 6.0F, false), ModelTransform.NONE);
        modelPartData.addChild("body", ModelPartBuilder.create().uv(0,19).cuboid(-6.0F, 15F, -6.0F, 12.0F, 8.0F, 12.0F, false), ModelTransform.NONE);
        modelPartData.addChild("head", ModelPartBuilder.create().uv(0,0).cuboid(-4.0F, 7F, -4.0F, 8.0F, 8.0F, 8.0F, false), ModelTransform.NONE);
        
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green,
            float blue, float alpha) {
                ImmutableList.of(this.root).forEach((modelRenderer) -> {
                    modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha);
                });
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw,
            float headPitch) {
        
    }
}
