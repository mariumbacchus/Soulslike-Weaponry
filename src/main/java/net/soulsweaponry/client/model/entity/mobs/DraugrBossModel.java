package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.DraugrBoss;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class DraugrBossModel extends AnimatedGeoModel<DraugrBoss>{

    @Override
    public Identifier getModelLocation(DraugrBoss object)
    {
        return new Identifier(SoulsWeaponry.ModId, "geo/draugr_boss.geo.json");
    }

    @Override
    public Identifier getTextureLocation(DraugrBoss object)
    {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/draugr_boss.png");
    }

    @Override
    public Identifier getAnimationFileLocation(DraugrBoss object)
    {
        return new Identifier(SoulsWeaponry.ModId, "animations/draugr_boss.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(DraugrBoss animatable, int instanceId, AnimationEvent customPredicate) {
        super.setCustomAnimations(animatable, instanceId, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
		}
    }
}
