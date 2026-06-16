package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import org.joml.Vector3d;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReturningKnightModel extends DefaultedEntityGeoModel<ReturningKnight> {

    public ReturningKnightModel() {
        super(Identifier.of(SoulsWeaponry.ModId, "returning_knight"), true);
    }

    private int lastPrintedMaceOfSpadesTick = -1;
    private boolean wasPrintingMaceOfSpades;
    private final List<String> maceOfSpadesKeyframes = new ArrayList<>();

    @Override
    public void setCustomAnimations(ReturningKnight animatable, long instanceId, AnimationState<ReturningKnight> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        boolean isMaceOfSpades = animatable.isState(ReturningKnight.States.MACE_OF_SPADES);

        if (!isMaceOfSpades) {
            if (this.wasPrintingMaceOfSpades && !this.maceOfSpadesKeyframes.isEmpty()) {
                this.printMaceOfSpadesKeyframes();
                this.maceOfSpadesKeyframes.clear();
                this.lastPrintedMaceOfSpadesTick = -1;
            }

            this.wasPrintingMaceOfSpades = false;
            return;
        }

        this.wasPrintingMaceOfSpades = true;

        GeoBone hitboxCenter = this.getAnimationProcessor().getBone("hitboxCenter");

        if (hitboxCenter == null) {
            return;
        }

        int attackTick = animatable.getAttackStatus();

        // Print only once per attack tick, not every render frame
        if (attackTick == this.lastPrintedMaceOfSpadesTick || attackTick > 70) {
            return;
        }

        this.lastPrintedMaceOfSpadesTick = attackTick;

        Vector3d worldPos = hitboxCenter.getWorldPosition();

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

        this.maceOfSpadesKeyframes.add(roundedKeyframe);

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

    private void printMaceOfSpadesKeyframes() {
        System.out.println();
        System.out.println("========== MACE OF SPADES KEYFRAMES ==========");
        System.out.println("private static final BossHitboxHelper.Keyframe[] MACE_PATH = new BossHitboxHelper.Keyframe[] {");

        for (String keyframe : this.maceOfSpadesKeyframes) {
            System.out.println(keyframe);
        }

        System.out.println("};");
        System.out.println("==============================================");
        System.out.println();
    }
}