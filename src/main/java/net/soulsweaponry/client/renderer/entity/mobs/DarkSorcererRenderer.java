package net.soulsweaponry.client.renderer.entity.mobs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.DarkSorcererModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;
import net.soulsweaponry.entity.mobs.DarkSorcerer;

@Environment(EnvType.CLIENT)
public class DarkSorcererRenderer extends MobEntityRenderer<DarkSorcerer, DarkSorcererRenderer.DarkSorcererRenderState, DarkSorcererModel> {

    public DarkSorcererRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DarkSorcererModel(ctx.getPart(EntityModelLayerModRegistry.DARK_SORCERER_LAYER)), 0.5F);
        var innerArmor = new BipedEntityModel<DarkSorcererRenderState>(ctx.getPart(EntityModelLayerModRegistry.DARK_SORCERER_INNER_ARMOR));
        var outerArmor = new BipedEntityModel<DarkSorcererRenderState>(ctx.getPart(EntityModelLayerModRegistry.DARK_SORCERER_OUTER_ARMOR));
        this.addFeature(new ArmorFeatureRenderer<>(this, innerArmor, outerArmor, ctx.getEquipmentRenderer()));
    }

    @Override
    public DarkSorcererRenderState createRenderState() {
        return new DarkSorcererRenderState();
    }

    @Override
    public void updateRenderState(DarkSorcerer entity, DarkSorcererRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.beaming = entity.getBeaming();
    }

    @Override
    public Identifier getTexture(DarkSorcererRenderState state) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/dark_sorcerer_merged.png");
    }

    public static class DarkSorcererRenderState extends BipedEntityRenderState {
        public boolean beaming;
    }
}