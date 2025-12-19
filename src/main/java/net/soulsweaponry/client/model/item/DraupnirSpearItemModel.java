package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.spear.DraupnirSpear;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class DraupnirSpearItemModel extends GeoModel<DraupnirSpear> {

    @Override
    public Identifier getModelResource(DraupnirSpear draupnirSpear, @Nullable GeoRenderer<DraupnirSpear> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/draupnir_spear.geo.json");
    }

    @Override
    public Identifier getTextureResource(DraupnirSpear draupnirSpear, @Nullable GeoRenderer<DraupnirSpear> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/draupnir_spear.png");
    }

    @Override
    public Identifier getAnimationResource(DraupnirSpear animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/draupnir_spear.animation.json");
    }
}
