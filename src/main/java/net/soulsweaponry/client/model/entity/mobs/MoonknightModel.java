package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.Moonknight;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class MoonknightModel extends GeoModel<Moonknight> {

    @Override
    public Identifier getModelResource(Moonknight moonknight, @Nullable GeoRenderer<Moonknight> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/entity/moonknight.geo.json");
    }

    @Override
    public Identifier getTextureResource(Moonknight moonknight, @Nullable GeoRenderer<Moonknight> geoRenderer) {
        String phase = moonknight.isPhaseTwo() || moonknight.initiatedPhaseTwo() ? "phase_2" : "phase_1";
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/moonknight/moonknight_" + phase + ".png");
    }

    @Override
    public Identifier getAnimationResource(Moonknight animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/entity/moonknight.animation.json");
    }

    @Override
    public void setCustomAnimations(Moonknight animatable, long instanceId, AnimationState<Moonknight> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
