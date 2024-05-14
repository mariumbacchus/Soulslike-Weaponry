package net.soulsweaponry.client.renderer.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;

import net.minecraft.util.Identifier;

import net.soulsweaponry.entity.projectile.ChargedArrow;

@Environment(EnvType.CLIENT)
public class ChargedArrowRenderer extends ProjectileEntityRenderer<ChargedArrow> {

    private static final Identifier TEXTURE = new Identifier("soulsweapons", "textures/entity/charged_arrow.png");

    public ChargedArrowRenderer(Context context) {
        super(context);
    }

    public Identifier getTexture(ChargedArrow entity) {
        return TEXTURE;
    }
   
}
