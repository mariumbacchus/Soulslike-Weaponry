package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.client.model.entity.projectile.LeviathanAxeEntityModel;
import net.soulsweaponry.entity.projectile.LeviathanAxeEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LeviathanAxeEntityRenderer extends GeoProjectileRenderer<LeviathanAxeEntity> {

    public LeviathanAxeEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new LeviathanAxeEntityModel());
    }
}
