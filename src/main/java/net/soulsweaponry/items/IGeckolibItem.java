package net.soulsweaponry.items;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Consumer;

// TODO this might work or not, test on one item, if no crash then implement to all
// test item = Mjolnir
public interface IGeckolibItem<T extends Item & GeoItem> extends GeoItem {

    @Override
    default void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoItemRenderer<T> renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = getGeckolibRenderer();

                return this.renderer;
            }
        });
    }

    GeoItemRenderer<T> getGeckolibRenderer();
}
