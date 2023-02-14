package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.Bloodthirster;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BloodthirsterModel extends AnimatedGeoModel<Bloodthirster>{

    @Override
    public Identifier getAnimationFileLocation(Bloodthirster animatable) {
        return null;
    }

    @Override
    public Identifier getModelLocation(Bloodthirster object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/bloodthirster.geo.json");
    }

    @Override
    public Identifier getTextureLocation(Bloodthirster object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/item/bloodthirster_textures.png");
    }
}
