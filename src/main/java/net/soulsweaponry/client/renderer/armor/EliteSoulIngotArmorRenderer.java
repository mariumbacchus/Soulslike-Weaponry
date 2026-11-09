package net.soulsweaponry.client.renderer.armor;

import net.minecraft.item.Item;
import net.soulsweaponry.client.model.armor.EliteSoulIngotArmorModel;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class EliteSoulIngotArmorRenderer<T extends Item & GeoItem> extends GeoArmorRenderer<T> {

    public EliteSoulIngotArmorRenderer() {
        super(new EliteSoulIngotArmorModel<>());
    }
}
