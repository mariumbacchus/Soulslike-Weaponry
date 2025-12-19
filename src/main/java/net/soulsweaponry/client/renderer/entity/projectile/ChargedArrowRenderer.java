package net.soulsweaponry.client.renderer.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.Identifier;
import net.soulsweaponry.entity.projectile.arrow.ChargedArrow;

@Environment(EnvType.CLIENT)
public class ChargedArrowRenderer extends ProjectileEntityRenderer<ChargedArrow, ProjectileEntityRenderState> {

    private static final Identifier TEXTURE = Identifier.of("soulsweapons", "textures/entity/charged_arrow.png");

    public ChargedArrowRenderer(Context context) {
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
