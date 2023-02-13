package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.AccursedLordBoss;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class AccursedLordBossModel extends AnimatedGeoModel<AccursedLordBoss>{
    
    @Override
    public Identifier getModelResource(AccursedLordBoss object)
    {
        return new Identifier(SoulsWeaponry.ModId, "geo/accursed_lord.geo.json");
    }

    @Override
    public Identifier getTextureResource(AccursedLordBoss object)
    {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/accursed_lord_texture.png");
    }

    @Override
    public Identifier getAnimationResource(AccursedLordBoss object)
    {
        return new Identifier(SoulsWeaponry.ModId, "animations/accursed_lord.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public void setCustomAnimations(AccursedLordBoss entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("h_head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
		}
	}
}
