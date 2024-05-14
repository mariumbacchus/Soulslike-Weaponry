package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.ReturningKnight;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ReturningKnightModel extends AnimatedGeoModel<ReturningKnight> {

    @Override
    public Identifier getAnimationFileLocation(ReturningKnight animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/returning_knight.animation.json");
    }

    @Override
    public Identifier getModelLocation(ReturningKnight object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/returning_knight.geo.json");
    }

    @Override
    public Identifier getTextureLocation(ReturningKnight object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/returning_knight_texture.png");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(ReturningKnight animatable, int instanceId, AnimationEvent customPredicate) {
        super.setCustomAnimations(animatable, instanceId, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
		}
    }
    
}
