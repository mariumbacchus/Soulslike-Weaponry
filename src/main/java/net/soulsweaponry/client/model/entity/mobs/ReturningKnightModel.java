package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;

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

    @Override
    public void setCustomAnimations(ReturningKnight animatable, long instanceId, AnimationState<ReturningKnight> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        // Shadow can also turn head
        GeoBone head = getAnimationProcessor().getBone("head2");
        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}