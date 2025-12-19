package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.mobs.NightShadeModel;
import net.soulsweaponry.entity.mobs.NightShade;

import java.awt.*;

public class NightShadeRenderer extends GeoEntityRendererDeathLight<NightShade> {

    public static final Color DEEP_BLUE = new Color(13, 2, 125);
    public static final Color ROYAL_BLUE = new Color(20, 0, 237);
    public static final Color DARKER_PERIWINKLE_BLUE = new Color(102, 88, 252);
    public static final Color DARK_SAPPHIRE = new Color(13, 3, 128);
    
    public NightShadeRenderer(Context ctx) {
        super(ctx, new NightShadeModel(), DEEP_BLUE, ROYAL_BLUE, DARKER_PERIWINKLE_BLUE, DARK_SAPPHIRE, 2.5f);
        this.shadowRadius = 0.7F;
    }

    @Override
    public RenderLayer getRenderType(NightShade animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(texture);
    }
}
