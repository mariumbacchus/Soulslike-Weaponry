package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class FreyrSwordEntityModel extends GeoModel<FreyrSwordEntity> {

    @Override
    public Identifier getModelResource(FreyrSwordEntity freyrSwordEntity, @Nullable GeoRenderer<FreyrSwordEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/entity/freyr_sword.geo.json");
    }

    @Override
    public Identifier getTextureResource(FreyrSwordEntity freyrSwordEntity, @Nullable GeoRenderer<FreyrSwordEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/freyr_sword.png");
    }

    @Override
    public Identifier getAnimationResource(FreyrSwordEntity animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/entity/freyr_sword.animation.json");
    }
}
