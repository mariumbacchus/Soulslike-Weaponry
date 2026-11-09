package net.soulsweaponry.client.model.armor;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.model.GeoModel;

public class EliteSoulIngotArmorModel<T extends Item & GeoItem> extends GeoModel<T> {

    @Override
    public Identifier getAnimationResource(T animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(T object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/elite_soul_ingot_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(T object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/armor/elite_soul_ingot_armor.png");
    }
    
}
