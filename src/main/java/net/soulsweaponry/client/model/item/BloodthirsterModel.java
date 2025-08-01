package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.sword.Bloodthirster;
import software.bernie.geckolib.model.GeoModel;

public class BloodthirsterModel extends GeoModel<Bloodthirster> {

    @Override
    public Identifier getAnimationResource(Bloodthirster animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(Bloodthirster object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/bloodthirster.geo.json");
    }

    @Override
    public Identifier getTextureResource(Bloodthirster object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/bloodthirster_textures.png");
    }
}
