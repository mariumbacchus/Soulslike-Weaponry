package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.sword.DarkinBlade;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class DarkinBladeModel extends GeoModel<DarkinBlade> {

    @Override
    public Identifier getModelResource(DarkinBlade darkinBlade, @Nullable GeoRenderer<DarkinBlade> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/darkin_blade.geo.json");
    }

    @Override
    public Identifier getTextureResource(DarkinBlade darkinBlade, @Nullable GeoRenderer<DarkinBlade> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/darkin_blade_textures.png");
    }

    @Override
    public Identifier getAnimationResource(DarkinBlade animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/darkin_blade.animation.json");
    }
}
