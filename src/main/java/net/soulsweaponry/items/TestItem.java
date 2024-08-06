package net.soulsweaponry.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.util.NbtHelper;

import java.util.Arrays;
import java.util.UUID;

public class TestItem extends SwordItem {

    public TestItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 99999999;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) {
            UUID uuid = UUID.randomUUID();
            NbtCompound compound = stack.getNbt();
            NbtHelper.addUUIDToArr(compound, uuid, "test");
            System.out.println(Arrays.toString(NbtHelper.getUUIDArr(compound, "test")));
            if (NbtHelper.getUUIDArr(compound, "test").length > 3) {
                System.out.println("Removing: " + NbtHelper.getUUIDArr(compound, "test")[0]);
                NbtHelper.removeUUIDFromArr(compound, NbtHelper.getUUIDArr(compound, "test")[0], "test");
                System.out.println("After removal: " + Arrays.toString(NbtHelper.getUUIDArr(compound, "test")));
            }
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
