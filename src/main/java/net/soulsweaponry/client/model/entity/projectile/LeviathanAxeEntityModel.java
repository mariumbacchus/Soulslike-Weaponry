package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.LeviathanAxeEntity;
import software.bernie.geckolib.model.GeoModel;

public class LeviathanAxeEntityModel extends GeoModel<LeviathanAxeEntity> {

    @Override
    public Identifier getAnimationResource(LeviathanAxeEntity animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/leviathan_axe.animation.json");
    }

    @Override
    public Identifier getModelResource(LeviathanAxeEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/leviathan_axe.geo.json");
    }

    @Override
    public Identifier getTextureResource(LeviathanAxeEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/item/leviathan_axe_texture.png");
    }
    
}
