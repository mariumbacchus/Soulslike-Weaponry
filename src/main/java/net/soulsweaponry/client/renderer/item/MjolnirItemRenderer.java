package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.MjolnirItemModel;
import net.soulsweaponry.items.Mjolnir;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MjolnirItemRenderer extends GeoItemRenderer<Mjolnir> {
    public MjolnirItemRenderer() {
        super (new MjolnirItemModel());
    }
}
