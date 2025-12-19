package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.MoltenMetal;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class MoltenMetalModel extends GeoModel<MoltenMetal> {

    @Override
    public Identifier getModelResource(MoltenMetal moltenMetal, @Nullable GeoRenderer<MoltenMetal> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/entity/molten_metal.geo.json");
    }

    @Override
    public Identifier getTextureResource(MoltenMetal moltenMetal, @Nullable GeoRenderer<MoltenMetal> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/molten_metal.png");
    }

    @Override
    public Identifier getAnimationResource(MoltenMetal moltenMetal) {
        return null;
    }
}
