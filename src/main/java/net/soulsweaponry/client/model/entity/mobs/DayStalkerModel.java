package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.DayStalker;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DayStalkerModel extends DefaultedEntityGeoModel<DayStalker> {

    public DayStalkerModel() {
        super(new Identifier(SoulsWeaponry.ModId, "day_stalker"), true);
    }
}