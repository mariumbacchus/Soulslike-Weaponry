package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.soulsweaponry.client.model.entity.mobs.ReturningKnightModel;
import net.soulsweaponry.entity.mobs.ReturningKnight;

public class ReturningKnightRenderer extends GeoEntityRendererDeathLight<ReturningKnight> {
    
    public ReturningKnightRenderer(Context ctx) {
        super(ctx, new ReturningKnightModel(), MoonknightRenderer.SOFT_PINK, MoonknightRenderer.LIGHT_CREAM_YELLOW, MoonknightRenderer.AMETHYST_PURPLE, MoonknightRenderer.LIGHT_AQUA, 4);
        this.shadowRadius = 2.5F;
    }
}
