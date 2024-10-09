package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.RemnantModel;
import net.soulsweaponry.entity.mobs.Remnant;
import org.jetbrains.annotations.Nullable;

public class RemnantRenderer extends GhostParentRenderer<Remnant, RemnantModel<Remnant>>{

    public RemnantRenderer(Context context) {
        super(context, new RemnantModel<>(context.getPart(EntityModelLayers.ZOMBIE)), new RemnantModel<>(context.getPart(EntityModelLayers.ZOMBIE_INNER_ARMOR)), new RemnantModel<>(context.getPart(EntityModelLayers.ZOMBIE_OUTER_ARMOR)));
        this.shadowRadius = 0.7f;
    }

    public Identifier getTexture(Remnant remnant) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/remnant_merged.png");
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(Remnant entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderLayer(entity, showBody, true, showOutline);
    }
}