package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.projectile.LeviathanAxeEntityModel;
import net.soulsweaponry.entity.projectile.LeviathanAxeEntity;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class LeviathanAxeEntityRenderer extends GeoProjectileRenderer<LeviathanAxeEntity> {

    public LeviathanAxeEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new LeviathanAxeEntityModel());
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
        addRenderLayer(new AutoGlowingGeoLayer<>(this) {
            @Override
            protected RenderLayer getRenderType(LeviathanAxeEntity animatable) {
                return RenderLayer.getEyes(new Identifier(SoulsWeaponry.ModId, "textures/item/leviathan_axe_glowmask.png"));
            }
        });
    }

    @Override
    protected void applyRotations(LeviathanAxeEntity entity, MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTick) {
        super.applyRotations(entity, matrixStack, ageInTicks, rotationYaw, partialTick);
        boolean noClip = entity.isNoClip();
        if (!entity.inGround() || noClip) {
            float totalTicks = entity.age + partialTick;
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(totalTicks * 60 * (noClip ? 1 : -1)));
        }
    }
}