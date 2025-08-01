package net.soulsweaponry.client.renderer.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.projectile.ChungusHeadModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;

@Environment(EnvType.CLIENT)
public class ChungusHeadRenderer extends EntityRenderer<TntEntity> {

    private static final Identifier TEXTURE = Identifier.of(
            SoulsWeaponry.ModId,
            "textures/entity/chungus/big_chungus.png"
    );
    private static final Identifier RED_EYES = Identifier.of(
            SoulsWeaponry.ModId,
            "textures/entity/chungus/red_eyes_overlay.png"
    );

    private final ChungusHeadModel model;

    public ChungusHeadRenderer(Context ctx) {
        super(ctx);
        this.model = new ChungusHeadModel(ctx.getPart(EntityModelLayerModRegistry.CHUNGUS_HEAD_LAYER));
    }

    @Override
    public void render(TntEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vcp, int light) {
        matrices.push();
        matrices.translate(0, 1.25f, 0);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(- yaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F + entity.getPitch(tickDelta)));
        VertexConsumer base = vcp.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
        this.model.render(
                matrices, base, light,
                OverlayTexture.DEFAULT_UV,
                0xFFFFFFFF
        );
        VertexConsumer eyes = vcp.getBuffer(RenderLayer.getEyes(RED_EYES));
        this.model.render(
                matrices, eyes, 0xF000F0,
                OverlayTexture.DEFAULT_UV,
                0xFFFFFFFF
        );
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vcp, light);
    }

    @Override
    public Identifier getTexture(TntEntity entity) {
        return TEXTURE;
    }
}