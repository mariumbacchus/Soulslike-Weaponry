package net.soulsweaponry.client.renderer.entity.mobs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.mobs.BigChungusModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;
import net.soulsweaponry.entity.mobs.BigChungus;

@Environment(EnvType.CLIENT)
public class BigChungusRenderer extends MobEntityRenderer<BigChungus, BigChungusModel<BigChungus>> {

    public BigChungusRenderer(Context context) {
        super(context, new BigChungusModel<>(context.getPart(EntityModelLayerModRegistry.BIG_CHUNGUS_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(BigChungus entity) {
        return new Identifier("soulsweapons", "textures/entity/" + (entity.isBosnian() ? "bosnian_big_chungus.png" : "big_chungus.png"));
    }
}
