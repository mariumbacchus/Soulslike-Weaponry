package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.scythe.ForlornScythe;
import software.bernie.geckolib.model.GeoModel;

public class ForlornScytheModel extends GeoModel<ForlornScythe> {

    @Override
    public Identifier getAnimationResource(ForlornScythe animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(ForlornScythe object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/forlorn_scythe.geo.json");
    }

    @Override
    public Identifier getTextureResource(ForlornScythe object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/forlorn_scythe.png");
    }
    
}
