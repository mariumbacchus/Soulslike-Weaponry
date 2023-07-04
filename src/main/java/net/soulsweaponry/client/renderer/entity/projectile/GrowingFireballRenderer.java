package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.projectile.GrowingFireballModel;
import net.soulsweaponry.entity.projectile.GrowingFireball;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GrowingFireballRenderer extends GeoEntityRenderer<GrowingFireball> {

    public GrowingFireballRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GrowingFireballModel());
    }

    @Override
    public RenderLayer getRenderType(GrowingFireball animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }

    @Override
    public void render(GrowingFireball entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if (entity != null) {
            poseStack.scale(entity.getRadius(), entity.getRadius(), entity.getRadius());
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}