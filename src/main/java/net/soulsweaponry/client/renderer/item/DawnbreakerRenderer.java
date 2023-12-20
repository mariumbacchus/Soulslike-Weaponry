package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.DawnbreakerModel;
import net.soulsweaponry.items.Dawnbreaker;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class DawnbreakerRenderer extends GeoItemRenderer<Dawnbreaker>{

    public DawnbreakerRenderer() {
        super(new DawnbreakerModel());
    }
    
}
