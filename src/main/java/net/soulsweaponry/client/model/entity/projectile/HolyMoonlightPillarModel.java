package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.HolyMoonlightPillar;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class HolyMoonlightPillarModel extends GeoModel<HolyMoonlightPillar> {

    @Override
    public Identifier getModelResource(HolyMoonlightPillar holyMoonlightPillar, @Nullable GeoRenderer<HolyMoonlightPillar> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/entity/holy_moonlight_pillar.geo.json");
    }

    @Override
    public Identifier getTextureResource(HolyMoonlightPillar holyMoonlightPillar, @Nullable GeoRenderer<HolyMoonlightPillar> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/holy_moonlight_pillar.png");
    }

    @Override
    public Identifier getAnimationResource(HolyMoonlightPillar animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/entity/holy_moonlight_pillar.animation.json");
    }
}
