package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.MjolnirProjectile;
import software.bernie.geckolib.model.GeoModel;

public class MjolnirProjectileModel extends GeoModel<MjolnirProjectile> {

    @Override
    public Identifier getAnimationResource(MjolnirProjectile animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/mjolnir.animation.json");
    }

    @Override
    public Identifier getModelResource(MjolnirProjectile object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/mjolnir.geo.json");

    }

    @Override
    public Identifier getTextureResource(MjolnirProjectile object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/mjolnir.png");
    }
}
