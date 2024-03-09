package net.soulsweaponry.client.renderer.armor;

import net.soulsweaponry.client.model.armor.ChaosSetModel;
import net.soulsweaponry.items.armor.ChaosSet;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ChaosSetRenderer extends GeoArmorRenderer<ChaosSet> {

    public ChaosSetRenderer() {
        super(new ChaosSetModel());
    }
    
}
