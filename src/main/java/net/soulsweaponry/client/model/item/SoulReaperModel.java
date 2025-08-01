package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.scythe.SoulReaper;
import software.bernie.geckolib.model.GeoModel;

public class SoulReaperModel extends GeoModel<SoulReaper> {

    @Override
    public Identifier getAnimationResource(SoulReaper animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/soul_reaper.animation.json");
    }

    @Override
    public Identifier getModelResource(SoulReaper object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/soul_reaper.geo.json");
    }

    @Override
    public Identifier getTextureResource(SoulReaper object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/soul_reaper.png");
    }
    
}
