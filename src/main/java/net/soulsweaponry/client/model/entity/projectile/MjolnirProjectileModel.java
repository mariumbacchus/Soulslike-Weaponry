package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.MjolnirProjectile;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class MjolnirProjectileModel extends GeoModel<MjolnirProjectile> {

    @Override
    public Identifier getModelResource(MjolnirProjectile mjolnirProjectile, @Nullable GeoRenderer<MjolnirProjectile> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/mjolnir.geo.json");
    }

    @Override
    public Identifier getTextureResource(MjolnirProjectile mjolnirProjectile, @Nullable GeoRenderer<MjolnirProjectile> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/mjolnir.png");
    }

    @Override
    public Identifier getAnimationResource(MjolnirProjectile animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/mjolnir.animation.json");
    }
}
