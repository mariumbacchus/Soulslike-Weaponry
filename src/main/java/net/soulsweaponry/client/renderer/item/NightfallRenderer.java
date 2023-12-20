package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.NightfallModel;
import net.soulsweaponry.items.Nightfall;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class NightfallRenderer extends GeoItemRenderer<Nightfall>{

    public NightfallRenderer() {
        super(new NightfallModel());
    }
    
}
