package net.soulsweaponry.client.renderer.item;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.item.MjolnirItemModel;
import net.soulsweaponry.items.hammer.Mjolnir;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class MjolnirItemRenderer extends GeoItemRenderer<Mjolnir> {

    public MjolnirItemRenderer() {
        super (new MjolnirItemModel());
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
        addRenderLayer(new AutoGlowingGeoLayer<>(this) {
            @Override
            protected RenderLayer getRenderType(Mjolnir animatable) {
                return RenderLayer.getEyes(new Identifier(SoulsWeaponry.ModId, "textures/item/mjolnir_glowmask.png"));
            }
        });
    }
}