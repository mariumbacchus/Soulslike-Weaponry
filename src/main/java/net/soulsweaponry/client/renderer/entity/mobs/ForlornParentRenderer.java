package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.ForlornModel;
import net.soulsweaponry.entity.mobs.Forlorn;

public class ForlornParentRenderer<T extends Forlorn, M extends ForlornModel<T>> extends BipedEntityRenderer<T, M> {

    public ForlornParentRenderer(Context ctx, M model, M legsArmorModel, M bodyArmorModel) {
        super(ctx, model, 0.5F);
        this.addFeature(new ArmorFeatureRenderer<>(this, legsArmorModel, bodyArmorModel, ctx.getModelManager()));
    }

    public Identifier getTexture(T entity) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/forlorn_inner.png");
    }
}
