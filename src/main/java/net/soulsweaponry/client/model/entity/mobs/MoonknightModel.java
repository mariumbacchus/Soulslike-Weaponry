package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.Moonknight;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class MoonknightModel extends AnimatedGeoModel<Moonknight> {

    @Override
    public Identifier getAnimationResource(Moonknight animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/moonknight.animation.json");
    }

    @Override
    public Identifier getModelResource(Moonknight object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/moonknight.geo.json");
    }

    @Override
    public Identifier getTextureResource(Moonknight object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/moonknight.png");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public void setCustomAnimations(Moonknight entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("head");
        
		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null && !entity.isDead()) {
			head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
		}
	}
    
}
