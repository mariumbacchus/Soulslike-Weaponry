package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.GrowingFireball;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GrowingFireballModel extends AnimatedGeoModel<GrowingFireball> {

    @Override
    public Identifier getAnimationFileLocation(GrowingFireball animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/growing_fireball.animation.json");
    }

    @Override
    public Identifier getModelLocation(GrowingFireball object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/growing_fireball.geo.json");
    }

    @Override
    public Identifier getTextureLocation(GrowingFireball object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/growing_fireball.png");
    }
}