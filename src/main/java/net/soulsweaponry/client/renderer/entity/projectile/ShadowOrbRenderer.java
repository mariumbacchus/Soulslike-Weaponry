package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.ShadowOrbModel;
import net.soulsweaponry.entity.projectile.ShadowOrb;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ShadowOrbRenderer extends GeoEntityRenderer<ShadowOrb> {

    public ShadowOrbRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ShadowOrbModel());
    }
}