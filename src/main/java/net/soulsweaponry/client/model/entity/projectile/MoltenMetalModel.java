package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.MoltenMetal;
import software.bernie.geckolib.model.GeoModel;

public class MoltenMetalModel extends GeoModel<MoltenMetal> {

    @Override
    public Identifier getModelResource(MoltenMetal moltenMetal) {
        return new Identifier(SoulsWeaponry.ModId, "geo/entity/molten_metal.geo.json");
    }

    @Override
    public Identifier getTextureResource(MoltenMetal moltenMetal) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/molten_metal.png");
    }

    @Override
    public Identifier getAnimationResource(MoltenMetal moltenMetal) {
        return null;
    }
}
