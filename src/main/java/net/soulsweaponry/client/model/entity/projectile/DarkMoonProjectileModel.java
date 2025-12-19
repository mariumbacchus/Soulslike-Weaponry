package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class DarkMoonProjectileModel extends GeoModel<MoonlightProjectile> {

    @Override
    public Identifier getAnimationResource(MoonlightProjectile animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(MoonlightProjectile moonlightProjectile, @Nullable GeoRenderer<MoonlightProjectile> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/moonlight_projectile_big.geo.json");
    }

    @Override
    public Identifier getTextureResource(MoonlightProjectile moonlightProjectile, @Nullable GeoRenderer<MoonlightProjectile> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/dark_moon_projectile.png");
    }
}
