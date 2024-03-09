package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.ChaosMonarch;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ChaosMonarchModel extends DefaultedEntityGeoModel<ChaosMonarch> {

    public ChaosMonarchModel() {
        super(new Identifier(SoulsWeaponry.ModId, "chaos_monarch"), true);
    }
}
