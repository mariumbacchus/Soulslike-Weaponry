package net.soulsweaponry.client.renderer.armor;

import net.soulsweaponry.client.model.armor.EChaosArmorModel;
import net.soulsweaponry.items.armor.ChaosSet;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class EChaosArmorRenderer extends GeoArmorRenderer<ChaosSet> {

    public EChaosArmorRenderer() {
        super(new EChaosArmorModel());
    }
}