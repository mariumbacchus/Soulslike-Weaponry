package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MoonlightProjectileBigModel extends AnimatedGeoModel<MoonlightProjectile>{

    @Override
    public Identifier getAnimationFileLocation(MoonlightProjectile animatable) {
        return null;
    }

    @Override
    public Identifier getModelLocation(MoonlightProjectile object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/moonlight_projectile_big.geo.json");
    }

    @Override
    public Identifier getTextureLocation(MoonlightProjectile object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/moonlight_projectile_big.png");
    }

    @SuppressWarnings({"rawtypes" })
    @Override
	public void setCustomAnimations(MoonlightProjectile entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);
		IBone bone = this.getAnimationProcessor().getBone("bone");

		if (bone != null) {
            switch (entity.getRotateState()) {
                case SWIPE_FROM_LEFT -> {
                    bone.setRotationX(90 * ((float) Math.PI / 180F));
                    bone.setRotationY(-40 * ((float) Math.PI / 180F));
                    bone.setRotationZ(-90 * ((float) Math.PI / 180F));
                }
                case SWIPE_FROM_RIGHT -> {
                    bone.setRotationX(-90 * ((float) Math.PI / 180F));
                    bone.setRotationY(-30 * ((float) Math.PI / 180F));
                    bone.setRotationZ(90 * ((float) Math.PI / 180F));
                }
                default -> {
                }
            }
		}
	}
}
