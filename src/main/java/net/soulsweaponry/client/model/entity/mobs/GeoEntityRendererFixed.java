package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public abstract class GeoEntityRendererFixed<T extends LivingEntity & IAnimatable> extends GeoEntityRenderer<T> {

    public GeoEntityRendererFixed(EntityRendererFactory.Context renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Override
    public Identifier getTexture(T entity) {
        return this.modelProvider.getTextureLocation(entity);
    }
}
