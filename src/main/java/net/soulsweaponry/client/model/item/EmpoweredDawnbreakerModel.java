package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.sword.EmpoweredDawnbreaker;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class EmpoweredDawnbreakerModel extends GeoModel<EmpoweredDawnbreaker> {

    @Override
    public Identifier getModelResource(EmpoweredDawnbreaker empoweredDawnbreaker, @Nullable GeoRenderer<EmpoweredDawnbreaker> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/empowered_dawnbreaker.geo.json");
    }

    @Override
    public Identifier getTextureResource(EmpoweredDawnbreaker empoweredDawnbreaker, @Nullable GeoRenderer<EmpoweredDawnbreaker> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/empowered_dawnbreaker.png");
    }

    @Override
    public Identifier getAnimationResource(EmpoweredDawnbreaker animatable) {
        return null;
    }
}