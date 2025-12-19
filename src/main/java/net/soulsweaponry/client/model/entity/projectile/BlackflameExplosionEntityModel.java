package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.BlackflameExplosionEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class BlackflameExplosionEntityModel extends GeoModel<BlackflameExplosionEntity> {

    @Override
    public Identifier getAnimationResource(BlackflameExplosionEntity animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/entity/holy_moonlight_pillar.animation.json");
    }

    @Override
    public Identifier getModelResource(BlackflameExplosionEntity blackflameExplosionEntity, @Nullable GeoRenderer<BlackflameExplosionEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/entity/holy_moonlight_pillar.geo.json");
    }

    @Override
    public Identifier getTextureResource(BlackflameExplosionEntity object, @Nullable GeoRenderer<BlackflameExplosionEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/blackflame_explosion_entity.png");
    }
}
