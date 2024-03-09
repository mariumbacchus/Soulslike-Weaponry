package net.soulsweaponry.client.model.armor;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.armor.ChaosSet;
import software.bernie.geckolib.model.GeoModel;

public class ChaosArmorModel extends GeoModel<ChaosSet> {

    @Override
    public Identifier getAnimationResource(ChaosSet animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/chaos_armor.animation.json");
    }

    @Override
    public Identifier getModelResource(ChaosSet object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/chaos_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(ChaosSet object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/armor/chaos_armor.png");
    }
    
}
