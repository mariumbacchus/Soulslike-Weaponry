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
import net.soulsweaponry.client.model.entity.mobs.RemnantModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;
import net.soulsweaponry.entity.mobs.Remnant;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RemnantRenderer extends BipedEntityRenderer<Remnant, RemnantRenderer.RemnantRenderState, RemnantModel> {

    public RemnantRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new RemnantModel(ctx.getPart(EntityModelLayerModRegistry.REMNANT_LAYER)), 0.7F);
        BipedEntityModel<RemnantRenderState> innerArmor = new BipedEntityModel<>(ctx.getPart(EntityModelLayerModRegistry.REMNANT_INNER_ARMOR));
        BipedEntityModel<RemnantRenderState> outerArmor = new BipedEntityModel<>(ctx.getPart(EntityModelLayerModRegistry.REMNANT_OUTER_ARMOR));
        this.addFeature(new ArmorFeatureRenderer<>(this, innerArmor, outerArmor, ctx.getEquipmentRenderer()));
    }

    @Override
    public RemnantRenderState createRenderState() {
        return new RemnantRenderState();
    }

    @Override
    public void updateRenderState(Remnant entity, RemnantRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.attacking = entity.isAttacking();
        state.mainHandEmpty = entity.getMainHandStack().isEmpty();
        state.translucent = true;
    }

    @Override
    protected @Nullable RenderLayer getRenderLayer(RemnantRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderLayer(state, showBody, state.translucent, showOutline);
    }

    @Override
    public Identifier getTexture(RemnantRenderState state) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/remnant_merged.png");
    }

    public static class RemnantRenderState extends BipedEntityRenderState {
        public boolean attacking;
        public boolean mainHandEmpty;
        public boolean translucent;
    }
}