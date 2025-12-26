package net.soulsweaponry.client.renderer.entity.mobs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.SoulReaperGhostModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;
import net.soulsweaponry.entity.mobs.SoulReaperGhost;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SoulReaperGhostRenderer extends BipedEntityRenderer<SoulReaperGhost, SoulReaperGhostRenderer.SoulReaperGhostRenderState, SoulReaperGhostModel> {

    private static final Identifier TEXTURE = Identifier.of(SoulsWeaponry.ModId, "textures/entity/soul_reaper_ghost.png");

    public SoulReaperGhostRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SoulReaperGhostModel(ctx.getPart(EntityModelLayerModRegistry.SOUL_REAPER_GHOST_LAYER)), 0.5F);
        BipedEntityModel<SoulReaperGhostRenderState> innerArmor = new BipedEntityModel<>(ctx.getPart(EntityModelLayerModRegistry.SOUL_REAPER_GHOST_INNER_ARMOR));
        BipedEntityModel<SoulReaperGhostRenderState> outerArmor = new BipedEntityModel<>(ctx.getPart(EntityModelLayerModRegistry.SOUL_REAPER_GHOST_OUTER_ARMOR));
        this.addFeature(new ArmorFeatureRenderer<>(this, innerArmor, outerArmor, ctx.getEquipmentRenderer()));
    }

    @Override
    public SoulReaperGhostRenderState createRenderState() {
        return new SoulReaperGhostRenderState();
    }

    @Override
    public void updateRenderState(SoulReaperGhost entity, SoulReaperGhostRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.translucent = true;
    }

    @Override
    public Identifier getTexture(SoulReaperGhostRenderState state) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(SoulReaperGhostRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderLayer(state, showBody, state.translucent, showOutline);
    }

    public static class SoulReaperGhostRenderState extends BipedEntityRenderState {
        public boolean translucent;
    }
}