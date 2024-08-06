package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.LeviathanAxeEntityModel;
import net.soulsweaponry.entity.projectile.LeviathanAxeEntity;

public class LeviathanAxeEntityRenderer extends GeoProjectileRenderer<LeviathanAxeEntity> {

    public LeviathanAxeEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new LeviathanAxeEntityModel());
    }
}
