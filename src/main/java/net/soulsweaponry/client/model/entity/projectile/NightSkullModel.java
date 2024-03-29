package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.NightSkull;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NightSkullModel extends AnimatedGeoModel<NightSkull> {

    @Override
    public Identifier getAnimationResource(NightSkull animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/night_skull.animation.json");
    }

    @Override
    public Identifier getModelResource(NightSkull object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/night_skull.geo.json");

    }

    @Override
    public Identifier getTextureResource(NightSkull object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/night_skull.png");
    }
}