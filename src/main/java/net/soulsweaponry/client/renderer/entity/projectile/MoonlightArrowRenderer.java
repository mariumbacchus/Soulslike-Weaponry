package net.soulsweaponry.client.renderer.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.arrow.MoonlightArrow;

@Environment(EnvType.CLIENT)
public class MoonlightArrowRenderer extends ProjectileEntityRenderer<MoonlightArrow, ProjectileEntityRenderState> {

    private static final Identifier TEXTURE = Identifier.of(SoulsWeaponry.ModId, "textures/entity/moonlight_arrow.png");

    public MoonlightArrowRenderer(Context context) {
        super(context);
    }

    @Override
    public ProjectileEntityRenderState createRenderState() {
        return new ProjectileEntityRenderState();
    }

    @Override
    protected Identifier getTexture(ProjectileEntityRenderState state) {
        return TEXTURE;
    }
}