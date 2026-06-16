package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.boss.NightShade;

public class NightShadeModel extends BossGeoEntityModel<NightShade> {

    public NightShadeModel() {
        super(Identifier.of(SoulsWeaponry.ModId, "night_shade"), true);
    }
}
