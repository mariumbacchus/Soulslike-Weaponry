package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.DarkSorcererModel;
import net.soulsweaponry.entity.mobs.DarkSorcerer;

public class DarkSorcererParentRenderer<T extends DarkSorcerer, M extends DarkSorcererModel<T>> extends BipedEntityRenderer<T, M> {

    public DarkSorcererParentRenderer(Context ctx, M model, M legsArmorModel, M bodyArmorModel) {
        super(ctx, model, 0.5F);
        this.addFeature(new ArmorFeatureRenderer<>(this, legsArmorModel, bodyArmorModel, ctx.getModelManager()));
    }

    public Identifier getTexture(T entity) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/dark_sorcerer.png");
    }
}
