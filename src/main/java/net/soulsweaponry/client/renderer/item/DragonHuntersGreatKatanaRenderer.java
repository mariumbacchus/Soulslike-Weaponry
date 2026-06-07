package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.DragonHuntersGreatKatanaModel;
import net.soulsweaponry.items.katana.DragonHuntersGreatKatana;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class DragonHuntersGreatKatanaRenderer extends GeoItemRenderer<DragonHuntersGreatKatana> {

    public DragonHuntersGreatKatanaRenderer() {
        super(new DragonHuntersGreatKatanaModel());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
