package net.soulsweaponry.client.renderer.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.arrow.SilverArrow;

@Environment(EnvType.CLIENT)
public class SilverArrowRenderer extends ProjectileEntityRenderer<SilverArrow, ProjectileEntityRenderState> {

    private static final Identifier TEXTURE = Identifier.of(SoulsWeaponry.ModId, "textures/entity/silver_arrow.png");

    public SilverArrowRenderer(Context context) {
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