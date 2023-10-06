package net.soulsweaponry.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.soulsweaponry.util.ParryData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Unique
    private float parryProgress;

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    protected void interceptRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch,
                                                  Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices,
                                                  VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        int frames = ParryData.getParryFrames(player);
        if (frames >= 1) {
            this.parryProgress = frames == 1 ? 0.1f : parryProgress;
            float added = (1f / (float) ParryData.MAX_PARRY_FRAMES) / 6f;
            this.parryProgress = Math.min(this.parryProgress + added, 1f);

            float h;
            float f = (float) ParryData.MAX_PARRY_FRAMES - parryProgress;
            float g = f / (float) ParryData.MAX_PARRY_FRAMES;
            if (g < 0.8f) {
                h = MathHelper.abs(MathHelper.cos(f / 4.0f * (float)Math.PI) * 0.1f);
                matrices.translate(0.0f, h, 0.0f);
            }
            h = 1.0f - (float)Math.pow(g, 27.0);
            matrices.translate(0.9f - h, h / 4f, -h / 4f);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(h * 20f));
        }
    }
}