package net.soulsweaponry.client.renderer.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.soulsweaponry.entity.projectile.KrakenSlayerProjectile;

@Environment(EnvType.CLIENT)
public class KrakenSlayerProjectileRenderer extends ProjectileEntityRenderer<KrakenSlayerProjectile> {

    private static final Identifier TEXTURE = new Identifier("soulsweapons", "textures/entity/kraken_slayer_projectile.png");

    public KrakenSlayerProjectileRenderer(Context context) {
        super(context);
    }

    public Identifier getTexture(KrakenSlayerProjectile entity) {
        return TEXTURE;
    }
}