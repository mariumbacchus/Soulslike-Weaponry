package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.LeviathanAxeModel;
import net.soulsweaponry.items.LeviathanAxe;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class LeviathanAxeRenderer extends GeoItemRenderer<LeviathanAxe> {

    public LeviathanAxeRenderer() {
        super(new LeviathanAxeModel());
    }
    
}
