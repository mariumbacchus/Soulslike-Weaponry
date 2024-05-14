package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.ChaosOrbEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ChaosOrbModel extends AnimatedGeoModel<ChaosOrbEntity> {

    @Override
    public Identifier getAnimationFileLocation(ChaosOrbEntity animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/chaos_orb.animation.json");
    }

    @Override
    public Identifier getModelLocation(ChaosOrbEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/chaos_orb.geo.json");
    }

    @Override
    public Identifier getTextureLocation(ChaosOrbEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/chaos_orb_0.png");
    }
}