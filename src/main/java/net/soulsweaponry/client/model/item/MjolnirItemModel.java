package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.hammer.Mjolnir;
import software.bernie.geckolib.model.GeoModel;

public class MjolnirItemModel extends GeoModel<Mjolnir> {

    @Override
    public Identifier getAnimationResource(Mjolnir animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(Mjolnir object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/mjolnir.geo.json");
    }

    @Override
    public Identifier getTextureResource(Mjolnir object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/mjolnir.png");
    }
}
