package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.model.entity.mobs.ReturningKnightModel;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

import java.awt.Color;

public class ReturningKnightRenderer extends GeoEntityRendererWithDeathlight<ReturningKnight> {

    private static final Color COLOR_ONE = new Color(254, 200, 203);
    private static final Color COLOR_TWO = new Color(254, 254, 218);
    private static final Color COLOR_THREE = new Color(106, 73, 156);
    private static final Color COLOR_FOUR = new Color(176, 253, 252);
    private static final Vec3d DEATH_LIGHT_TRANSLATION = new Vec3d(0, 4, 0);

    public ReturningKnightRenderer(Context ctx) {
        super(ctx, new ReturningKnightModel());
        this.shadowRadius = 2.5F;
    }

    @Override
    protected int getDeathTicks(ReturningKnight entity) {
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