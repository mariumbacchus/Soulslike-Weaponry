package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.mobs.GeoEntityRendererFixed;
import net.soulsweaponry.client.model.entity.mobs.WarmthEntityModel;
import net.soulsweaponry.entity.mobs.WarmthEntity;
import org.jetbrains.annotations.Nullable;

public class WarmthEntityRenderer extends GeoEntityRendererFixed<WarmthEntity> {

    public WarmthEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WarmthEntityModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    public RenderLayer getRenderType(WarmthEntity animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }
}