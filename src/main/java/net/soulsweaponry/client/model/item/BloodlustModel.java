package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.katana.Bloodlust;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class BloodlustModel extends GeoModel<Bloodlust> {

    @Override
    public Identifier getModelResource(Bloodlust bloodlust, @Nullable GeoRenderer<Bloodlust> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/bloodlust.geo.json");
    }

    @Override
    public Identifier getTextureResource(Bloodlust bloodlust, @Nullable GeoRenderer<Bloodlust> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/bloodlust.png");
    }

    @Override
    public Identifier getAnimationResource(Bloodlust animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/bloodlust.animation.json");
    }
}
