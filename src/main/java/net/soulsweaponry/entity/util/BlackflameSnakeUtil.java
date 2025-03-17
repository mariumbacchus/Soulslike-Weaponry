package net.soulsweaponry.entity.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class BlackflameSnakeUtil {

    /**
     * Get two lists, one with the left side of the curve, the other with the right. Meant for easier spawning of entities
     * so they spawn from the start position and stop at the end, unlike {@link #getAllPositions(float, float, Vec3d, Vec3d)}
     * which returns all positions at once, making looping and spawning over them will spawn them on non-consistent positions.
     */
    public static List<List<Vec3d>> getCurvedPositions(float entityYaw, float radius, Vec3d start, Vec3d end) {
        List<List<Vec3d>> curvedPositions = new ArrayList<>();
        curvedPositions.add(getPositionsFromSide(true, entityYaw, radius, start, end));
        curvedPositions.add(getPositionsFromSide(false, entityYaw, radius, start, end));
        return curvedPositions;
    }

    public static List<Vec3d> getAllPositions(float yaw, float radius, Vec3d start, Vec3d end) {
        ArrayList<Vec3d> positions = new ArrayList<>();
        yaw += 90;
        double ra = Math.toRadians(yaw);
        Vec3d s = start.add(Math.cos(ra) * radius, 0, Math.sin(ra) * radius);
        Vec3d endLeft = null;
        Vec3d endRight = null;
        for (int i = 90; i < 270; i++) {
            if (i % 8 == 0) {
                double rad = Math.toRadians(yaw + i);
                double x = radius * Math.cos(rad);
                double z = radius * Math.sin(rad);
                Vec3d pos = new Vec3d(x, 0, z).add(s);
                if (i == 264) {
                    endLeft = pos;
                }
                if (i == 96) {
                    endRight = pos;
                }
                positions.add(pos);
            }
        }
        if (endLeft != null && endRight != null) {
            Vec3d start1 = endLeft;
            Vec3d between = new Vec3d(end.getX() - start1.getX(), end.getY() - start1.getY(), end.getZ() - start1.getZ());
            int len = MathHelper.floor(between.length());
            for (int i = 0; i < len; i++) {
                start1 = start1.add(between.multiply((double) 1 / len));
                positions.add(new Vec3d(start1.getX(), start1.getY(), start1.getZ()));
            }
            Vec3d start2 = endRight;
            Vec3d between2 = new Vec3d(end.getX() - start2.getX(), end.getY() - start2.getY(), end.getZ() - start2.getZ());
            int len2 = MathHelper.floor(between2.length());
            for (int i = 0; i < len2; i++) {
                start2 = start2.add(between2.multiply((double) 1 / len));
                positions.add(new Vec3d(start2.getX(), start2.getY(), start2.getZ()));
            }
        }
        return positions;
    }

    //int i = 180; i > 90; i-- right
    //int i = 180; i < 270; i++ left
    public static List<Vec3d> getPositionsFromSide(boolean leftSide, float yaw, float radius, Vec3d start, Vec3d end) {
        ArrayList<Vec3d> positions = new ArrayList<>();
        yaw += 90;
        double ra = Math.toRadians(yaw);
        Vec3d s = start.add(Math.cos(ra) * radius, 0, Math.sin(ra) * radius);
        Vec3d endPos = null;
        int i = 180;
        while (leftSide ? i < 270 : i > 90) {
            if (i % 8 == 0) {
                double rad = Math.toRadians(yaw + i);
                double x = radius * Math.cos(rad);
                double z = radius * Math.sin(rad);
                Vec3d pos = new Vec3d(x, 0, z).add(s);
                if (i == (leftSide ? 264 : 96)) {
                    endPos = pos;
                }
                positions.add(pos);
            }
            if (leftSide) {
                i++;
            } else {
                i--;
            }
        }
        if (endPos != null) {
            positions.addAll(getPosOfLine(endPos, end));
        }
        return positions;
    }

    public static List<Vec3d> getPosOfLine(Vec3d targetPos, Vec3d end) {
        ArrayList<Vec3d> positions = new ArrayList<>();
        Vec3d between = new Vec3d(end.getX() - targetPos.getX(), end.getY() - targetPos.getY(), end.getZ() - targetPos.getZ());
        int len = MathHelper.floor(between.length());
        for (int j = 0; j < len; j++) {
            targetPos = targetPos.add(between.multiply((double) 1 / len));
            positions.add(new Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()));
        }
        return positions;
    }
}