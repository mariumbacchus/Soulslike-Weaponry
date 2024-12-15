package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.Moonknight;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class MoonknightModel extends GeoModel<Moonknight> {

    @Override
    public Identifier getAnimationResource(Moonknight animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/entity/moonknight.animation.json");
    }

    @Override
    public Identifier getModelResource(Moonknight object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/entity/moonknight.geo.json");
    }

    @Override
    public Identifier getTextureResource(Moonknight object) {
        String phase = object.isPhaseTwo() || object.initiatedPhaseTwo() ? "phase_2" : "phase_1";
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/moonknight/moonknight_" + phase + ".png");
    }

    @Override
    public void setCustomAnimations(Moonknight animatable, long instanceId, AnimationState<Moonknight> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
