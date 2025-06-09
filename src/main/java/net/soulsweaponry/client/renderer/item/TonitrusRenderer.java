package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.TonitrusModel;
import net.soulsweaponry.items.hammer.Tonitrus;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TonitrusRenderer extends GeoItemRenderer<Tonitrus> {

    public TonitrusRenderer() {
        super(new TonitrusModel());
    }
    
}
