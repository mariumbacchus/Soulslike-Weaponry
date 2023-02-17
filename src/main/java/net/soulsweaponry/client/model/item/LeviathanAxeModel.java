package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.LeviathanAxe;
import software.bernie.geckolib.model.GeoModel;

public class LeviathanAxeModel extends GeoModel<LeviathanAxe> {

    @Override
    public Identifier getAnimationResource(LeviathanAxe animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(LeviathanAxe object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/leviathan_axe.geo.json");
    }

    @Override
    public Identifier getTextureResource(LeviathanAxe object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/item/leviathan_axe_texture.png");
    }
    
}
