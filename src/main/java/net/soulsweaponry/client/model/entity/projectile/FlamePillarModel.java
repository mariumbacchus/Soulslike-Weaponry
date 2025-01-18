package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.FlamePillar;
import software.bernie.geckolib.model.GeoModel;

public class FlamePillarModel extends GeoModel<FlamePillar> {

    @Override
    public Identifier getAnimationResource(FlamePillar animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/entity/holy_moonlight_pillar.animation.json");
    }

    @Override
    public Identifier getModelResource(FlamePillar object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/entity/holy_moonlight_pillar.geo.json");
    }

    @Override
    public Identifier getTextureResource(FlamePillar object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/flame_pillar.png");
    }
}
