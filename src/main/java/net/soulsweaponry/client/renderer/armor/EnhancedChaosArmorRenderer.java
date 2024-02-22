package net.soulsweaponry.client.renderer.armor;

import net.soulsweaponry.client.model.armor.EnhancedChaosArmorModel;
import net.soulsweaponry.items.ChaosSet;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class EnhancedChaosArmorRenderer extends GeoArmorRenderer<ChaosSet> {

    public EnhancedChaosArmorRenderer() {
        super(new EnhancedChaosArmorModel());
    }
}