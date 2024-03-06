package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.LeviathanAxeEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LeviathanAxeEntityModel extends AnimatedGeoModel<LeviathanAxeEntity>{

    @Override
    public Identifier getAnimationFileLocation(LeviathanAxeEntity animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/leviathan_axe.animation.json");
    }

    @Override
    public Identifier getModelLocation(LeviathanAxeEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/leviathan_axe.geo.json");
    }

    @Override
    public Identifier getTextureLocation(LeviathanAxeEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/item/leviathan_axe_texture.png");
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setCustomAnimations(LeviathanAxeEntity animatable, int instanceId, AnimationEvent customPredicate) {
        super.setCustomAnimations(animatable, instanceId, customPredicate);

        IBone main = this.getAnimationProcessor().getBone("main");
        float rotation = animatable.age % 360;
		if (!animatable.dealtDamage && main != null) {
			main.setRotationX(-rotation);
		}
    }
}
