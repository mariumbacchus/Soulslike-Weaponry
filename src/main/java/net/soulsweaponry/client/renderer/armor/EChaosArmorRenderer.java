package net.soulsweaponry.client.renderer.armor;

import net.minecraft.item.Item;
import net.soulsweaponry.client.model.armor.EChaosArmorModel;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class EChaosArmorRenderer<T extends Item & GeoItem> extends GeoArmorRenderer<T> {

    public EChaosArmorRenderer() {
        super(new EChaosArmorModel<>());
    }
}