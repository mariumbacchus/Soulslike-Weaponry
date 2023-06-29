package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.mobs.WarmthEntityModel;
import net.soulsweaponry.entity.mobs.WarmthEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WarmthEntityRenderer extends GeoEntityRenderer<WarmthEntity> {

    public WarmthEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WarmthEntityModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    public RenderLayer getRenderType(WarmthEntity animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }
}
