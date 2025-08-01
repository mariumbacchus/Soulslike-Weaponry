package net.soulsweaponry.client.model.armor;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.model.GeoModel;

public class ChaosSetModel<T extends Item & GeoItem> extends GeoModel<T> {

    @Override
    public Identifier getAnimationResource(T animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/entity/chaos_monarch.animation.json");
    }

    @Override
    public Identifier getModelResource(T object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/chaos_set.geo.json");
    }

    @Override
    public Identifier getTextureResource(T object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/armor/chaos_set_texture.png");
    }
    
}
