package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.ForlornScytheModel;
import net.soulsweaponry.items.ForlornScythe;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ForlornScytheRenderer extends GeoItemRenderer<ForlornScythe> {

    public ForlornScytheRenderer() {
        super(new ForlornScytheModel());
    }
    
}
