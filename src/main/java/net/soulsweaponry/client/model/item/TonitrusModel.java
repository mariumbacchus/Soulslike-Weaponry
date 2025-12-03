package net.soulsweaponry.client.model.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.hammer.Tonitrus;
import net.soulsweaponry.registry.EffectRegistry;
import software.bernie.geckolib.model.GeoModel;

public class TonitrusModel extends GeoModel<Tonitrus> {

    private static final Identifier BASE  = new Identifier(SoulsWeaponry.ModId, "textures/item/tonitrus.png");
    private static final Identifier GLOW  = new Identifier(SoulsWeaponry.ModId, "textures/item/tonitrus_ani.png");

    @Override
    public Identifier getAnimationResource(Tonitrus animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/tonitrus.animation.json");
    }

    @Override
    public Identifier getModelResource(Tonitrus object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/tonitrus.geo.json");
    }

    @Override
    public Identifier getTextureResource(Tonitrus object) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && player.hasStatusEffect(EffectRegistry.STORMVEIL.get())) {
            return GLOW;
        }
        return BASE;
    }
}