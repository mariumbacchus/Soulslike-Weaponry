package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.mobs.boss.BossEntity;
import org.joml.Vector3d;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Creates a new GeoModel with the following asset paths:
 * - Animation: assets/soulsweapons/animations/entity/subpath
 * - Model: assets/soulsweapons/geo/entity/subpath
 * - Texture: assets/soulsweapons/textures/entity/subpath
 */
public class BossGeoEntityModel<T extends BossEntity<?> & GeoEntity> extends DefaultedEntityGeoModel<T> {

    private int lastPrintedTick = -1;
    private boolean wasPrinting;
    private final List<String> keyframes = new ArrayList<>();

    public BossGeoEntityModel(Identifier assetSubpath, boolean turnHead) {
        super(assetSubpath, turnHead);
    }

    /**
     * Override to print debug lines for where the bone returned by {@link #getDebugBoneId()} is in the world
     * during the animation. Should for example return boss.isState(OBLITERATE).
     */
    public boolean isDebugAttacking(T boss) {
        return false;
    }

    /**
     * Returns the id of the bone to track, i.e. "hitboxCenter" bone in Returning Knight, which is an empty bone
     * inside Nightfall.
     */
    public String getDebugBoneId() {
        return "";
    }

    /**
     * Override to set a custom boolean for when it should not print the debug lines,
     * for example don't print when attackStatus is less than 70.
     */
    public boolean shouldNotPrint(int attackStatus) {
        return false;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (!this.isDebugAttacking(animatable)) {
            if (this.wasPrinting && !this.keyframes.isEmpty()) {
                this.printKeyframes();
                this.keyframes.clear();
                this.lastPrintedTick = -1;
            }
            this.wasPrinting = false;
            return;
        }
        this.wasPrinting = true;

        GeoBone bone = this.getAnimationProcessor().getBone(this.getDebugBoneId());
        if (bone == null) {
            return;
        }
        int attackTick = animatable.getAttackStatus();

        // Print only once per attack tick, not every render frame
        if (attackTick == this.lastPrintedTick || this.shouldNotPrint(attackTick)) {
            return;
        }
        this.lastPrintedTick = attackTick;

        Vector3d worldPos = bone.getWorldPosition();
        Vec3d localOffset = BossHitboxHelper.worldToBossLocal(
                animatable,
                new Vec3d(worldPos.x, worldPos.y, worldPos.z),
                animatable.bodyYaw
        );

        String roundedKeyframe = String.format(
                Locale.ROOT,
                "            new BossHitboxHelper.Keyframe(%d, new Vec3d(%.1f, %.1f, %.1f)),",
                attackTick,
                localOffset.x,
                localOffset.y,
                localOffset.z
        );

        this.keyframes.add(roundedKeyframe);

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
        System.out.println("  SERVER KEYFRAME = new BossHitboxHelper.Keyframe("
                + attackTick
                + ", new Vec3d("
                + localOffset.x + ", "
                + localOffset.y + ", "
                + localOffset.z
                + ")),"
        );
        System.out.println("  SERVER KEYFRAME (decimal fix) = " + roundedKeyframe.trim());
    }

    private void printKeyframes() {
        System.out.println();
        System.out.println("========== ATTACK KEYFRAMES ==========");
        System.out.println("private static final BossHitboxHelper.Keyframe[] PATH = new BossHitboxHelper.Keyframe[] {");

        for (String keyframe : this.keyframes) {
            System.out.println(keyframe);
        }

        System.out.println("};");
        System.out.println("==============================================");
        System.out.println();
    }
}
