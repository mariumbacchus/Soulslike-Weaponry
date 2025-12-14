package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.mobs.ChaosMonarchModel;
import net.soulsweaponry.entity.mobs.ChaosMonarch;

import java.awt.*;

public class ChaosMonarchRenderer extends GeoEntityRendererDeathLight<ChaosMonarch> {

    public static final Color DARK_MAGENTA = new Color(160, 14, 131);
    public static final Color DARK_PINK = new Color(160, 102, 149);
    public static final Color PINK = new Color(255, 163, 236);

    public ChaosMonarchRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ChaosMonarchModel(), Color.MAGENTA, DARK_MAGENTA, DARK_PINK, PINK, 4);
        this.shadowRadius = 0.7F;
    }
}
