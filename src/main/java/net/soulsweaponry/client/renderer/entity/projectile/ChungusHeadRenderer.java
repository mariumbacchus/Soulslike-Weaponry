package net.soulsweaponry.client.renderer.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.projectile.ChungusHeadModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;

@Environment(EnvType.CLIENT)
public class ChungusHeadRenderer extends EntityRenderer<TntEntity, ChungusHeadModel.ChungusHeadRenderState> {

    private static final Identifier TEXTURE = Identifier.of(SoulsWeaponry.ModId, "textures/entity/chungus/big_chungus.png");
    private static final Identifier RED_EYES = Identifier.of(SoulsWeaponry.ModId, "textures/entity/chungus/red_eyes_overlay.png");

    private final ChungusHeadModel model;

    public ChungusHeadRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new ChungusHeadModel(ctx.getPart(EntityModelLayerModRegistry.CHUNGUS_HEAD_LAYER));
    }

    @Override
    public ChungusHeadModel.ChungusHeadRenderState createRenderState() {
        return new ChungusHeadModel.ChungusHeadRenderState();
    }

    @Override
    public void updateRenderState(TntEntity entity, ChungusHeadModel.ChungusHeadRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.yawDeg = entity.getYaw(tickDelta);
        state.pitchDeg = entity.getPitch(tickDelta);
    }

    @Override
    public void render(ChungusHeadModel.ChungusHeadRenderState state, MatrixStack matrices, VertexConsumerProvider vcp, int light) {
        matrices.push();
        matrices.translate(0.0F, 1.25F, 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-state.yawDeg));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F + state.pitchDeg));

        VertexConsumer base = vcp.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
        this.model.render(matrices, base, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);

        VertexConsumer eyes = vcp.getBuffer(RenderLayer.getEyes(RED_EYES));
        this.model.render(matrices, eyes, 0xF000F0, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);

        matrices.pop();
        super.render(state, matrices, vcp, light);
    }
}