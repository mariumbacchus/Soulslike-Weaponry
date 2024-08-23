package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.AccursedLordBoss;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AccursedLordBossModel extends DefaultedEntityGeoModel<AccursedLordBoss> {

    public AccursedLordBossModel() {
        super(new Identifier(SoulsWeaponry.ModId, "accursed_lord"), true);
    }
    // This creates a new GeoModel with the following asset paths:
    // Animation Json: assets/mymod/animations/entity/monster/my_entity.animation.json
    // Model Json: assets/mymod/geo/entity/monster/my_entity.geo.json
    // Texture: assets/mymod/textures/entity/monster/my_entity.png

    /*@Override
    public Identifier getModelResource(AccursedLordBoss object)
    {
        return new Identifier(SoulsWeaponry.ModId, "geo/accursed_lord.geo.json");
    }

    @Override
    public Identifier getTextureResource(AccursedLordBoss object)
    {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/accursed_lord.png");
    }

    @Override
    public Identifier getAnimationResource(AccursedLordBoss object)
    {
        return new Identifier(SoulsWeaponry.ModId, "animations/accursed_lord.animation.json");
    }*/
}