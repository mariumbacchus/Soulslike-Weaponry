package net.soulsweaponry.client.renderer.entity.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.client.model.entity.projectile.DragonslayerSwordspearModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;
import net.soulsweaponry.entity.projectile.DragonslayerSwordspearEntity;

@Environment(EnvType.CLIENT)
public class DragonslayerSwordspearRenderer extends EntityRenderer<DragonslayerSwordspearEntity, DragonslayerSwordspearRenderer.DragonslayerSwordspearRenderState> {

    public static final Identifier TEXTURE = Identifier.of("soulsweapons", "textures/entity/dragonslayer_swordspear.png");

    private final DragonslayerSwordspearModel model;

    public DragonslayerSwordspearRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new DragonslayerSwordspearModel(context.getPart(EntityModelLayerModRegistry.DRAGONSLAYER_SWORDSPEAR_LAYER));
    }

    @Override
    public DragonslayerSwordspearRenderState createRenderState() {
        return new DragonslayerSwordspearRenderState();
    }

    @Override
    public void updateRenderState(DragonslayerSwordspearEntity entity, DragonslayerSwordspearRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.yawDeg = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F;
        state.pitchDeg = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) + 90.0F;
        state.enchanted = entity.isEnchanted();
    }

    @Override
    public void render(DragonslayerSwordspearRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yawDeg));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.pitchDeg));

        VertexConsumer vc = ItemRenderer.getItemGlintConsumer(
                vertexConsumers,
                RenderLayer.getEntityCutout(TEXTURE),
                false,
                state.enchanted
        );

        this.model.render(matrices, vc, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
        matrices.pop();

        super.render(state, matrices, vertexConsumers, light);
    }

    public static class DragonslayerSwordspearRenderState extends EntityRenderState {
        public float yawDeg;
        public float pitchDeg;
        public boolean enchanted;
    }
}