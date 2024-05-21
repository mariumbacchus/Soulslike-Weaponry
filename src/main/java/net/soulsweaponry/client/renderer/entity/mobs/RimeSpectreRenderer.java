package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.mobs.GeoEntityRendererFixed;
import net.soulsweaponry.client.model.entity.mobs.RimeSpectreModel;
import net.soulsweaponry.entity.mobs.RimeSpectre;
import org.jetbrains.annotations.Nullable;

public class RimeSpectreRenderer extends GeoEntityRendererFixed<RimeSpectre> {

    public RimeSpectreRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new RimeSpectreModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    protected float getDeathMaxRotation(RimeSpectre entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public RenderLayer getRenderType(RimeSpectre animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }
}