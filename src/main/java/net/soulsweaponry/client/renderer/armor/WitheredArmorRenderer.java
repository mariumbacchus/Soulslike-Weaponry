package net.soulsweaponry.client.renderer.armor;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.armor.WitheredArmorModel;
import net.soulsweaponry.items.armor.WitheredArmor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WitheredArmorRenderer extends GeoArmorRenderer<WitheredArmor> {

    public WitheredArmorRenderer() {
        super(new WitheredArmorModel());
    }

    @Override
    public RenderLayer getRenderType(WitheredArmor animatable, Identifier texture, @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTextureLocation(animatable));
    }
}