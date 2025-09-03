package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.model.GeoModel;

public abstract class BulletRenderer<T extends SilverBulletEntity> extends GeoProjectileRenderer<T> {

    public static final Color TRANSLUCENT_CYAN = Color.ofRGBA(0f, 1f, 1f, 0.5f);
    public static final Color TRANSLUCENT_WHITE = Color.ofRGBA(1f, 1f, 1f, 0.5f);

    public BulletRenderer(EntityRendererFactory.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }

    @Override
    public RenderLayer getRenderType(T animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    public Color getRenderColor(T animatable, float partialTick, int packedLight) {
        if (animatable.isEchoCopy() && animatable.isNoClip()) {
            return TRANSLUCENT_CYAN;
        }
        if (animatable.isEchoCopy()) {
            return Color.CYAN;
        }
        if (animatable.isNoClip()) {
            return TRANSLUCENT_WHITE;
        }
        return super.getRenderColor(animatable, partialTick, packedLight);
    }
}