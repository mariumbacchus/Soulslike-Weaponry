package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.sword.Bloodthirster;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class BloodthirsterModel extends GeoModel<Bloodthirster> {

    @Override
    public Identifier getModelResource(Bloodthirster bloodthirster, @Nullable GeoRenderer<Bloodthirster> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/bloodthirster.geo.json");
    }

    @Override
    public Identifier getTextureResource(Bloodthirster bloodthirster, @Nullable GeoRenderer<Bloodthirster> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/bloodthirster_textures.png");
    }

    @Override
    public Identifier getAnimationResource(Bloodthirster animatable) {
        return null;
    }
}
