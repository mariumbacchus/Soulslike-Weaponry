package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.WitheredDemon;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class WitheredDemonModel extends DefaultedEntityGeoModel<WitheredDemon> {

    public WitheredDemonModel() {
        super(new Identifier(SoulsWeaponry.ModId, "withered_demon"), true);
    }
}