package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.scythe.SoulReaper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class SoulReaperModel extends GeoModel<SoulReaper> {

    @Override
    public Identifier getModelResource(SoulReaper soulReaper, @Nullable GeoRenderer<SoulReaper> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/soul_reaper.geo.json");
    }

    @Override
    public Identifier getTextureResource(SoulReaper soulReaper, @Nullable GeoRenderer<SoulReaper> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/soul_reaper.png");
    }

    @Override
    public Identifier getAnimationResource(SoulReaper animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/soul_reaper.animation.json");
    }
}
