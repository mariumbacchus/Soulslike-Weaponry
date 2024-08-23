package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.DarkinBladeModel;
import net.soulsweaponry.items.DarkinBlade;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DarkinBladeRenderer extends GeoItemRenderer<DarkinBlade> {

    public DarkinBladeRenderer() {
        super(new DarkinBladeModel());
    }

}