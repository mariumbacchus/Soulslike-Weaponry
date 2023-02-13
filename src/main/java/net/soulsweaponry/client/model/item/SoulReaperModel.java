package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.SoulReaper;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SoulReaperModel extends AnimatedGeoModel<SoulReaper>{

    @Override
    public Identifier getAnimationResource(SoulReaper animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/soul_reaper.animation.json");
    }

    @Override
    public Identifier getModelResource(SoulReaper object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/soul_reaper.geo.json");
    }

    @Override
    public Identifier getTextureResource(SoulReaper object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/item/soul_reaper.png");
    }
    
}
