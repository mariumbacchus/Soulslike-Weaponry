package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.CometSpear;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CometSpearItemModel extends AnimatedGeoModel<CometSpear>{
    @Override
    public Identifier getModelResource(CometSpear object)
    {
        return new Identifier(SoulsWeaponry.ModId, "geo/comet_spear.geo.json");
    }

    @Override
    public Identifier getTextureResource(CometSpear object)
    {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/comet_spear.png");
    }

    @Override
    public Identifier getAnimationResource(CometSpear object)
    {
        return new Identifier(SoulsWeaponry.ModId, null);
    }
}
