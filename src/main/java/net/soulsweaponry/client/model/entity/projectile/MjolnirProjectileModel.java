package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.MjolnirProjectile;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MjolnirProjectileModel extends AnimatedGeoModel<MjolnirProjectile> {

    @Override
    public Identifier getAnimationFileLocation(MjolnirProjectile animatable) {
        return new Identifier(SoulsWeaponry.ModId, null);
    }

    @Override
    public Identifier getModelLocation(MjolnirProjectile object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/mjolnir.geo.json");

    }

    @Override
    public Identifier getTextureLocation(MjolnirProjectile object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/mjolnir_texture.png");
    }
    
}
