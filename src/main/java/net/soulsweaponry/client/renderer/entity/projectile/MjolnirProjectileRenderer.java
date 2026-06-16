package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.projectile.MjolnirProjectileModel;
import net.soulsweaponry.entity.projectile.MjolnirProjectile;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class MjolnirProjectileRenderer extends GeoProjectileRenderer<MjolnirProjectile> {

    public MjolnirProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MjolnirProjectileModel());
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
        addRenderLayer(new AutoGlowingGeoLayer<>(this) {
            @Override
            protected RenderLayer getRenderType(MjolnirProjectile animatable) {
                return RenderLayer.getEyes(new Identifier(SoulsWeaponry.ModId, "textures/item/mjolnir_glowmask.png"));
            }
        });
    }
}