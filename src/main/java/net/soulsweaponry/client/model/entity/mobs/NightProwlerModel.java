package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.NightProwler;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NightProwlerModel extends AnimatedGeoModel<NightProwler> {

    @Override
    public Identifier getModelLocation(NightProwler object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/night_prowler.geo.json");
    }

    @Override
    public Identifier getTextureLocation(NightProwler object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/night_prowler.png");
    }

    @Override
    public Identifier getAnimationFileLocation(NightProwler object) {
        return new Identifier(SoulsWeaponry.ModId, "animations/night_prowler.animation.json");
    }
}
