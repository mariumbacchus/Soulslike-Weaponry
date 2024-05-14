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
import net.soulsweaponry.entity.projectile.DragonslayerSwordspearEntity;

public class DragonslayerSwordspearModel extends EntityModel<DragonslayerSwordspearEntity> {
    
    private final ModelPart base;

    public DragonslayerSwordspearModel(ModelPart modelPart) {
        this.base = modelPart;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        //made this through trial and error... never rotating a model again my god lol
        modelPartData.addChild("sword", ModelPartBuilder.create().uv(0,0).cuboid(-1F, -4F, -1.0F, 3.0F, 8.0F, 1.0F, false), ModelTransform.NONE);
        modelPartData.addChild("sword_top", ModelPartBuilder.create().uv(2,0).cuboid(2.1F, -3.5F, -0.95F, 2.1F, 2.1F, 0.9F, false), ModelTransform.rotation(0.0F, 0.0F, -0.7854F)); 
        
        modelPartData.addChild("middle", ModelPartBuilder.create().uv(0,0).cuboid(-3F, 5.0F, -1F, 7.0F, 1.0F, 1.0F, false), ModelTransform.NONE);
        modelPartData.addChild("right", ModelPartBuilder.create().uv(0,0).cuboid(-6.75F, 1.25F, -1.1F, 4.1F, 1.0F, 1.2F, true), ModelTransform.rotation(0.0F, 0.0F, -0.7854F));
        modelPartData.addChild("left", ModelPartBuilder.create().uv(0,0).cuboid(3.3F, 0.6F, -1.1F, 4.1F, 1.0F, 1.2F, false), ModelTransform.rotation(0.0F, 0.0F, 0.7854F)); //y opp til høyre, x opp til venstre begge gjelder positive tall

        modelPartData.addChild("middle_detail", ModelPartBuilder.create().uv(11,11).cuboid(-0.1F, 4.9F, -1.1F, 1.2F, 1.2F, 1.2F, false), ModelTransform.NONE);
        modelPartData.addChild("sword_detail1", ModelPartBuilder.create().uv(5,11).cuboid(-0.5F, 0F, -1.1F, 0.5F, 3F, 1.2F, false), ModelTransform.NONE);
        modelPartData.addChild("sword_detail2", ModelPartBuilder.create().uv(5,11).cuboid(1F, 0F, -1.1F, 0.5F, 3F, 1.2F, false), ModelTransform.NONE);
        modelPartData.addChild("sword_detail3", ModelPartBuilder.create().uv(5,11).cuboid(0.25F, -3F, -1.1F, 0.5F, 3F, 1.2F, false), ModelTransform.NONE);

        modelPartData.addChild("main_handle", ModelPartBuilder.create().uv(12,0).cuboid(0.0F, 5F, -1.0F, 1.0F, 16.0F, 1.0F, false), ModelTransform.NONE);
        modelPartData.addChild("bot_part", ModelPartBuilder.create().uv(11,11).cuboid(-0.1F, 20.2F, -1.1F, 1.2F, 1.0F, 1.2F, false), ModelTransform.NONE);
        
        modelPartData.addChild("holding_handle", ModelPartBuilder.create().uv(9,6).cuboid(-0.1F, 10F, -1.1F, 1.2F, 2.0F, 1.2F, false), ModelTransform.NONE);
        
        modelPartData.addChild("detail_1", ModelPartBuilder.create().uv(0,9).cuboid(-0.1F, 7F, -1.1F, 1.2F, 0.5F, 1.2F, false), ModelTransform.NONE);
        modelPartData.addChild("detail_2", ModelPartBuilder.create().uv(0,9).cuboid(-0.1F, 14F, -1.1F, 1.2F, 0.5F, 1.2F, false), ModelTransform.NONE);
        modelPartData.addChild("detail_3", ModelPartBuilder.create().uv(0,9).cuboid(-0.1F, 16F, -1.1F, 1.2F, 0.5F, 1.2F, false), ModelTransform.NONE);
        modelPartData.addChild("detail_4", ModelPartBuilder.create().uv(0,9).cuboid(-0.1F, 18F, -1.1F, 1.2F, 0.5F, 1.2F, false), ModelTransform.NONE);

        return TexturedModelData.of(modelData, 16, 16);
    }

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green,
			float blue, float alpha) {
                ImmutableList.of(this.base).forEach((modelRenderer) -> {
                    modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha);
                });
	}

    @Override
    public void setAngles(DragonslayerSwordspearEntity entity, float limbAngle, float limbDistance,
            float animationProgress, float headYaw, float headPitch) {
    }

    
}
