package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.CometSpearEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class CometSpearModel extends GeoModel<CometSpearEntity> {

    @Override
    public Identifier getModelResource(CometSpearEntity cometSpearEntity, @Nullable GeoRenderer<CometSpearEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/comet_spear.geo.json");
    }

    @Override
    public Identifier getTextureResource(CometSpearEntity cometSpearEntity, @Nullable GeoRenderer<CometSpearEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/comet_spear.png");
    }

    @Override
    public Identifier getAnimationResource(CometSpearEntity animatable) {
        return null;
    }
}
