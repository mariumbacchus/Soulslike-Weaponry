package net.soulsweaponry.client.renderer.item;

import net.soulsweaponry.client.model.item.LargeMoonlightSwordModel;
import net.soulsweaponry.items.sword.LargeMoonlightSword;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class LargeMoonlightSwordRenderer extends GeoItemRenderer<LargeMoonlightSword> {

    public LargeMoonlightSwordRenderer() {
        super(new LargeMoonlightSwordModel());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
        this.addRenderLayer(new MoonlightBladeShaderLayer(this));
    }

}
