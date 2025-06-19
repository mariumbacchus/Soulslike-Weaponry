package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.MjolnirItemModel;
import net.soulsweaponry.items.hammer.Mjolnir;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class MjolnirItemRenderer extends GeoItemRenderer<Mjolnir> {

    public MjolnirItemRenderer() {
        super (new MjolnirItemModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
