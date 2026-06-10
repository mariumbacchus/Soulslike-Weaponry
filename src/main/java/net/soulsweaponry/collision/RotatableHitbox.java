package net.soulsweaponry.collision;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/*
 * Reusable oriented bounding box / rotatable hitbox implementation.
 *
 * Uses Separating Axis Theorem for OBB collision checks.
 * Inspired by Better Combat's oriented melee hitbox implementation, but generalized for bosses and item abilities.
 *
 * Better Combat: https://github.com/ZsoltMolnarrr/BetterCombat/blob/1.21.1/common/src/main/java/net/bettercombat/client/collision/OrientedBoundingBox.java
 *
 * Other sources:
 * The Math Behind Bounding Box Collision Detection - AABB vs OBB(Separate Axis Theorem), at https://dev.to/pratyush_mohanty_6b8f2749/the-math-behind-bounding-box-collision-detection-aabb-vs-obbseparate-axis-theorem-1gdn
 * Real-Time Collision Detection by Christer Ericson
 */
public class RotatableHitbox {

    private static final double EPSILON = 1.0E-8;

    /*
     * Local OBB axes.
     *
     * axisX = local right
     * axisY = local up
     * axisZ = local forward
     *
     * The box extends by halfExtents.x along axisX,
     * halfExtents.y along axisY,
     * halfExtents.z along axisZ.
     */
    private Vec3d center;
    private Vec3d halfExtents;

    private Vec3d axisX;
    private Vec3d axisY;
    private Vec3d axisZ;

    private final Vec3d[] vertices = new Vec3d[8];

    private boolean dirty = true;

    public static final int[][] EDGES = new int[][] {
            {0, 1}, {1, 2}, {2, 3}, {3, 0},
            {4, 5}, {5, 6}, {6, 7}, {7, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };

    public RotatableHitbox(Vec3d center, Vec3d size, Vec3d axisX, Vec3d axisY, Vec3d axisZ) {
        this.center = center;
        this.halfExtents = size.multiply(0.5);
        setAxes(axisX, axisY, axisZ);
    }

    public static RotatableHitbox fromAabb(Box box) {
        Vec3d center = new Vec3d(
                (box.minX + box.maxX) * 0.5,
                (box.minY + box.maxY) * 0.5,
                (box.minZ + box.maxZ) * 0.5
        );

        Vec3d size = new Vec3d(
                box.maxX - box.minX,
                box.maxY - box.minY,
                box.maxZ - box.minZ
        );

        return new RotatableHitbox(
                center,
                size,
                new Vec3d(1, 0, 0),
                new Vec3d(0, 1, 0),
                new Vec3d(0, 0, 1)
        );
    }

    public static RotatableHitbox fromLook(Vec3d center, Vec3d size, Vec3d lookDirection) {
        return fromLook(center, size, lookDirection, new Vec3d(0, 1, 0), 0.0);
    }

    public static RotatableHitbox fromLook(Vec3d center, Vec3d size, Vec3d lookDirection, Vec3d upHint, double rollRadians) {
        Vec3d forward = safeNormalize(lookDirection, new Vec3d(0, 0, 1));
        Vec3d up = safeNormalize(upHint, new Vec3d(0, 1, 0));

        if (Math.abs(forward.dotProduct(up)) > 0.98) {
            up = new Vec3d(0, 0, 1);
        }

        Vec3d right = safeNormalize(up.crossProduct(forward), new Vec3d(1, 0, 0));
        Vec3d realUp = safeNormalize(forward.crossProduct(right), new Vec3d(0, 1, 0));

        if (rollRadians != 0.0) {
            right = rotateAroundAxis(right, forward, rollRadians);
            realUp = rotateAroundAxis(realUp, forward, rollRadians);
        }

        return new RotatableHitbox(center, size, right, realUp, forward);
    }

    public static RotatableHitbox fromYawPitch(Vec3d center, Vec3d size, float pitch, float yaw) {
        Vec3d look = Vec3d.fromPolar(pitch, yaw);
        return fromLook(center, size, look);
    }

    public RotatableHitbox copy() {
        return new RotatableHitbox(center, halfExtents.multiply(2.0), axisX, axisY, axisZ);
    }

    public RotatableHitbox setCenter(Vec3d center) {
        this.center = center;
        this.dirty = true;
        return this;
    }

    public RotatableHitbox setSize(Vec3d size) {
        this.halfExtents = size.multiply(0.5);
        this.dirty = true;
        return this;
    }

    public RotatableHitbox setHalfExtents(Vec3d halfExtents) {
        this.halfExtents = halfExtents;
        this.dirty = true;
        return this;
    }

    public RotatableHitbox setAxes(Vec3d axisX, Vec3d axisY, Vec3d axisZ) {
        this.axisX = safeNormalize(axisX, new Vec3d(1, 0, 0));
        this.axisY = safeNormalize(axisY, new Vec3d(0, 1, 0));
        this.axisZ = safeNormalize(axisZ, new Vec3d(0, 0, 1));
        this.dirty = true;
        return this;
    }

    public RotatableHitbox setTransform(Vec3d center, Vec3d size, Vec3d axisX, Vec3d axisY, Vec3d axisZ) {
        this.center = center;
        this.halfExtents = size.multiply(0.5);
        setAxes(axisX, axisY, axisZ);
        return this;
    }

    public RotatableHitbox setFromLook(Vec3d center, Vec3d size, Vec3d lookDirection) {
        RotatableHitbox temp = fromLook(center, size, lookDirection);
        this.center = temp.center;
        this.halfExtents = temp.halfExtents;
        this.axisX = temp.axisX;
        this.axisY = temp.axisY;
        this.axisZ = temp.axisZ;
        this.dirty = true;
        return this;
    }

    public RotatableHitbox offsetWorld(double x, double y, double z) {
        return offsetWorld(new Vec3d(x, y, z));
    }

    public RotatableHitbox offsetWorld(Vec3d offset) {
        this.center = this.center.add(offset);
        this.dirty = true;
        return this;
    }

    public RotatableHitbox offsetLocal(double x, double y, double z) {
        this.center = this.center
                .add(axisX.multiply(x))
                .add(axisY.multiply(y))
                .add(axisZ.multiply(z));

        this.dirty = true;
        return this;
    }

    public RotatableHitbox scale(double scale) {
        this.halfExtents = this.halfExtents.multiply(scale);
        this.dirty = true;
        return this;
    }

    public RotatableHitbox scale(double x, double y, double z) {
        return scale(new Vec3d(x, y, z));
    }

    public RotatableHitbox scale(Vec3d scale) {
        this.halfExtents = new Vec3d(
                this.halfExtents.x * scale.x,
                this.halfExtents.y * scale.y,
                this.halfExtents.z * scale.z
        );

        this.dirty = true;
        return this;
    }

    public void update() {
        if (!dirty) {
            return;
        }

        Vec3d x = axisX.multiply(halfExtents.x);
        Vec3d y = axisY.multiply(halfExtents.y);
        Vec3d z = axisZ.multiply(halfExtents.z);

        vertices[0] = center.subtract(x).subtract(y).subtract(z);
        vertices[1] = center.add(x).subtract(y).subtract(z);
        vertices[2] = center.add(x).add(y).subtract(z);
        vertices[3] = center.subtract(x).add(y).subtract(z);

        vertices[4] = center.subtract(x).subtract(y).add(z);
        vertices[5] = center.add(x).subtract(y).add(z);
        vertices[6] = center.add(x).add(y).add(z);
        vertices[7] = center.subtract(x).add(y).add(z);

        dirty = false;
    }

    public boolean contains(Vec3d point) {
        Vec3d local = point.subtract(center);

        double x = local.dotProduct(axisX);
        double y = local.dotProduct(axisY);
        double z = local.dotProduct(axisZ);

        return Math.abs(x) <= halfExtents.x
                && Math.abs(y) <= halfExtents.y
                && Math.abs(z) <= halfExtents.z;
    }

    public boolean intersects(Box box) {
        return intersects(RotatableHitbox.fromAabb(box));
    }

    public boolean intersects(RotatableHitbox other) {
        this.update();
        other.update();

        Vec3d[] axes = new Vec3d[] {
                this.axisX,
                this.axisY,
                this.axisZ,

                other.axisX,
                other.axisY,
                other.axisZ,

                this.axisX.crossProduct(other.axisX),
                this.axisX.crossProduct(other.axisY),
                this.axisX.crossProduct(other.axisZ),

                this.axisY.crossProduct(other.axisX),
                this.axisY.crossProduct(other.axisY),
                this.axisY.crossProduct(other.axisZ),

                this.axisZ.crossProduct(other.axisX),
                this.axisZ.crossProduct(other.axisY),
                this.axisZ.crossProduct(other.axisZ)
        };

        for (Vec3d axis : axes) {
            if (isSeparatedOnAxis(this.vertices, other.vertices, axis)) {
                return false;
            }
        }

        return true;
    }

    public List<LivingEntity> getIntersectingTargets(World world, Entity except) {
        return getIntersectingEntities(world, except, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isAttackable()).stream().map(e -> (LivingEntity) e).toList();
    }

    public List<Entity> getIntersectingEntities(
            World world,
            Entity except,
            Predicate<Entity> predicate
    ) {
        update();

        Box searchBox = toAabb();

        List<Entity> nearby = world.getOtherEntities(
                except,
                searchBox,
                entity -> predicate == null || predicate.test(entity)
        );

        List<Entity> result = new ArrayList<>();

        for (Entity entity : nearby) {
            Box entityBox = entity.getBoundingBox().expand(entity.getTargetingMargin());

            if (this.intersects(entityBox) || this.contains(entity.getPos().add(0, entity.getHeight() * 0.5, 0))) {
                result.add(entity);
            }
        }

        return result;
    }

    public Box toAabb() {
        update();

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;

        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (Vec3d vertex : vertices) {
            minX = Math.min(minX, vertex.x);
            minY = Math.min(minY, vertex.y);
            minZ = Math.min(minZ, vertex.z);

            maxX = Math.max(maxX, vertex.x);
            maxY = Math.max(maxY, vertex.y);
            maxZ = Math.max(maxZ, vertex.z);
        }

        return new Box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private static boolean isSeparatedOnAxis(Vec3d[] aVertices, Vec3d[] bVertices, Vec3d axis) {
        if (axis.lengthSquared() < EPSILON) {
            return false;
        }

        double aMin = Double.POSITIVE_INFINITY;
        double aMax = Double.NEGATIVE_INFINITY;

        double bMin = Double.POSITIVE_INFINITY;
        double bMax = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < 8; i++) {
            double aProjection = aVertices[i].dotProduct(axis);
            double bProjection = bVertices[i].dotProduct(axis);

            aMin = Math.min(aMin, aProjection);
            aMax = Math.max(aMax, aProjection);

            bMin = Math.min(bMin, bProjection);
            bMax = Math.max(bMax, bProjection);
        }

        return aMax < bMin || bMax < aMin;
    }

    private static Vec3d safeNormalize(Vec3d vector, Vec3d fallback) {
        if (vector == null || vector.lengthSquared() < EPSILON) {
            return fallback;
        }

        return vector.normalize();
    }

    private static Vec3d rotateAroundAxis(Vec3d vector, Vec3d axis, double radians) {
        Vec3d n = safeNormalize(axis, new Vec3d(0, 1, 0));

        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        return vector.multiply(cos)
                .add(n.crossProduct(vector).multiply(sin))
                .add(n.multiply(n.dotProduct(vector) * (1.0 - cos)));
    }

    public Vec3d getCenter() {
        return center;
    }

    public Vec3d getSize() {
        return halfExtents.multiply(2.0);
    }

    public Vec3d getHalfExtents() {
        return halfExtents;
    }

    public Vec3d getAxisX() {
        return axisX;
    }

    public Vec3d getAxisY() {
        return axisY;
    }

    public Vec3d getAxisZ() {
        return axisZ;
    }

    public Vec3d[] getVertices() {
        update();
        return vertices.clone();
    }
}
