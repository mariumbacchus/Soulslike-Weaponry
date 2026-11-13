package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.EliteRemnantModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;
import net.soulsweaponry.entity.mobs.EliteRemnant;
import org.jetbrains.annotations.Nullable;

public class EliteRemnantRenderer extends GhostParentRenderer<EliteRemnant, EliteRemnantModel> {

    public EliteRemnantRenderer(Context context) {
        super(context,
                new EliteRemnantModel(context.getPart(EntityModelLayerModRegistry.REMNANT_LAYER)),
                new EliteRemnantModel(context.getPart(EntityModelLayerModRegistry.REMNANT_INNER_ARMOR)),
                new EliteRemnantModel(context.getPart(EntityModelLayerModRegistry.REMNANT_OUTER_ARMOR)));
        this.shadowRadius = 0.7F;
        this.addFeature(new EliteRemnantShieldFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(EliteRemnant entity) {//TODO maybe unique texture idk
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/remnant_merged.png");
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(EliteRemnant entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderLayer(entity, showBody, true, showOutline);
    }
}