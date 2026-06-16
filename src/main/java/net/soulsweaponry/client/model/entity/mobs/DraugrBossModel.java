package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.boss.DraugrBoss;

public class DraugrBossModel extends BossGeoEntityModel<DraugrBoss> {

    public DraugrBossModel() {
        super(Identifier.of(SoulsWeaponry.ModId, "draugr_boss"), true);
    }
}
