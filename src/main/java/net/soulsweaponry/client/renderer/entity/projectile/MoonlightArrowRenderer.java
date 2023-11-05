package net.soulsweaponry.client.renderer.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.soulsweaponry.entity.projectile.MoonlightArrow;

@Environment(EnvType.CLIENT)
public class MoonlightArrowRenderer extends ProjectileEntityRenderer<MoonlightArrow> {

    private static final Identifier TEXTURE = new Identifier("soulsweapons", "textures/entity/moonlight_arrow.png");

    public MoonlightArrowRenderer(Context context) {
        super(context);
    }

    public Identifier getTexture(MoonlightArrow entity) {
        return TEXTURE;
    }
}