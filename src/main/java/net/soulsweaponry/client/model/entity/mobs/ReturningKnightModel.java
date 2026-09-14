package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.boss.Moonknight;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public class ReturningKnightModel extends BossGeoEntityModel<ReturningKnight> {

    public ReturningKnightModel() {
        super(Identifier.of(SoulsWeaponry.ModId, "returning_knight"), true);
    }

    @Override
    public String getDebugBoneId() {
        return "hitboxCenter";
    }

    @Override
    public boolean isDebugAttacking(ReturningKnight boss) {
        return false;//boss.isState(ReturningKnight.States.SEISMIC_WAVE);
    }

    @Override
    public Identifier getTextureResource(ReturningKnight object) {
        String phase = object.isPhaseTwo() ? "returning_knight_phase_2_512_translucent" : "returning_knight_512";
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/returning_knight/" + phase + ".png");
    }
}