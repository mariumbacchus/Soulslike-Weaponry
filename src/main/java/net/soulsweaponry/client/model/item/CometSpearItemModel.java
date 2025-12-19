package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.spear.CometSpear;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class CometSpearItemModel extends GeoModel<CometSpear> {

    @Override
    public Identifier getModelResource(CometSpear cometSpear, @Nullable GeoRenderer<CometSpear> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/comet_spear.geo.json");
    }

    @Override
    public Identifier getTextureResource(CometSpear cometSpear, @Nullable GeoRenderer<CometSpear> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/comet_spear.png");
    }

    @Override
    public Identifier getAnimationResource(CometSpear object) {
        return null;
    }
}
