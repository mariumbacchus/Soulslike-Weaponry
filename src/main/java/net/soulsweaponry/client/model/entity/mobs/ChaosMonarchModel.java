package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.ChaosMonarch;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ChaosMonarchModel extends AnimatedGeoModel<ChaosMonarch> {

    @Override
    public Identifier getAnimationFileLocation(ChaosMonarch animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/chaos_monarch.animation.json");
    }

    @Override
    public Identifier getModelLocation(ChaosMonarch object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/chaos_monarch.geo.json");
    }

    @Override
    public Identifier getTextureLocation(ChaosMonarch object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/chaos_monarch.png");
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(ChaosMonarch animatable, int instanceId, AnimationEvent customPredicate) {
        super.setCustomAnimations(animatable, instanceId, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
		}
    }
}
