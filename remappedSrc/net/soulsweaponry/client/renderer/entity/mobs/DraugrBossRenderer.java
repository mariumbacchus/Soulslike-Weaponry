package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.soulsweaponry.client.model.entity.mobs.DraugrBossModel;
import net.soulsweaponry.entity.mobs.DraugrBoss;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DraugrBossRenderer extends GeoEntityRenderer<DraugrBoss> {

    public DraugrBossRenderer(Context ctx) {
        super(ctx, new DraugrBossModel());
        this.shadowRadius = 0.7F;
    }
    
    @Override
    protected float getDeathMaxRotation(DraugrBoss entityLivingBaseIn) {
        return 0f;
    }
}
