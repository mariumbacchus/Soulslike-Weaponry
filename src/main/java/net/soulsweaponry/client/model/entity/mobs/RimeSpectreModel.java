package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.RimeSpectre;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class RimeSpectreModel extends AnimatedGeoModel<RimeSpectre>{

    @Override
    public Identifier getModelLocation(RimeSpectre object)
    {
        return new Identifier(SoulsWeaponry.ModId, "geo/rime_spectre.geo.json");
    }

    @Override
    public Identifier getTextureLocation(RimeSpectre object)
    {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/rime_spectre.png");
    }

    @Override
    public Identifier getAnimationFileLocation(RimeSpectre object)
    {
        return new Identifier(SoulsWeaponry.ModId, "animations/rime_spectre.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(RimeSpectre entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}