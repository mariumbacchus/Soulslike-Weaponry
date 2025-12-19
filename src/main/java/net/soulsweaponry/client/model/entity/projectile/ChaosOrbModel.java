package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.ChaosOrbEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class ChaosOrbModel extends GeoModel<ChaosOrbEntity> {

    @Override
    public Identifier getAnimationResource(ChaosOrbEntity animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/entity/chaos_orb.animation.json");
    }

    @Override
    public Identifier getModelResource(ChaosOrbEntity chaosOrbEntity, @Nullable GeoRenderer<ChaosOrbEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/entity/chaos_orb.geo.json");
    }

    @Override
    public Identifier getTextureResource(ChaosOrbEntity chaosOrbEntity, @Nullable GeoRenderer<ChaosOrbEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/chaos_orb_0.png");
    }
}
