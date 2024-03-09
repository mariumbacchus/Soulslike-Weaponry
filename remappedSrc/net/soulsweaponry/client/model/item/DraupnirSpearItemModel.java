package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.DraupnirSpear;
import software.bernie.geckolib.model.GeoModel;

public class DraupnirSpearItemModel extends GeoModel<DraupnirSpear> {

    @Override
    public Identifier getAnimationResource(DraupnirSpear animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/draupnir_spear.animation.json");
    }

    @Override
    public Identifier getModelResource(DraupnirSpear object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/draupnir_spear.geo.json");

    }

    @Override
    public Identifier getTextureResource(DraupnirSpear object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/draupnir_spear.png");
    }
}
