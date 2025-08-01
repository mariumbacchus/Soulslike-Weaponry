package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.LeviathanAxeEntity;
import software.bernie.geckolib.model.GeoModel;

public class LeviathanAxeEntityModel extends GeoModel<LeviathanAxeEntity> {

    @Override
    public Identifier getAnimationResource(LeviathanAxeEntity animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/leviathan_axe.animation.json");
    }

    @Override
    public Identifier getModelResource(LeviathanAxeEntity object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/leviathan_axe.geo.json");
    }

    @Override
    public Identifier getTextureResource(LeviathanAxeEntity object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/leviathan_axe.png");
    }
    
}
