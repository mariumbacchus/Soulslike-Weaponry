package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.ForlornScytheModel;
import net.soulsweaponry.items.ForlornScythe;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ForlornScytheRenderer extends GeoItemRenderer<ForlornScythe>{

    public ForlornScytheRenderer() {
        super(new ForlornScytheModel());
    }
    
}
