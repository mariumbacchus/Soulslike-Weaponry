package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.CometSpearModel;
import net.soulsweaponry.entity.projectile.CometSpearEntity;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class CometSpearRenderer extends GeoProjectilesRenderer<CometSpearEntity>{

    public CometSpearRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CometSpearModel());
    }
}
