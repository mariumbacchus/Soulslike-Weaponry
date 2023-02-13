package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.LeviathanAxeEntityModel;
import net.soulsweaponry.entity.projectile.LeviathanAxeEntity;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class LeviathanAxeEntityRenderer extends GeoProjectilesRenderer<LeviathanAxeEntity>{

    public LeviathanAxeEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new LeviathanAxeEntityModel());
    }
}
