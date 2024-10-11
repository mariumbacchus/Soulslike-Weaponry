package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.DarkSorcererModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;
import net.soulsweaponry.entity.mobs.DarkSorcerer;

public class DarkSorcererRenderer extends DarkSorcererParentRenderer<DarkSorcerer, DarkSorcererModel<DarkSorcerer>>{

    public DarkSorcererRenderer(Context context) {
        super(context, new DarkSorcererModel<>(context.getPart(EntityModelLayerModRegistry.DARK_SORCERER_LAYER)),
                new DarkSorcererModel<>(context.getPart(EntityModelLayerModRegistry.DARK_SORCERER_INNER_ARMOR)), new DarkSorcererModel<>(context.getPart(EntityModelLayerModRegistry.DARK_SORCERER_OUTER_ARMOR)));
    }

    public Identifier getTexture(DarkSorcerer remnant) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/dark_sorcerer_merged.png");
    }
}
