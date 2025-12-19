package net.soulsweaponry.client.renderer.entity.mobs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.BigChungusModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;
import net.soulsweaponry.entity.mobs.BigChungus;

@Environment(EnvType.CLIENT)
public class BigChungusRenderer extends MobEntityRenderer<BigChungus, BigChungusRenderer.BigChungusRenderState, BigChungusModel> {

    public BigChungusRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BigChungusModel(ctx.getPart(EntityModelLayerModRegistry.BIG_CHUNGUS_LAYER)), 0.5f);
        this.addFeature(new RedEyesOverlay(this));
    }

    @Override
    public BigChungusRenderState createRenderState() { // render state contains all the vars we need in the renderer
        return new BigChungusRenderState();
    }

    @Override
    public void updateRenderState(BigChungus entity, BigChungusRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);

        // copy values into render state
        state.chungusState = entity.getState();
        state.aggressive = entity.isAggressive();
    }

    @Override
    public Identifier getTexture(BigChungusRenderState state) {
        String prefix = (state.chungusState == BigChungus.ChungusStates.NORMAL) ? "" : state.chungusState.name().toLowerCase() + "_";
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/chungus/" + prefix + "big_chungus.png");
    }

    public static class BigChungusRenderState extends LivingEntityRenderState {
        public BigChungus.ChungusStates chungusState = BigChungus.ChungusStates.NORMAL;
        public boolean aggressive = false;
    }

    @Environment(EnvType.CLIENT)
    public static class RedEyesOverlay extends EyesFeatureRenderer<BigChungusRenderState, BigChungusModel> {

        private static final Identifier RED_EYES_TEXTURE = Identifier.of(SoulsWeaponry.ModId, "textures/entity/chungus/red_eyes_overlay.png");

        public RedEyesOverlay(BigChungusRenderer renderer) {
            super(renderer);
        }

        @Override
        public RenderLayer getEyesTexture() {
            return RenderLayer.getEyes(RED_EYES_TEXTURE);
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, BigChungusRenderState state, float limbAngle, float limbDistance) {
            if (state.aggressive) {
                super.render(matrices, vertexConsumers, light, state, limbAngle, limbDistance);
            }
        }
    }
}