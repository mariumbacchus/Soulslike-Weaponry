package net.soulsweaponry.client.renderer.entity.mobs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.ForlornModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;
import net.soulsweaponry.entity.mobs.Forlorn;

@Environment(EnvType.CLIENT)
public class ForlornRenderer extends BipedEntityRenderer<Forlorn, ForlornRenderer.ForlornRenderState, ForlornModel> {

    public ForlornRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ForlornModel(ctx.getPart(EntityModelLayerModRegistry.FORLORN_LAYER)), 0.7F);
        BipedEntityModel<ForlornRenderState> innerArmor = new BipedEntityModel<>(ctx.getPart(EntityModelLayerModRegistry.FORLORN_INNER_ARMOR));
        BipedEntityModel<ForlornRenderState> outerArmor = new BipedEntityModel<>(ctx.getPart(EntityModelLayerModRegistry.FORLORN_OUTER_ARMOR));
        this.addFeature(new ArmorFeatureRenderer<>(this, innerArmor, outerArmor, ctx.getEquipmentRenderer()));
    }

    @Override
    public ForlornRenderState createRenderState() {
        return new ForlornRenderState();
    }

    @Override
    public void updateRenderState(Forlorn entity, ForlornRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.holdingCrossbow = entity.getMainHandStack().isOf(Items.CROSSBOW);
    }

    @Override
    public Identifier getTexture(ForlornRenderState state) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/dark_sorcerer.png");
    }

    public static class ForlornRenderState extends BipedEntityRenderState {
        public boolean holdingCrossbow;
    }
}