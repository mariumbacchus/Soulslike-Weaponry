package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.BloodlustModel;
import net.soulsweaponry.client.model.item.TonitrusModel;
import net.soulsweaponry.items.hammer.Tonitrus;
import net.soulsweaponry.items.katana.Bloodlust;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BloodlustRenderer extends GeoItemRenderer<Bloodlust> {

    public BloodlustRenderer() {
        super(new BloodlustModel());
    }
    
}
