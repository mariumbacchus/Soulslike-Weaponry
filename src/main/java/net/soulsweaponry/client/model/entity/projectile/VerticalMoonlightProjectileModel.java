package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class VerticalMoonlightProjectileModel extends AnimatedGeoModel<MoonlightProjectile>{

    @Override
    public Identifier getAnimationFileLocation(MoonlightProjectile animatable) {
        return null;
    }

    @Override
    public Identifier getModelLocation(MoonlightProjectile object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/vertical_moonlight_projectile.geo.json");
    }

    @Override
    public Identifier getTextureLocation(MoonlightProjectile object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/moonlight_projectile_big.png");
    }
}
