package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.boss.ChaosMonarch;

public class ChaosMonarchModel extends BossGeoEntityModel<ChaosMonarch> {

    public ChaosMonarchModel() {
        super(Identifier.of(SoulsWeaponry.ModId, "chaos_monarch"), true);
    }
}
