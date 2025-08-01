package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.Soulmass;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SoulmassModel extends DefaultedEntityGeoModel<Soulmass> {

    public SoulmassModel() {
        super(Identifier.of(SoulsWeaponry.ModId, "soulmass"), true);
    }
}
