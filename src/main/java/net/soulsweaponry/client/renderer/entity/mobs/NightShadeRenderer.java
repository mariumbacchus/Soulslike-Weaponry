package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.model.entity.mobs.NightShadeModel;
import net.soulsweaponry.entity.mobs.boss.NightShade;

import java.awt.Color;

public class NightShadeRenderer extends GeoEntityRendererWithDeathlight<NightShade> {

    private static final Color COLOR_ONE = new Color(13, 2, 125);
    private static final Color COLOR_TWO = new Color(20, 0, 237);
    private static final Color COLOR_THREE = new Color(102, 88, 252);
    private static final Color COLOR_FOUR = new Color(13, 3, 128);
    private static final Vec3d DEATH_LIGHT_TRANSLATION = new Vec3d(0, 2.5, 0);

    public NightShadeRenderer(Context ctx) {
        super(ctx, new NightShadeModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    protected boolean shouldRenderDeathLight(NightShade entity) {
        return !entity.isCopy();
    }

    @Override
    public RenderLayer getRenderType(NightShade animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }

    @Override
    protected int getDeathTicks(NightShade entity) {
        return entity.getDeathTicks();
    }

    @Override
    protected Vec3d getDeathLightTranslation() {
        return DEATH_LIGHT_TRANSLATION;
    }

    @Override
    protected Color getDeathLightColorOne() {
        return COLOR_ONE;
    }

    @Override
    protected Color getDeathLightColorTwo() {
        return COLOR_TWO;
    }

    @Override
    protected Color getDeathLightColorThree() {
        return COLOR_THREE;
    }

    @Override
    protected Color getDeathLightColorFour() {
        return COLOR_FOUR;
    }
}