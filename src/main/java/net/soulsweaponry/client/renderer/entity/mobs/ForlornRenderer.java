package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.ForlornModel;
import net.soulsweaponry.entity.mobs.Forlorn;

public class ForlornRenderer extends ForlornParentRenderer<Forlorn, ForlornModel<Forlorn>> {

    public ForlornRenderer(Context context) {
        super(context, new ForlornModel<>(context.getPart(EntityModelLayers.ZOMBIE)), new ForlornModel<>(context.getPart(EntityModelLayers.ZOMBIE_INNER_ARMOR)), new ForlornModel<>(context.getPart(EntityModelLayers.ZOMBIE_OUTER_ARMOR)));
        this.shadowRadius = 0.7f;
    }

    public Identifier getTexture(Forlorn remnant) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/dark_sorcerer.png");
    }
}