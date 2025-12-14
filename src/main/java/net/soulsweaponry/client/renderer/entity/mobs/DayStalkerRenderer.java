package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.mobs.DayStalkerModel;
import net.soulsweaponry.entity.mobs.DayStalker;

import java.awt.*;

public class DayStalkerRenderer extends GeoEntityRendererDeathLight<DayStalker> {

    public static final Color LIGHT_ORANGE = new Color(250, 186, 132);
    public static final Color DARK_SLATE_PURPLE = new Color(72, 63, 98);
    public static final Color VERY_DARK_INDIGO = new Color(40, 34, 59);

    public DayStalkerRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DayStalkerModel(), Color.RED, LIGHT_ORANGE, DARK_SLATE_PURPLE, VERY_DARK_INDIGO, 4);
        this.shadowRadius = 1F;
    }

    @Override
    public RenderLayer getRenderType(DayStalker animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTextureLocation(animatable));
    }
}
