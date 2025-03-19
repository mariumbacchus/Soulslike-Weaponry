package net.soulsweaponry.client.registry;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.soulsweaponry.mixin.RenderPhaseAccessor;

public class RenderLayerRegistry {

    public static final RenderLayer NIGHT_PROWLER_PORTAL = RenderLayer.of(
            "night_prowler_portal",
            VertexFormats.POSITION,
            VertexFormat.DrawMode.QUADS,
            256,
            false,
            false,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(RenderPhaseAccessor.getEndPortalProgram())
                    .texture(
                            RenderPhase.Textures.create()
                                    // 2-3x .add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false) alone gives blue-white-ish dimension looking like an orbital strike preparing, kinda cool
                                    //.add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false)
                                    //.add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false)
                                    .add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false)
                                    .add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false)
                                    .build()
                    )
                    .build(false)
    );
}
