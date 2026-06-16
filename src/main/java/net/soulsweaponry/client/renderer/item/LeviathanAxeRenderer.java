package net.soulsweaponry.client.renderer.item;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.item.LeviathanAxeModel;
import net.soulsweaponry.items.axe.LeviathanAxe;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class LeviathanAxeRenderer extends GeoItemRenderer<LeviathanAxe> {

    public LeviathanAxeRenderer() {
        super(new LeviathanAxeModel());
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
        addRenderLayer(new AutoGlowingGeoLayer<>(this) {
            @Override
            protected RenderLayer getRenderType(LeviathanAxe animatable) {
                return RenderLayer.getEyes(new Identifier(SoulsWeaponry.ModId, "textures/item/leviathan_axe_glowmask.png"));
            }
        });
    }
}