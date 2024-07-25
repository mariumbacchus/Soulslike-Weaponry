package net.soulsweaponry.entity.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.NightProwler;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlackflameSnakeLogic {
    private final Vec3d start;
    private final Vec3d end;
    private final float radius;
    private final int ticksBetween;
    private final float yaw;
    private final UUID ownerUuid;
    private int age;
    private final List<List<Vec3d>> allPos = new ArrayList<>();

    public BlackflameSnakeLogic(Vec3d start, Vec3d end, float radius, int ticksBetween, float userYaw, UUID ownerUuid) {
        this.start = start;
        this.end = end;
        this.radius = radius;
        this.ticksBetween = ticksBetween;
        this.yaw = userYaw;
        this.ownerUuid = ownerUuid;
        this.allPos.add(this.getPositionsFromSide(true));
        this.allPos.add(this.getPositionsFromSide(false));
    }

    /**
     * <p>1. In the constructor, a list "allPos" is made with lists over all the different paths are made in the form
     * of lists of positions to detonate an explosion.</p>
     * <p>2. Age is constantly being updated each tick. Goes through all the lists inside the main list "allPos"
     * and checks whether the age of the class modulus ticksBetween each explosion equals 0. If this is the case,
     * move on to point 3.</p>
     * <p>3. For every position based on the first position in the list, execute the explosion logic, then
     * remove the used position, so it moves onto the next explosion the next time the if statement above passes.</p>
     */
    public void tick(World world) {
        this.age++;
        for (List<Vec3d> list : this.allPos) {
            if (this.age % this.ticksBetween == 0 && list.size() != 0) {
                this.ruptureLogic(world, list.get(0));
                list.remove(0);
            }
        }
    }

    private void ruptureLogic(World world, Vec3d target) {
        if (!world.isClient) {
            ParticleHandler.particleOutburstMap(world, 250, target.getX(), target.getY(), target.getZ(), ParticleEvents.BLACKFLAME_SNAKE_PARTICLE_MAP, 1f);
            for (Entity entity : world.getOtherEntities(this.getOwner(world), new Box(new BlockPos(target)).expand(2D))) {
                if (entity instanceof LivingEntity living && !this.isOwner(living) && !(entity instanceof NightProwler)) {
                    DamageSource src = (this.getOwner(world) != null && this.getOwner(world) instanceof LivingEntity) ?
                            DamageSource.mob((LivingEntity) this.getOwner(world)) : DamageSource.GENERIC;
                    if (living.damage(src, 35f * ConfigConstructor.night_prowler_damage_modifier)) {
                        living.addVelocity(0, 1f, 0);
                    }
                }
            }
            world.playSound(null, new BlockPos(target), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f);
        }
    }

    private List<Vec3d> calcAllPositions() {
        ArrayList<Vec3d> positions = new ArrayList<>();
        double yaw = this.yaw + 90;
        double ra = Math.toRadians(yaw);
        float r = this.radius;
        Vec3d s = this.start.add(Math.cos(ra) * r, 0, Math.sin(ra) * r);
        Vec3d endLeft = null;
        Vec3d endRight = null;
        for (int i = 90; i < 270; i++) {
            if (i % 8 == 0) {
                double rad = Math.toRadians(yaw + i);
                double x = r * Math.cos(rad);
                double z = r * Math.sin(rad);
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
            Vec3d start = endLeft;
            Vec3d between = new Vec3d(this.end.getX() - start.getX(), this.end.getY() - start.getY(), this.end.getZ() - start.getZ());
            int len = MathHelper.floor(between.length());
            for (int i = 0; i < len; i++) {
                start = start.add(between.multiply((double) 1 / len));
                positions.add(new Vec3d(start.getX(), start.getY(), start.getZ()));
            }
            Vec3d start2 = endRight;
            Vec3d between2 = new Vec3d(this.end.getX() - start2.getX(), this.end.getY() - start2.getY(), this.end.getZ() - start2.getZ());
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
    private List<Vec3d> getPositionsFromSide(boolean leftSide) {
        ArrayList<Vec3d> positions = new ArrayList<>();
        double yaw = this.yaw + 90;
        double ra = Math.toRadians(yaw);
        float r = this.radius;
        Vec3d s = this.start.add(Math.cos(ra) * r, 0, Math.sin(ra) * r);
        Vec3d endPos = null;
        int i = 180;
        while (leftSide ? i < 270 : i > 90) {
            if (i % 8 == 0) {
                double rad = Math.toRadians(yaw + i);
                double x = r * Math.cos(rad);
                double z = r * Math.sin(rad);
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
            positions.addAll(this.getPosOfLine(endPos));
        }
        return positions;
    }

    private List<Vec3d> getPosOfLine(Vec3d targetPos) {
        ArrayList<Vec3d> positions = new ArrayList<>();
        Vec3d between = new Vec3d(this.end.getX() - targetPos.getX(), this.end.getY() - targetPos.getY(), this.end.getZ() - targetPos.getZ());
        int len = MathHelper.floor(between.length());
        for (int j = 0; j < len; j++) {
            targetPos = targetPos.add(between.multiply((double) 1 / len));
            positions.add(new Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()));
        }
        return positions;
    }

    public Vec3d getStart() {
        return start;
    }

    public Vec3d getEnd() {
        return end;
    }

    public float getRadius() {
        return radius;
    }

    public float getYaw() {
        return yaw;
    }

    /**
     * Returns the ticks between each movement/explosion. When one spot explodes, the
     * object moves on to the next point to detonate.
     * @return Ticks between explosions
     */
    public int getTicksBetween() {
        return ticksBetween;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public boolean isOwner(LivingEntity target) {
        return target.getUuid().equals(this.ownerUuid);
    }

    public boolean isFinished() {
        int i = 0;
        for (List<Vec3d> list : this.allPos) {
            i += list.size();
        }
        return i == 0;
    }

    @Nullable
    public Entity getOwner(World world) {
        if (!world.isClient) {
            return ((ServerWorld)world).getEntity(this.getOwnerUuid());
        } else {
            return null;
        }
    }
}