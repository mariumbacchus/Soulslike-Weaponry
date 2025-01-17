package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.HolyMoonlightPillar;
import software.bernie.geckolib.model.GeoModel;

public class HolyMoonlightPillarModel extends GeoModel<HolyMoonlightPillar> {

    @Override
    public Identifier getAnimationResource(HolyMoonlightPillar animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/entity/holy_moonlight_pillar.animation.json");
    }

    @Override
    public Identifier getModelResource(HolyMoonlightPillar object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/entity/holy_moonlight_pillar.geo.json");
    }

    @Override
    public Identifier getTextureResource(HolyMoonlightPillar object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/holy_moonlight_pillar.png");
    }
}
