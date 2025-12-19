package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.sword.Dawnbreaker;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class DawnbreakerModel extends GeoModel<Dawnbreaker> {

    @Override
    public Identifier getModelResource(Dawnbreaker dawnbreaker, @Nullable GeoRenderer<Dawnbreaker> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/dawnbreaker.geo.json");
    }

    @Override
    public Identifier getTextureResource(Dawnbreaker dawnbreaker, @Nullable GeoRenderer<Dawnbreaker> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/dawnbreaker_texture.png");
    }

    @Override
    public Identifier getAnimationResource(Dawnbreaker animatable) {
        return null;
    }
}
