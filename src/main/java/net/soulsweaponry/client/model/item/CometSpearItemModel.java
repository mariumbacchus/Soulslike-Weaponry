package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.spear.CometSpear;
import software.bernie.geckolib.model.GeoModel;

public class CometSpearItemModel extends GeoModel<CometSpear> {
    @Override
    public Identifier getModelResource(CometSpear object)
    {
        return Identifier.of(SoulsWeaponry.ModId, "geo/comet_spear.geo.json");
    }

    @Override
    public Identifier getTextureResource(CometSpear object)
    {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/comet_spear.png");
    }

    @Override
    public Identifier getAnimationResource(CometSpear object)
    {
        return Identifier.of(SoulsWeaponry.ModId, null);
    }
}
