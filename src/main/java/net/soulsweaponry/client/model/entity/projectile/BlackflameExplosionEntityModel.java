package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.BlackflameExplosionEntity;
import software.bernie.geckolib.model.GeoModel;

public class BlackflameExplosionEntityModel extends GeoModel<BlackflameExplosionEntity> {

    @Override
    public Identifier getAnimationResource(BlackflameExplosionEntity animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/entity/holy_moonlight_pillar.animation.json");
    }

    @Override
    public Identifier getModelResource(BlackflameExplosionEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/entity/holy_moonlight_pillar.geo.json");
    }

    @Override
    public Identifier getTextureResource(BlackflameExplosionEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/blackflame_explosion_entity.png");
    }
}
