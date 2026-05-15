package net.soulsweaponry.client.registry;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public class ShaderLayerRegistry {

    private static final Identifier MOONLIGHT_BLADE_STARS = Identifier.of(SoulsWeaponry.ModId, "textures/environment/moonlight_blade_stars.png");

    private static final RenderPhase.ShaderProgram MOONLIGHT_BLADE_PORTAL_SHADER = new RenderPhase.ShaderProgram(() -> ShaderRegistry.MOONLIGHT_BLADE_SHADER);

    private static final RenderLayer MOONLIGHT_BLADE_SHADER = RenderLayer.of(
            "soulsweapons_moonlight_blade_shader",
            VertexFormats.POSITION,
            VertexFormat.DrawMode.QUADS,
            1536,
            false,
            true,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(MOONLIGHT_BLADE_PORTAL_SHADER)
                    .texture(RenderPhase.Textures.create()
                            .add(MOONLIGHT_BLADE_STARS, false, false)
                            .build())
                    .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                    .cull(RenderPhase.DISABLE_CULLING)
                    .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
                    .build(false)
    );

    public static RenderLayer moonlightBladeShader() {
        return MOONLIGHT_BLADE_SHADER;
    }
}