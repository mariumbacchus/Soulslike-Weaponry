package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.SoulReaperGhostModel;
import net.soulsweaponry.entity.mobs.SoulReaperGhost;

public class SoulReaperGhostRenderer extends MobEntityRenderer<SoulReaperGhost, SoulReaperGhostModel<SoulReaperGhost>> {
    private static final Identifier TEXTURE = new Identifier(SoulsWeaponry.ModId, "textures/entity/empty.png");

    public SoulReaperGhostRenderer(Context context, SoulReaperGhostModel<SoulReaperGhost> entityModel, float f) {
        super(context, new SoulReaperGhostModel<>(context.getPart(EntityModelLayers.ZOMBIE)), 0.5f);
        this.addFeature(new SoulReaperGhostFeatureRenderer<SoulReaperGhost>(this, context.getModelLoader()));
    }

    @Override
    public Identifier getTexture(SoulReaperGhost var1) {
        return TEXTURE;
    }
    
}
