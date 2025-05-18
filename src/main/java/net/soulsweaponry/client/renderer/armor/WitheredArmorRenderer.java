package net.soulsweaponry.client.renderer.armor;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.armor.WitheredArmorModel;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WitheredArmorRenderer<T extends Item & GeoItem> extends GeoArmorRenderer<T> {

    public WitheredArmorRenderer() {
        super(new WitheredArmorModel<>());
    }

    @Override
    public RenderLayer getRenderType(T animatable, Identifier texture, @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTextureLocation(animatable));
    }
}