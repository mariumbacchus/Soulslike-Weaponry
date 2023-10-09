package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.EmpoweredDawnbreakerModel;
import net.soulsweaponry.items.EmpoweredDawnbreaker;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class EmpoweredDawnbreakerRenderer extends GeoItemRenderer<EmpoweredDawnbreaker>{

    public EmpoweredDawnbreakerRenderer() {
        super(new EmpoweredDawnbreakerModel());
    }

}