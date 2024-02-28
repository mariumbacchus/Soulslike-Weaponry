package net.soulsweaponry.client.model.armor;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.armor.WitheredArmor;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class WitheredArmorModel extends AnimatedGeoModel<WitheredArmor> {

    @Override
    public Identifier getAnimationFileLocation(WitheredArmor animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/withered_armor.animation.json");
    }

    @Override
    public Identifier getModelLocation(WitheredArmor object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/withered_armor.geo.json");
    }

    @Override
    public Identifier getTextureLocation(WitheredArmor object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/armor/withered_armor.png");
    }
    
}
