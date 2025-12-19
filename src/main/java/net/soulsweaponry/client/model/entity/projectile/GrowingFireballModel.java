package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.GrowingFireball;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class GrowingFireballModel extends GeoModel<GrowingFireball> {

    @Override
    public Identifier getModelResource(GrowingFireball growingFireball, @Nullable GeoRenderer<GrowingFireball> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/entity/growing_fireball.geo.json");
    }

    @Override
    public Identifier getTextureResource(GrowingFireball growingFireball, @Nullable GeoRenderer<GrowingFireball> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/growing_fireball.png");
    }

    @Override
    public Identifier getAnimationResource(GrowingFireball animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/entity/growing_fireball.animation.json");
    }
}
