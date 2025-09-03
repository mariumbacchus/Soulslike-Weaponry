package net.soulsweaponry.client.model.armor;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.model.GeoModel;

public class WitheredArmorModel<T extends Item & GeoItem> extends GeoModel<T> {

    @Override
    public Identifier getAnimationResource(T animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/withered_armor.animation.json");
    }

    @Override
    public Identifier getModelResource(T object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/withered_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(T object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/armor/withered_armor.png");
    }
}