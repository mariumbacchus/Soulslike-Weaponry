package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.scythe.ForlornScythe;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class ForlornScytheModel extends GeoModel<ForlornScythe> {

    @Override
    public Identifier getModelResource(ForlornScythe forlornScythe, @Nullable GeoRenderer<ForlornScythe> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/forlorn_scythe.geo.json");
    }

    @Override
    public Identifier getTextureResource(ForlornScythe forlornScythe, @Nullable GeoRenderer<ForlornScythe> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/forlorn_scythe.png");
    }

    @Override
    public Identifier getAnimationResource(ForlornScythe animatable) {
        return null;
    }
}
