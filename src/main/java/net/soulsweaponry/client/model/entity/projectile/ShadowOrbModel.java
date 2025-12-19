package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.ShadowOrb;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class ShadowOrbModel extends GeoModel<ShadowOrb> {

    @Override
    public Identifier getModelResource(ShadowOrb shadowOrb, @Nullable GeoRenderer<ShadowOrb> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/cannonball.geo.json");
    }

    @Override
    public Identifier getTextureResource(ShadowOrb shadowOrb, @Nullable GeoRenderer<ShadowOrb> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/shadow_orb.png");
    }

    @Override
    public Identifier getAnimationResource(ShadowOrb animatable) {
        return null;
    }
}
