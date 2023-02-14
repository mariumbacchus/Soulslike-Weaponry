package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.NightShade;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class NightShadeModel extends AnimatedGeoModel<NightShade>{

    @Override
    public Identifier getAnimationFileLocation(NightShade animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/night_shade.animation.json");
    }

    @Override
    public Identifier getModelLocation(NightShade object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/night_shade.geo.json");
    }

    @Override
    public Identifier getTextureLocation(NightShade object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/night_shade.png");
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(NightShade animatable, int instanceId, AnimationEvent customPredicate) {
        super.setCustomAnimations(animatable, instanceId, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
		}
    }
}
