package net.soulsweaponry.client.renderer.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.arrow.SilverArrow;

@Environment(EnvType.CLIENT)
public class SilverArrowRenderer extends ProjectileEntityRenderer<SilverArrow> {

    private static final Identifier TEXTURE = new Identifier(SoulsWeaponry.ModId, "textures/entity/silver_arrow.png");

    public SilverArrowRenderer(Context context) {
        super(context);
    }

    public Identifier getTexture(SilverArrow entity) {
        return TEXTURE;
    }
}