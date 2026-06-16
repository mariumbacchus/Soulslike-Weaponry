package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.boss.Moonknight;

public class MoonknightModel extends BossGeoEntityModel<Moonknight> {

    public MoonknightModel() {
        super(Identifier.of(SoulsWeaponry.ModId, "moonknight"), true);
    }

    @Override
    public Identifier getTextureResource(Moonknight object) {
        String phase = object.isPhaseTwo() || object.initiatedPhaseTwo() ? "phase_2" : "phase_1";
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/moonknight/moonknight_" + phase + ".png");
    }
}
