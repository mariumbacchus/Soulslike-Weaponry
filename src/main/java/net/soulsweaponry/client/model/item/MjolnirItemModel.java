package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.hammer.Mjolnir;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class MjolnirItemModel extends GeoModel<Mjolnir> {

    @Override
    public Identifier getModelResource(Mjolnir mjolnir, @Nullable GeoRenderer<Mjolnir> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/mjolnir.geo.json");
    }

    @Override
    public Identifier getTextureResource(Mjolnir mjolnir, @Nullable GeoRenderer<Mjolnir> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/mjolnir.png");
    }

    @Override
    public Identifier getAnimationResource(Mjolnir animatable) {
        return null;
    }
}
