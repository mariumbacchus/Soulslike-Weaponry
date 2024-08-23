package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.client.model.entity.projectile.DragonslayerSwordspearModel;
import net.soulsweaponry.entity.projectile.DragonslayerSwordspearEntity;
import net.soulsweaponry.events.ClientModBusEvents;

public class DragonslayerSwordspearRenderer extends EntityRenderer<DragonslayerSwordspearEntity> {
    public static final Identifier TEXTURE = new Identifier("soulsweapons","textures/entity/dragonslayer_swordspear.png");
    private final DragonslayerSwordspearModel model;

    public DragonslayerSwordspearRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new DragonslayerSwordspearModel(context.getPart(ClientModBusEvents.DRAGONSLAYER_SWORDSPEAR_LAYER));
    }

    public void render(DragonslayerSwordspearEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, entity.prevYaw, entity.getYaw()) - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, entity.prevPitch, entity.getPitch()) + 90.0F));
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(this.getTexture(entity)), false, entity.isEnchanted());
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
    }

	@Override
	public Identifier getTexture(DragonslayerSwordspearEntity entity) {
		return TEXTURE;
	}
}
