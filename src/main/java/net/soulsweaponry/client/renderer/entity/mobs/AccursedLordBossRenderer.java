package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.model.entity.mobs.AccursedLordBossModel;
import net.soulsweaponry.entity.mobs.boss.AccursedLordBoss;

import java.awt.Color;

public class AccursedLordBossRenderer extends GeoEntityRendererWithDeathlight<AccursedLordBoss> {

    private static final Color COLOR_ONE = new Color(247, 94, 94);
    private static final Color COLOR_TWO = new Color(140, 1, 1);
    private static final Color COLOR_THREE = new Color(209, 0, 0);
    private static final Color COLOR_FOUR = new Color(110, 1, 1);
    private static final Vec3d DEATH_LIGHT_TRANSLATION = new Vec3d(0, 3, 0);

    public AccursedLordBossRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new AccursedLordBossModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    protected int getDeathTicks(AccursedLordBoss entity) {
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