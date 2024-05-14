package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.DraupnirSpearItemModel;
import net.soulsweaponry.items.DraupnirSpear;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class DraupnirSpearItemRenderer extends GeoItemRenderer<DraupnirSpear> {

    public DraupnirSpearItemRenderer() {
        super(new DraupnirSpearItemModel());
    }
    
}
