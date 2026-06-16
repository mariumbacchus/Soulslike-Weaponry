package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.model.entity.mobs.DayStalkerModel;
import net.soulsweaponry.entity.mobs.boss.DayStalker;

import java.awt.*;

public class DayStalkerRenderer extends GeoEntityRendererWithDeathlight<DayStalker> {

    private static final Color COLOR_ONE = new Color(252, 34, 34);
    private static final Color COLOR_TWO = new Color(250, 186, 132);
    private static final Color COLOR_THREE = new Color(72, 63, 98);
    private static final Color COLOR_FOUR = new Color(40, 34, 59);
    private static final Vec3d DEATH_LIGHT_TRANSLATION = new Vec3d(0, 4, 0);

    public DayStalkerRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DayStalkerModel());
        this.shadowRadius = 1F;
    }

    @Override
    public RenderLayer getRenderType(DayStalker animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }

    @Override
    protected int getDeathTicks(DayStalker entity) {
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