package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.SoulReaperModel;
import net.soulsweaponry.items.SoulReaper;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class SoulReaperRenderer extends GeoItemRenderer<SoulReaper>{

    public SoulReaperRenderer() {
        super(new SoulReaperModel());
    }
    
}
