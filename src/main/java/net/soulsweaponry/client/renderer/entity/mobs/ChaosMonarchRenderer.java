package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.model.entity.mobs.ChaosMonarchModel;
import net.soulsweaponry.entity.mobs.boss.ChaosMonarch;

import java.awt.Color;

public class ChaosMonarchRenderer extends GeoEntityRendererWithDeathlight<ChaosMonarch> {

    private static final Color COLOR_ONE = new Color(255, 22, 206);
    private static final Color COLOR_TWO = new Color(160, 14, 131);
    private static final Color COLOR_THREE = new Color(160, 102, 149);
    private static final Color COLOR_FOUR = new Color(255, 163, 236);
    private static final Vec3d DEATH_LIGHT_TRANSLATION = new Vec3d(0, 4, 0);

    public ChaosMonarchRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ChaosMonarchModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    protected int getDeathTicks(ChaosMonarch entity) {
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