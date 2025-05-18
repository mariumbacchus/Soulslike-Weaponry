package net.soulsweaponry.client.renderer.armor;

import net.minecraft.item.Item;
import net.soulsweaponry.client.model.armor.ChaosSetModel;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ChaosSetRenderer<T extends Item & GeoItem> extends GeoArmorRenderer<T> {

    public ChaosSetRenderer() {
        super(new ChaosSetModel<>());
    }
}
