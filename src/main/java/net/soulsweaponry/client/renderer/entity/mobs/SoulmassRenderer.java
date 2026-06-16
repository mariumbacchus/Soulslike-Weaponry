package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.model.entity.mobs.SoulmassModel;
import net.soulsweaponry.entity.mobs.Soulmass;

import java.awt.Color;

public class SoulmassRenderer extends GeoEntityRendererWithDeathlight<Soulmass> {

    private static final Color COLOR_ONE = new Color(13, 2, 125);
    private static final Color COLOR_TWO = new Color(20, 0, 237);
    private static final Color COLOR_THREE = new Color(102, 88, 252);
    private static final Color COLOR_FOUR = new Color(13, 3, 128);
    private static final Vec3d DEATH_LIGHT_TRANSLATION = new Vec3d(0, 2.5, 0);

    public SoulmassRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SoulmassModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    protected int getDeathTicks(Soulmass entity) {
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