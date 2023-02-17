package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.NightProwler;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class NightProwlerModel extends DefaultedEntityGeoModel<NightProwler> {

    public NightProwlerModel() {
        super(new Identifier(SoulsWeaponry.ModId, "night_prowler"), true);
    }
}
