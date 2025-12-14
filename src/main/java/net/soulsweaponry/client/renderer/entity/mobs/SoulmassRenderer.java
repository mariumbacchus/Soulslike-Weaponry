package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.mobs.SoulmassModel;
import net.soulsweaponry.entity.mobs.Soulmass;

public class SoulmassRenderer extends GeoEntityRendererDeathLight<Soulmass> {

    public SoulmassRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SoulmassModel(), NightShadeRenderer.DEEP_BLUE, NightShadeRenderer.ROYAL_BLUE, NightShadeRenderer.DARKER_PERIWINKLE_BLUE, NightShadeRenderer.DARK_SAPPHIRE, 2.5f);
        this.shadowRadius = 0.7F;
    }
}
