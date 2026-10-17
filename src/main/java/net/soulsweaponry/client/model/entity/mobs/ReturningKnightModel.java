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
        return "hitboxCenter"; // main mace (phase 1): hitboxCenter, shadow mace (phase 2): hitboxCenter2
    }

    @Override
    public boolean isDebugAttacking(ReturningKnight boss) {
        return false;//boss.isState(ReturningKnight.States.OBLITERATE_FAKEOUT_PHASE_2);//TODO
    }

    @Override
    public Identifier getTextureResource(ReturningKnight object) {
        String phase = object.isPhaseTwo() ? "returning_knight_phase_2_512_translucent" : "returning_knight_512";
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/returning_knight/" + phase + ".png");
    }
}