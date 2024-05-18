package net.soulsweaponry.client.registry;

import net.soulsweaponry.client.renderer.armor.ChaosArmorRenderer;
import net.soulsweaponry.client.renderer.armor.ChaosSetRenderer;
import net.soulsweaponry.client.renderer.armor.EChaosArmorRenderer;
import net.soulsweaponry.client.renderer.armor.WitheredArmorRenderer;
import net.soulsweaponry.items.armor.*;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class ArmorRenderRegistry {

    public static void register() {
        GeoArmorRenderer.registerArmorRenderer(ChaosSet.class, ChaosSetRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(ChaosArmor.class, ChaosArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(EChaosArmor.class, EChaosArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(WitheredArmor.class, WitheredArmorRenderer::new);
    }
}
