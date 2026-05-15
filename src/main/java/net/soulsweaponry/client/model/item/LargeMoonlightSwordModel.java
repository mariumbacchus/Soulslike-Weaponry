package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.sword.LargeMoonlightSword;
import software.bernie.geckolib.model.GeoModel;

public class LargeMoonlightSwordModel extends GeoModel<LargeMoonlightSword> {

    @Override
    public Identifier getAnimationResource(LargeMoonlightSword animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(LargeMoonlightSword object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/large_sword_of_moonlight.geo.json");
    }

    @Override
    public Identifier getTextureResource(LargeMoonlightSword object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/large_sword_of_moonlight.png");
    }
}
