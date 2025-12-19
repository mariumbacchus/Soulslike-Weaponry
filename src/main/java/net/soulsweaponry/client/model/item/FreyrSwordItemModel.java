package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.sword.FreyrSword;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class FreyrSwordItemModel extends GeoModel<FreyrSword> {

    @Override
    public Identifier getModelResource(FreyrSword freyrSword, @Nullable GeoRenderer<FreyrSword> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/freyr_sword_item.geo.json");
    }

    @Override
    public Identifier getTextureResource(FreyrSword freyrSword, @Nullable GeoRenderer<FreyrSword> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/freyr_sword.png");
    }

    @Override
    public Identifier getAnimationResource(FreyrSword animatable) {
        return null;
    }
}
