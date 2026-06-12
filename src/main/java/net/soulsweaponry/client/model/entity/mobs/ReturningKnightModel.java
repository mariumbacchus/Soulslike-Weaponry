package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ReturningKnightModel extends DefaultedEntityGeoModel<ReturningKnight> {

    public ReturningKnightModel() {
        super(Identifier.of(SoulsWeaponry.ModId, "returning_knight"), true);
    }

    /*
    Debugging of the obliterate mace hitbox
    
    private int lastPrintedObliterateTick = -1;

    @Override
    public void setCustomAnimations(ReturningKnight animatable, long instanceId, AnimationState<ReturningKnight> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone hitboxCenter = this.getAnimationProcessor().getBone("hitboxCenter");

        if (hitboxCenter == null || !animatable.getObliterate()) {
            return;
        }

        int attackTick = animatable.getObliterateTick();

        // Print only once per attack tick, not every render frame
        if (attackTick == this.lastPrintedObliterateTick) {
            return;
        }

        this.lastPrintedObliterateTick = attackTick;

        org.joml.Vector3d worldPos = hitboxCenter.getWorldPosition();
        Vec3d localOffset = worldToBossLocal(animatable, worldPos);

        System.out.println("Returning Knight hitboxCenter:");
        System.out.println("  attackTick = " + attackTick);
        System.out.println("  bone world = "
                + worldPos.x + ", "
                + worldPos.y + ", "
                + worldPos.z
        );
        System.out.println("  entity pos = "
                + animatable.getX() + ", "
                + animatable.getY() + ", "
                + animatable.getZ()
        );
        System.out.println("  SERVER KEYFRAME = new Keyframe("
                + attackTick
                + ", new Vec3d("
                + localOffset.x + ", "
                + localOffset.y + ", "
                + localOffset.z
                + ")),"
        );
    }

    private static Vec3d worldToBossLocal(ReturningKnight entity, org.joml.Vector3d worldPos) {
        Vec3d delta = new Vec3d(
                worldPos.x - entity.getX(),
                worldPos.y - entity.getY(),
                worldPos.z - entity.getZ()
        );

        Vec3d forward = getForwardFromYaw(entity.bodyYaw);
        Vec3d up = new Vec3d(0.0, 1.0, 0.0);
        Vec3d right = up.crossProduct(forward).normalize();

        return new Vec3d(
                delta.dotProduct(right),
                delta.y,
                delta.dotProduct(forward)
        );
    }

    private static Vec3d getForwardFromYaw(float yaw) {
        double radians = Math.toRadians(yaw);

        return new Vec3d(
                -Math.sin(radians),
                0.0,
                Math.cos(radians)
        ).normalize();
    }*/
}
