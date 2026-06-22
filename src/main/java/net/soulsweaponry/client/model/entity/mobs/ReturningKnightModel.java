package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
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
}