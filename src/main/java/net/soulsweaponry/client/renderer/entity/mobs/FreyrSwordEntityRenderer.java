package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.mobs.FreyrSwordEntityModel;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class FreyrSwordEntityRenderer extends GeoProjectilesRenderer<FreyrSwordEntity> {

    public FreyrSwordEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new FreyrSwordEntityModel());
    }
}
