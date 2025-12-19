package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.WarmthEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class WarmthEntityModel extends GeoModel<WarmthEntity> {

    @Override
    public Identifier getModelResource(WarmthEntity warmthEntity, @Nullable GeoRenderer<WarmthEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/entity/warmth_entity.geo.json");
    }

    @Override
    public Identifier getTextureResource(WarmthEntity warmthEntity, @Nullable GeoRenderer<WarmthEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/warmth_entity.png");
    }

    @Override
    public Identifier getAnimationResource(WarmthEntity animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/entity/warmth_entity.animation.json");
    }
}
