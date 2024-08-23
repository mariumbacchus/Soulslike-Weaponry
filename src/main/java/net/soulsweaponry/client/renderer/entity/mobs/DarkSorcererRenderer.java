package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.DarkSorcererModel;
import net.soulsweaponry.entity.mobs.DarkSorcerer;

public class DarkSorcererRenderer extends DarkSorcererParentRenderer<DarkSorcerer, DarkSorcererModel<DarkSorcerer>>{

    public DarkSorcererRenderer(Context context) {
        super(context, new DarkSorcererModel<>(context.getPart(EntityModelLayers.ZOMBIE)),
                new DarkSorcererModel<>(context.getPart(EntityModelLayers.ZOMBIE_INNER_ARMOR)), new DarkSorcererModel<>(context.getPart(EntityModelLayers.ZOMBIE_OUTER_ARMOR)));
    }

    public Identifier getTexture(DarkSorcerer remnant) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/dark_sorcerer_merged.png");
    }
}
