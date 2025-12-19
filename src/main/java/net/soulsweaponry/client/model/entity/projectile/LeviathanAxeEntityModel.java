package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.LeviathanAxeEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class LeviathanAxeEntityModel extends GeoModel<LeviathanAxeEntity> {

    @Override
    public Identifier getModelResource(LeviathanAxeEntity leviathanAxeEntity, @Nullable GeoRenderer<LeviathanAxeEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/leviathan_axe.geo.json");
    }

    @Override
    public Identifier getTextureResource(LeviathanAxeEntity leviathanAxeEntity, @Nullable GeoRenderer<LeviathanAxeEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/leviathan_axe.png");
    }

    @Override
    public Identifier getAnimationResource(LeviathanAxeEntity animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/leviathan_axe.animation.json");
    }
}
