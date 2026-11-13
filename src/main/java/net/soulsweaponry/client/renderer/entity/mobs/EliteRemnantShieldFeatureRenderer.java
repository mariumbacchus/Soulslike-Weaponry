package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.mobs.EliteRemnantModel;
import net.soulsweaponry.entity.mobs.EliteRemnant;

public class EliteRemnantShieldFeatureRenderer extends FeatureRenderer<EliteRemnant, EliteRemnantModel> {

    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/creeper/creeper_armor.png");
    private final EliteRemnantModel model;

    public EliteRemnantShieldFeatureRenderer(FeatureRendererContext<EliteRemnant, EliteRemnantModel> context) {
        super(context);
        this.model = new EliteRemnantModel(TexturedModelData.of(BipedEntityModel.getModelData(
                new Dilation(1.2F), 0F), 64, 64).createModel());
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EliteRemnant entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        float opacity = entity.getShieldOpacity(tickDelta);
        if (opacity <= 0F || entity.isInvisible()) {
            return;
        }
        // Copy the final animated pose, including the eating animation
        this.getContextModel().copyBipedStateTo(this.model);
        this.model.setVisible(true);
        this.model.hat.visible = false;

        float age = entity.age + tickDelta;
        float scroll = age * 0.01F % 1F;

        VertexConsumer vertices = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(TEXTURE, scroll, scroll));

        // Fade the RGB contribution for the additive energy effect
        // 128 matches vanilla's approximately 50% gray tint at full strength
        int brightness = Math.round(128F * opacity);
        int color = 0xFF000000 | (brightness << 16) | (brightness << 8) | brightness;
        this.model.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV, color);
    }
}