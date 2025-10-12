package net.soulsweaponry.items.misc;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.soulsweaponry.api.entitystats.EntityStatsUtil;
import net.soulsweaponry.api.entitystats.EntityPosture;
import net.soulsweaponry.api.entitystats.EntityStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestItem extends Item {

    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 99999999;
    }

    private void conjureFangs(World world, LivingEntity user, double x, double z, double maxY, double y, float yaw, int warmup) {
        BlockPos blockPos = BlockPos.ofFloored(x, y, z);
        boolean bl = false;
        double d = 0.0;

        do {
            BlockPos blockPos2 = blockPos.down();
            BlockState blockState = world.getBlockState(blockPos2);
            if (blockState.isSideSolidFullSquare(world, blockPos2, Direction.UP)) {
                if (!world.isAir(blockPos)) {
                    BlockState blockState2 = world.getBlockState(blockPos);
                    VoxelShape voxelShape = blockState2.getCollisionShape(world, blockPos);
                    if (!voxelShape.isEmpty()) {
                        d = voxelShape.getMax(Direction.Axis.Y);
                    }
                }

                bl = true;
                break;
            }

            blockPos = blockPos.down();
        } while (blockPos.getY() >= MathHelper.floor(maxY) - 1);

        if (bl) {
            world.spawnEntity(new EvokerFangsEntity(world, x, (double)blockPos.getY() + d, z, yaw, warmup, user));
        }
    }

    private static final float ARC_LENGTH = 1.0f;
    private static final float ARC_VARIATION = 1.5f;
    private static final float INACCURACY = 0.75f;
    private static final float MIN_SEGMENT_DIST = 3.0f;
    private static final float BRANCH_CHANCE = 2f;
    private static final int DENSITY = 8; // particles per block

    private void spawnChainLightning(ServerWorld world, Vec3d start, Vec3d end) {
        Random rand = world.random;
        List<Vec3d> points = new ArrayList<>();
        points.add(start);

        // build the jagged polyline
        Vec3d last = start;
        while (last.squaredDistanceTo(end) > MIN_SEGMENT_DIST * MIN_SEGMENT_DIST) {
            Vec3d dir = end.subtract(last).normalize();
            dir = randomize(dir, INACCURACY, rand);

            float len = MathHelper.nextFloat(rand, ARC_LENGTH * ARC_VARIATION, ARC_LENGTH);
            Vec3d next = last.add(dir.multiply(len));

            points.add(next);
            last = next;
        }
        points.add(end);

        // spawn along each segment ... plus little branches
        for (int i = 0; i < points.size() - 1; i++) {
            Vec3d p1 = points.get(i);
            Vec3d p2 = points.get(i + 1);

            // main segment
            spawnParticlesOnLine(world, p1, p2);

            // random side-branch?
            if (rand.nextFloat() < BRANCH_CHANCE) {
                Vec3d branchDir   = randomize(p2.subtract(p1).normalize(), INACCURACY * 2, rand);
                float branchLen   = MathHelper.nextFloat(rand,
                        ARC_LENGTH * ARC_VARIATION * 0.5f,
                        ARC_LENGTH * 0.5f);
                Vec3d branchEnd   = p1.add(branchDir.multiply(branchLen));
                spawnParticlesOnLine(world, p1, branchEnd);
            }
        }
    }

    private Vec3d randomize(Vec3d vec, float deviation, Random rand) {
        // add a small random vector then renormalize
        Vec3d rnd = new Vec3d(
                rand.nextDouble() * 2 - 1,
                rand.nextDouble() * 2 - 1,
                rand.nextDouble() * 2 - 1
        ).multiply(deviation);
        return vec.add(rnd).normalize();
    }

    private void spawnParticlesOnLine(ServerWorld world, Vec3d a, Vec3d b) {
        double dist = a.distanceTo(b);
        int steps = MathHelper.ceil(dist * DENSITY);

        for (int i = 0; i <= steps; i++) {
            double t = (double)i / (double)steps;
            double x = MathHelper.lerp(t, a.x, b.x);
            double y = MathHelper.lerp(t, a.y, b.y);
            double z = MathHelper.lerp(t, a.z, b.z);
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 1, 0, 0, 0, 0);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        EntityPosture.getMaxPostureLoss(user);
        Optional<EntityStats> maybeStats = EntityStatsUtil.getStats(user);
        if (maybeStats.isPresent()) {
            EntityStats stats = maybeStats.get();
            float maxPosture = stats.max_posture_loss;
            float maxBleed = stats.max_bleed;
            float base = stats.base_posture_unit;
            float bleedRes = stats.bleed_buildup_resistance;
            float bleedDamageRes = stats.bleed_damage_resistance;
            float postRes = stats.posture_loss_buildup_resistance;
            System.out.println("maxPosture " + maxPosture);
            System.out.println("maxBleed " + maxBleed);
            System.out.println("base posture unit " + base);
            System.out.println("bleed buildup Res" + bleedRes);
            System.out.println("posture buildup res " + postRes);
            System.out.println("bleedDamageRes " + bleedDamageRes);
        }
        // only run on the server
        /*if (!world.isClient() && world instanceof ServerWorld serverWorld) {
            LivingEntity primary = user.getAttacking();
            if (primary != null) {
                // 1) main bolt: player eyes → primary target eyes
                Vec3d fromPlayer = new Vec3d(user.getX(), user.getEyeY(), user.getZ());
                Vec3d toPrimary  = new Vec3d(primary.getX(), primary.getEyeY(), primary.getZ());
                ParticleHandler.chainLightning(serverWorld, fromPlayer, toPrimary);

                // 2) damage & chain to each secondary living entity
                for (Entity e : world.getOtherEntities(primary,
                        primary.getBoundingBox().expand(5.0),
                        ent -> ent instanceof LivingEntity && !ent.equals(user))) {
                    LivingEntity secondary = (LivingEntity)e;
                    secondary.damage(world.getDamageSources().lightningBolt(), 1.0f);

                    Vec3d toSecondary = new Vec3d(
                            secondary.getX(),
                            secondary.getEyeY(),
                            secondary.getZ()
                    );
                    ParticleHandler.chainLightning(serverWorld, toPrimary, toSecondary);
                }
            }
            return TypedActionResult.success(stack);
        }*/

        /*double d = user.getY() - 5;
        double e = user.getY() + 5;
        float f = (float) Math.toRadians(user.getYaw() + 90);
        for (int i = 0; i < 16; i++) {
            double h = 1.25 * (double)(i + 1);
            this.conjureFangs(world, user, user.getX() + (double)MathHelper.cos(f) * h, user.getZ() + (double)MathHelper.sin(f) * h, d, e, f, i);
        }*/
        if (world.isClient) {

            // This will make an X shape of two half circles where the user is facing (only rotates Y axis)
            /*
            for (int t = -90; t < 90; t++) {
                double rad = Math.toRadians(t);
                float yaw = (float) Math.toRadians(user.getYaw() + 90);
                double x1 = 3 * Math.cos(rad);
                double y1 = 3 * Math.sin(rad);
                double z1 = 3 * Math.sin(rad);
                Vec3d vec1 = new Vec3d(x1, y1, z1).rotateY(-yaw).add(user.getEyePos()); //.add(user.getCameraPosVec(0))

                double x2 = 3 * Math.cos(rad);
                double y2 = 3 * Math.sin(rad);
                double z2 = -3 * Math.sin(rad);
                Vec3d vec2 = new Vec3d(x2, y2, z2).rotateY(-yaw).add(user.getEyePos());

                world.addParticle(ParticleTypes.FLAME, vec1.x, vec1.y, vec1.z, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, vec2.x, vec2.y, vec2.z, 0, 0, 0);
            }
            */

            // This will grant coordinates in a half circle with two quarter circles to the left and right no matter where
            // the user is facing.
            /*
            float r = 10;
            double yaw = user.getYaw() + 90;
            double ra = Math.toRadians(yaw);
            Vec3d s = user.getPos().add(Math.cos(ra) * r, 0, Math.sin(ra) * r);
            Vec3d endLeft = null;
            Vec3d endRight = null;
            for (int i = 90; i < 270; i++) {
                if (i % 8 == 0) {
                    double rad = Math.toRadians(yaw + i);
                    double x = r * Math.cos(rad);
                    double z = r * Math.sin(rad);
                    Vec3d pos = new Vec3d(x, 0, z).add(s);
                    world.addParticle(ParticleTypes.FLAME, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
                    if (i == 264) {
                        endLeft = pos;
                    }
                    if (i == 96) {
                        endRight = pos;
                    }
                }
            }

            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, endLeft.getX(), endLeft.getY(), endLeft.getZ(), 0, 0, 0);
            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, endRight.getX(), endRight.getY(), endRight.getZ(), 0, 0, 0);

            //This will make a line from one point to the other
            Vec3d start = endLeft;
            Vec3d slutt = user.getPos().add(30, 0, 0);
            Vec3d mellom = new Vec3d(slutt.getX() - start.getX(), slutt.getY() - start.getY(), slutt.getZ() - start.getZ());
            int len = MathHelper.floor(mellom.length());
            for (int i = 0; i < len; i++) {
                start = start.add(mellom.multiply((double) 1 / len));
                world.addParticle(ParticleRegistry.PURPLE_FLAME, start.getX(), start.getY(), start.getZ(), 0, 0, 0);
            }
            Vec3d start2 = endRight;
            Vec3d mellom2 = new Vec3d(slutt.getX() - start2.getX(), slutt.getY() - start2.getY(), slutt.getZ() - start2.getZ());
            int len2 = MathHelper.floor(mellom2.length());
            for (int i = 0; i < len2; i++) {
                start2 = start2.add(mellom2.multiply((double) 1 / len2));
                world.addParticle(ParticleRegistry.PURPLE_FLAME, start2.getX(), start2.getY(), start2.getZ(), 0, 0, 0);
            }
            */

            // Creates spirals upwards, used when something dies within Night Prowlers Eclipse attack, and therefore heals it.
            /*
            float r = 1f;
            for (int theta = 0; theta < 360; theta++) {
                if (theta % 2 == 0) {
                    double x0 = user.getX();
                    double y0 = user.getY() + 3;
                    double z0 = user.getZ();
                    double x = x0 + r * Math.cos(theta * Math.PI / 180);
                    double z = z0 + r * Math.sin(theta * Math.PI / 180);
                    if (user.isSneaking()) {
                        world.addParticle(ParticleRegistry.DAZZLING_PARTICLE, x, user.getY() + theta * Math.PI/180, z,
                                user.getRandom().nextGaussian()/100f, user.getRandom().nextGaussian()/100f, user.getRandom().nextGaussian()/100f);
                        world.addParticle(ParticleRegistry.DARK_STAR, x, y0 + r * Math.tan(theta * Math.PI / 180), z,
                                user.getRandom().nextGaussian()/100f, user.getRandom().nextGaussian()/100f, user.getRandom().nextGaussian()/100f);
                    } else {
                        world.addParticle(ParticleRegistry.DAZZLING_PARTICLE, x, user.getY() + theta * Math.PI/180, z,
                                0, 0, 0);
                        world.addParticle(ParticleRegistry.DARK_STAR, x, y0 + r * Math.tan(theta * Math.PI / 180), z,
                                0, 0, 0);
                    }
                }
            }
             */

//            yaw = user.getYaw() + 90;
//            for (int i = 180; i < 270; i++) {
//                double rad = Math.toRadians(yaw + i);
//                double x = r * Math.cos(rad);
//                double z = r * Math.sin(rad);
//                Vec3d pos = new Vec3d(x, 0, z).add(s);
//                world.addParticle(ParticleTypes.FLAME, pos.getX(), user.getY(), pos.getZ(), 0, 0, 0);
//            }

            // Second one that actually makes an arc
            /*Vec3d a = user.getPos();
            Vec3d b = user.getEyePos().add(-2, 0, -4);
            Vec3d c = user.getRotationVector().multiply(5).add(user.getPos());
            Vec3d o = a.add(c).multiply(0.5D);
            double r = a.distanceTo(o);
            double vax = a.getX() - o.getX();
            double vaz = a.getZ() - o.getZ();
            double vbx = b.getX() - o.getX();
            double vbz = b.getZ() - o.getZ();
            double vcx = c.getX() - o.getX();
            double vcz = c.getZ() - o.getZ();

            double tb = orientedAngle(vax, vaz, vbx, vbz);
            double tc = orientedAngle(vax, vaz, vcx, vcz);
            if (tc < tb) {
                tc = tc - 2 * Math.PI;
            }

            double segLen = 0.1D;
            double arcLen = Math.abs(tc) * r;
            double segNum = Math.ceil(arcLen / segLen);
            double segAngle = tc / segNum;
            double t = Math.atan2(vaz, vax);
            for (int i = 0; i < segNum; i++) {
                double x = o.getX() + r * Math.cos(t);
                double y = o.getY() + r * Math.atan(t);
                double z = o.getZ() + r * Math.sin(t);
                t = t + segAngle;
                world.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
            }*/

            // First one that somewhat worked, but at the same time not at all
            /*Vec3d start = user.getEyePos();
            Vec3d end = start.add(user.getRotationVector().multiply(5));
            Vec3d center = start.add(end).multiply(0.5D);
            double r = start.distanceTo(center);

            Vec3d n = start.crossProduct(end);
            Vec3d X = start.multiply((1D / start.length()));
            double yNorm = n.crossProduct(start).length();
            Vec3d Y = n.crossProduct(start).multiply(1D / yNorm);
            for (int t = 0; t < 180; t++) {
                Vec3d vec1 = X.multiply(r * Math.cos(t * Math.PI/180));
                Vec3d vec2 = Y.multiply(r * Math.sin(t * Math.PI/180));
                Vec3d vec = start.add(vec1.add(vec2));
                world.addParticle(ParticleTypes.FLAME, vec.getX(), vec.getY(), vec.getZ(), 0, 0, 0);
            }*/

            // I call this; The Orange
//            Vec3d[] rotations = {
//                    new Vec3d(-0.07747747749090195, 0.01303846761584282, -0.9969075918197632),
//                    new Vec3d(0.5694341063499451, -0.005177162121981382, -0.8220207095146179),
//                    new Vec3d(0.9171263575553894, -0.010450053960084915, -0.39845961332321167),
//                    new Vec3d(0.9772142171859741, -0.01562679372727871, 0.21167917549610138),
//                    new Vec3d(0.7729447484016418, -0.01303846761584282, 0.6343393325805664),
//                    new Vec3d(0.3349834084510803, -0.00776569964364171, 0.942192018032074),
//                    new Vec3d(-0.059023935347795486, -0.0, 0.9982565641403198),
//                    new Vec3d(-0.43421533703804016, 0.0025885896757245064, 0.9008051156997681),
//            };
//            for (Vec3d rotation : rotations) {
//                Vec3d start = user.getEyePos();
//                Vec3d end = start.add(rotation.multiply(5));
//                Vec3d center = start.add(end).multiply(0.5D);
//                double r = start.distanceTo(center);
//                Vec3d n = start.crossProduct(end);
//                Vec3d X = start.multiply((1D / start.length()));
//                double yNorm = n.crossProduct(start).length();
//                Vec3d Y = n.crossProduct(start).multiply(1D / yNorm);
//                for (int t = 0; t < 360; t++) {
//                    Vec3d vec1 = X.multiply(r * Math.cos(t * Math.PI/180));
//                    Vec3d vec2 = Y.multiply(r * Math.sin(t * Math.PI/180));
//                    Vec3d vec = start.add(vec1.add(vec2));
//                    world.addParticle(ParticleTypes.FLAME, vec.getX(), vec.getY(), vec.getZ(), 0, 0, 0);
//                }
//            }
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.fail(stack);
    }

    /*private double orientedAngle(double x1, double y1, double x2, double y2) {
        double t = Math.atan2(x1*y2 - y1*x2, x1*x2 + y1*y2);
        if (t < 0) {
            t = t + 2 * Math.PI;
        }
        return t;
    }*/
}
