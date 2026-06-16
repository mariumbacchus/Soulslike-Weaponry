package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.util.CustomDeathHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.Color;

public abstract class GeoEntityRendererWithDeathlight<T extends LivingEntity & GeoEntity> extends GeoEntityRenderer<T> {

    public GeoEntityRendererWithDeathlight(EntityRendererFactory.Context ctx, GeoModel<T> model) {
        super(ctx, model);
    }

    @Override
    public void postRender(MatrixStack poseStack, T entity, BakedGeoModel model, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        if (this.shouldRenderDeathLight(entity)) {
            CustomDeathHandler.renderDeathLight(entity, entity.getYaw(), partialTick, poseStack, this.getDeathLightTranslation(), bufferSource, packedLight, this.getDeathTicks(entity), this.getDeathLightColorOne(), this.getDeathLightColorTwo(), this.getDeathLightColorThree(), this.getDeathLightColorFour());
        }
    }

    @Override
    protected float getDeathMaxRotation(T animatable) {
        return 0f;
    }

    protected boolean shouldRenderDeathLight(T entity) {
        return true;
    }

    protected Vec3d getDeathLightTranslation() {
        return Vec3d.ZERO;
    }

    protected abstract int getDeathTicks(T entity);

    protected abstract Color getDeathLightColorOne();

    protected abstract Color getDeathLightColorTwo();

    protected abstract Color getDeathLightColorThree();

    protected abstract Color getDeathLightColorFour();
}