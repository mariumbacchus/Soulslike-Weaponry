package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.NightProwler;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class NightProwlerModel extends AnimatedGeoModel<NightProwler>{
    
    @Override
    public Identifier getModelResource(NightProwler object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/night_prowler.geo.json");
    }

    @Override
    public Identifier getTextureResource(NightProwler object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/night_prowler.png");
    }

    @Override
    public Identifier getAnimationResource(NightProwler object) {
        return new Identifier(SoulsWeaponry.ModId, "animations/night_prowler.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(NightProwler entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null && !entity.isDead()) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
