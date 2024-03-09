package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.FreyrSwordItemModel;
import net.soulsweaponry.items.FreyrSword;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FreyrSwordItemRenderer extends GeoItemRenderer<FreyrSword> {

    public FreyrSwordItemRenderer() {
        super(new FreyrSwordItemModel());
    }
    
}
