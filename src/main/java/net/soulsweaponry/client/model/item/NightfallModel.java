package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.hammer.Nightfall;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class NightfallModel extends GeoModel<Nightfall> {

    @Override
    public Identifier getModelResource(Nightfall nightfall, @Nullable GeoRenderer<Nightfall> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/nightfall.geo.json");
    }

    @Override
    public Identifier getTextureResource(Nightfall nightfall, @Nullable GeoRenderer<Nightfall> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/nightfall.png");
    }

    @Override
    public Identifier getAnimationResource(Nightfall animatable) {
        return null;
    }
}
