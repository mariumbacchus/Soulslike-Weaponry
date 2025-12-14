package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.mobs.AccursedLordBossModel;
import net.soulsweaponry.entity.mobs.AccursedLordBoss;

import java.awt.*;

public class AccursedLordBossRenderer extends GeoEntityRendererDeathLight<AccursedLordBoss> {

    public static final Color LIGHT_RED = new Color(247, 94, 94);
    public static final Color DARK_CRIMSON = new Color(140, 1, 1);
    public static final Color CRIMSON = new Color(209, 0, 0);
    public static final Color DARK_RED = new Color(110, 1, 1);

    public AccursedLordBossRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new AccursedLordBossModel(), LIGHT_RED, DARK_CRIMSON, CRIMSON, DARK_RED, 3);
        this.shadowRadius = 0.7F;
    }
}
