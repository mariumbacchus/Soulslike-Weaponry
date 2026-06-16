package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.boss.AccursedLordBoss;

public class AccursedLordBossModel extends BossGeoEntityModel<AccursedLordBoss> {

    public AccursedLordBossModel() {
        super(Identifier.of(SoulsWeaponry.ModId, "accursed_lord"), true);
    }
}
