package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.SoulReaperGhostModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;
import net.soulsweaponry.entity.mobs.SoulReaperGhost;
import org.jetbrains.annotations.Nullable;

public class SoulReaperGhostRenderer extends SoulReaperGhostParentRenderer<SoulReaperGhost, SoulReaperGhostModel<SoulReaperGhost>> {
    private static final Identifier TEXTURE = new Identifier(SoulsWeaponry.ModId, "textures/entity/soul_reaper_ghost.png");

    public SoulReaperGhostRenderer(Context context) {
        super(context, new SoulReaperGhostModel<>(context.getPart(EntityModelLayerModRegistry.SOUL_REAPER_GHOST_LAYER)),
                new SoulReaperGhostModel<>(context.getPart(EntityModelLayerModRegistry.SOUL_REAPER_GHOST_INNER_ARMOR)),
                new SoulReaperGhostModel<>(context.getPart(EntityModelLayerModRegistry.SOUL_REAPER_GHOST_OUTER_ARMOR)));
    }

    @Override
    public Identifier getTexture(SoulReaperGhost var1) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(SoulReaperGhost entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderLayer(entity, showBody, true, showOutline);
    }
}
