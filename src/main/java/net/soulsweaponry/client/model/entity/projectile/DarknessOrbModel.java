package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.DarknessOrb;
import software.bernie.geckolib.model.GeoModel;

public class DarknessOrbModel extends GeoModel<DarknessOrb> {

    @Override
    public Identifier getAnimationResource(DarknessOrb animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(DarknessOrb object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/cannonball.geo.json");
    }

    @Override
    public Identifier getTextureResource(DarknessOrb object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/shadow_orb.png");
    }
}
