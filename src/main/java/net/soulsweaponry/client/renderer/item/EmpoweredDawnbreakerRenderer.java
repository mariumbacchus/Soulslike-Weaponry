package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.EmpoweredDawnbreakerModel;
import net.soulsweaponry.items.sword.EmpoweredDawnbreaker;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class EmpoweredDawnbreakerRenderer extends GeoItemRenderer<EmpoweredDawnbreaker> {

    public EmpoweredDawnbreakerRenderer() {
        super(new EmpoweredDawnbreakerModel());
    }
}