package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.DayStalker;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class DayStalkerModel extends AnimatedGeoModel<DayStalker>{
    
    @Override
    public Identifier getModelLocation(DayStalker object)
    {
        return new Identifier(SoulsWeaponry.ModId, "geo/day_stalker.geo.json");
    }

    @Override
    public Identifier getTextureLocation(DayStalker object)
    {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/day_stalker.png");
    }

    @Override
    public Identifier getAnimationFileLocation(DayStalker object)
    {
        return new Identifier(SoulsWeaponry.ModId, "animations/day_stalker.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(DayStalker animatable, int instanceId, AnimationEvent customPredicate) {
        super.setCustomAnimations(animatable, instanceId, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
		}
    }
}
