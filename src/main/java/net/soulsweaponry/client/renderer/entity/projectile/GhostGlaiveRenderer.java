package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.projectile.GhostGlaiveModel;
import net.soulsweaponry.entity.projectile.noclip.GhostGlaiveEntity;

public class GhostGlaiveRenderer extends GeoProjectileRenderer<GhostGlaiveEntity> {

    public GhostGlaiveRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GhostGlaiveModel());
    }

    @Override
    public RenderLayer getRenderType(GhostGlaiveEntity animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(texture);
    }
}