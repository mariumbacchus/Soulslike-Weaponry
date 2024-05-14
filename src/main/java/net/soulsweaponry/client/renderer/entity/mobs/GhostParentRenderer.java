package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.RemnantModel;
import net.soulsweaponry.entity.mobs.Remnant;

public class GhostParentRenderer<T extends Remnant, M extends RemnantModel<T>> extends BipedEntityRenderer<T, M> {

    public GhostParentRenderer(Context ctx, M model, M legsArmorModel, M bodyArmorModel) {
        super(ctx, model, 0.5F);
        this.addFeature(new ArmorFeatureRenderer<>(this, legsArmorModel, bodyArmorModel));
    }

    public Identifier getTexture(Remnant entity) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/remnant.png");
    }
}
