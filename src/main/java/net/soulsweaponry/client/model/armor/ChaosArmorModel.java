package net.soulsweaponry.client.model.armor;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.armor.ChaosSet;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ChaosArmorModel extends AnimatedGeoModel<ChaosSet> {

    @Override
    public Identifier getAnimationFileLocation(ChaosSet animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/chaos_armor.animation.json");
    }

    @Override
    public Identifier getModelLocation(ChaosSet object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/chaos_armor.geo.json");
    }

    @Override
    public Identifier getTextureLocation(ChaosSet object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/armor/chaos_armor.png");
    }
}