package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.boss.NightProwler;

public class NightProwlerModel extends BossGeoEntityModel<NightProwler> {

    public NightProwlerModel() {
        super(Identifier.of(SoulsWeaponry.ModId, "night_prowler"), true);
    }
}
