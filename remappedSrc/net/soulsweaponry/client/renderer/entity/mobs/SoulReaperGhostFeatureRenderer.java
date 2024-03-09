package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.SoulReaperGhostModel;
import net.soulsweaponry.entity.mobs.SoulReaperGhost;

public class SoulReaperGhostFeatureRenderer<T extends SoulReaperGhost> extends FeatureRenderer<T, SoulReaperGhostModel<T>> {
    private final EntityModel<T> model;

    public SoulReaperGhostFeatureRenderer(FeatureRendererContext<T, SoulReaperGhostModel<T>> context, EntityModelLoader loader) {
        super(context);
        this.model = new SoulReaperGhostModel<T>(loader.getModelPart(EntityModelLayers.DROWNED_OUTER));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean bl = minecraftClient.hasOutline((Entity)livingEntity) && ((Entity)livingEntity).isInvisible();
        if (((Entity)livingEntity).isInvisible() && !bl) {
            return;
        }
        VertexConsumer vertexConsumer = bl ? vertexConsumerProvider.getBuffer(RenderLayer.getOutline(new Identifier(SoulsWeaponry.ModId, "textures/entity/soul_reaper_ghost.png"))) : vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(new Identifier(SoulsWeaponry.ModId, "textures/entity/soul_reaper_ghost.png")));
        ((SoulReaperGhostModel<T>)this.getContextModel()).copyStateTo(this.model);
        this.model.animateModel(livingEntity, f, g, h);
        this.model.setAngles(livingEntity, f, g, j, k, l);
        this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(livingEntity, 0.0f), 1.0f, 1.0f, 1.0f, 1.0f);
    }
}
