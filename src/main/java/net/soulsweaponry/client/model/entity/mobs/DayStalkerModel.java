package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.boss.DayStalker;

public class DayStalkerModel extends BossGeoEntityModel<DayStalker> {

    public DayStalkerModel() {
        super(Identifier.of(SoulsWeaponry.ModId, "day_stalker"), true);
    }
}
