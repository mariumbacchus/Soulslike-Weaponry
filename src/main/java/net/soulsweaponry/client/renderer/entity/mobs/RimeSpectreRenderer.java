package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.mobs.RimeSpectreModel;
import net.soulsweaponry.entity.mobs.RimeSpectre;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RimeSpectreRenderer extends GeoEntityRenderer<RimeSpectre> {

    public RimeSpectreRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new RimeSpectreModel());
        this.shadowRadius = 0.7F;
    }
    
    @Override
    protected float getDeathMaxRotation(RimeSpectre entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public RenderLayer getRenderType(RimeSpectre animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }
}
