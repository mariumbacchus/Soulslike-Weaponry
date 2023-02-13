package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.RemnantModel;
import net.soulsweaponry.entity.mobs.Remnant;

public class RemnantFeatureRenderer<T extends Remnant> extends FeatureRenderer<T, RemnantModel<T>>{
    private static final Identifier SKIN = new Identifier(SoulsWeaponry.ModId, "textures/entity/remnant_outer.png");
    private final RemnantModel<T> model;

    public RemnantFeatureRenderer(FeatureRendererContext<T, RemnantModel<T>> context, EntityModelLoader loader) {
        super(context);
        this.model = new RemnantModel<> (loader.getModelPart(EntityModelLayers.DROWNED_OUTER));
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T entity, float f, float g, float h, float j, float k, float l) {
        render(this.getContextModel(), this.model, SKIN, matrixStack, vertexConsumerProvider, i, entity, f, g, j, k, l, h, 1.0F, 1.0F, 1.0F);
    }
}
