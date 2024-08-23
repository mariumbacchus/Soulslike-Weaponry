package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.mobs.FrostGiantModel;
import net.soulsweaponry.entity.mobs.FrostGiant;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FrostGiantRenderer extends GeoEntityRenderer<FrostGiant> {

    public FrostGiantRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new FrostGiantModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    protected float getDeathMaxRotation(FrostGiant entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public RenderLayer getRenderType(FrostGiant animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }
}