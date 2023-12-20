package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.CometSpearItemModel;
import net.soulsweaponry.items.CometSpear;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class CometSpearItemRenderer extends GeoItemRenderer<CometSpear> {
    public CometSpearItemRenderer() {
        super (new CometSpearItemModel());
    }
}
