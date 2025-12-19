package net.soulsweaponry.client.model.armor;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class ChaosArmorModel<T extends Item & GeoItem> extends GeoModel<T> {

    @Override
    public Identifier getAnimationResource(T animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/chaos_armor.animation.json");
    }

    @Override
    public Identifier getModelResource(T t, @Nullable GeoRenderer<T> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/chaos_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(T t, @Nullable GeoRenderer<T> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/armor/chaos_armor.png");
    }
}
