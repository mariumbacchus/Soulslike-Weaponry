package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.soulsweaponry.client.model.entity.mobs.SoulReaperGhostModel;
import net.soulsweaponry.entity.mobs.SoulReaperGhost;

public abstract class SoulReaperGhostParentRenderer<T extends SoulReaperGhost, M extends SoulReaperGhostModel<T>> extends BipedEntityRenderer<T, M> {

    public SoulReaperGhostParentRenderer(Context ctx, M model, M legsArmorModel, M bodyArmorModel) {
        super(ctx, model, 0.5F);
        this.addFeature(new ArmorFeatureRenderer<>(this, legsArmorModel, bodyArmorModel, ctx.getModelManager()));
    }
}