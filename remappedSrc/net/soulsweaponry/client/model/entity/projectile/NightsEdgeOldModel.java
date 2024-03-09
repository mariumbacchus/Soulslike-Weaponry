package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.soulsweaponry.entity.projectile.NightsEdge;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class NightsEdgeOldModel extends EntityModel<NightsEdge> {
	private final ModelPart main;
	private final ModelPart inner;
	private final ModelPart outer;
	private final ModelPart left;
	private final ModelPart right;

	public NightsEdgeOldModel(ModelPart root) {
		this.main = root.getChild("main");
		this.inner = main.getChild("inner3");
		this.outer = main.getChild("outer2");
		this.left = outer.getChild("left");
		this.right = outer.getChild("right");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create(), ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		ModelPartData inner3 = main.addChild("inner3", ModelPartBuilder.create().uv(14, 24).cuboid(-5.4259F, 1.2778F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(30, 11).cuboid(-3.4259F, 4.2778F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(16, 20).cuboid(-2.4259F, 4.2778F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(30, 2).cuboid(-4.4259F, 5.2778F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-10.4259F, 4.2778F, -0.5F, 6.0F, 6.0F, 1.0F, new Dilation(0.0F))
		.uv(16, 11).cuboid(-8.4259F, 3.2778F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(10, 20).cuboid(-4.4259F, 4.2778F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 9).cuboid(-4.4259F, 1.2778F, -0.5F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(22, 16).cuboid(-7.4259F, 2.2778F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 7).cuboid(-4.4259F, 0.2778F, -0.5F, 5.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(14, 0).cuboid(-3.4259F, -1.7222F, -0.5F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(22, 3).cuboid(-2.4259F, -2.7222F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(25, 30).cuboid(0.5741F, -2.7222F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(24, 0).cuboid(-1.4259F, -3.7222F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(30, 15).cuboid(-0.4259F, 1.2778F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(22, 26).cuboid(0.5741F, -0.7222F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(26, 5).cuboid(1.5741F, -1.7222F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(14, 28).cuboid(2.5741F, -1.7222F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(30, 23).cuboid(3.5741F, -2.7222F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(29, 30).cuboid(4.5741F, -3.7222F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(26, 18).cuboid(1.5741F, -2.7222F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(18, 6).cuboid(0.5741F, -4.7222F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(6, 25).cuboid(5.5741F, -7.7222F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 30).cuboid(1.5741F, -5.7222F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 18).cuboid(2.5741F, -6.7222F, -0.5F, 2.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(10, 28).cuboid(4.5741F, -7.7222F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(18, 28).cuboid(6.5741F, -7.7222F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(10.3757F, -10.1565F, 0.0F));

		ModelPartData outer2 = main.addChild("outer2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData left = outer2.addChild("left", ModelPartBuilder.create().uv(0, 13).cuboid(0.5F, -4.4091F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(16, 13).cuboid(-1.5F, -5.4091F, -1.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(28, 8).cuboid(-0.5F, -4.4091F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(26, 27).cuboid(-0.5F, -2.4091F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(10, 7).cuboid(-1.5F, -1.4091F, -1.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(16, 24).cuboid(-2.5F, -0.4091F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(24, 11).cuboid(-2.5F, 3.5909F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(16, 16).cuboid(-1.5F, 2.5909F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 27).cuboid(-0.5F, 1.5909F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(20, 6).cuboid(0.5F, 0.5909F, -1.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(6, 20).cuboid(1.5F, -0.4091F, -1.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5503F, -2.4696F, 0.0F));

		ModelPartData right = outer2.addChild("right", ModelPartBuilder.create().uv(8, 15).cuboid(-2.5455F, -4.3636F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(14, 3).cuboid(-1.5455F, -5.3636F, -1.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(26, 24).cuboid(-0.5455F, -4.3636F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(26, 20).cuboid(-0.5455F, -2.3636F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(8, 11).cuboid(-1.5455F, -1.3636F, -1.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(10, 24).cuboid(1.4545F, -0.3636F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 23).cuboid(1.4545F, 3.6364F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(12, 20).cuboid(0.4545F, 2.6364F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(22, 22).cuboid(-0.5455F, 1.6364F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(22, 18).cuboid(-1.5455F, 0.6364F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(18, 20).cuboid(-2.5455F, -0.3636F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(2.5866F, 1.6668F, 0.0F, 0.0F, 0.0F, 1.5708F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(NightsEdge entity, float limbAngle, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (main.pivotY > 4f) {
			main.pivotY = 28f - entity.age;
		}
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}