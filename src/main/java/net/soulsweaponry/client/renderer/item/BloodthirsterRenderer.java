package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.BloodthirsterModel;
import net.soulsweaponry.items.Bloodthirster;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class BloodthirsterRenderer extends GeoItemRenderer<Bloodthirster>{

    public BloodthirsterRenderer() {
        super(new BloodthirsterModel());
    }
    
}
