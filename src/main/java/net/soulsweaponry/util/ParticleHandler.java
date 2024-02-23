package net.soulsweaponry.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.soulsweaponry.networking.PacketHelper;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.networking.PacketIds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ParticleHandler {

    /**
     * Summon particles going out in random directions.
     * @param world world, either server or client sided
     * @param amount amount of particles
     * @param x x cord
     * @param y y cord
     * @param z z cord
     * @param particleMap map containing the particle and velocity divider used in calculations to make the particles go in specific directions
     * @param sizeMod overall size modifier for the whole explosion
     * <p></p>
     * Example usage: <p></p>
     * {@code HashMap<ParticleOptions, Vec3> map = new HashMap<>();
     * map.put(ParticleTypes.SOUL_FIRE_FLAME, new Vec3(2, 8, 2));
     * map.put(new ItemParticleOption(ParticleTypes.ITEM, Items.STONE.getDefaultInstance()), new Vec3(1, 2, 1));
     * ParticleHandler.particleOutburstMap(world, 150, targetArea.getX(), targetArea.getY() + .1f, targetArea.getZ(), map, 1f);}
     */
    public static void particleOutburstMap(World world, int amount, double x, double y, double z, HashMap<ParticleEffect, Vec3d> particleMap, float sizeMod) {
        for (ParticleEffect particle : particleMap.keySet()) {
            particleOutburst(world, amount, x, y, z, particle, particleMap.get(particle), sizeMod);
        }
    }

    /**
     * Summon particles forming a sphere.
     * @param world world, either server or client sided
     * @param amount amount of particles
     * @param x x cord
     * @param y y cord
     * @param z z cord
     * @param particles list containing the particles
     * @param sizeMod overall size modifier for the whole sphere
     * <p></p>
     * Example usage: <p></p>
     * {@code List<ParticleOptions> list = new ArrayList<>();
     * list.add(ParticleTypes.LARGE_SMOKE);
     * list.add(ParticleTypes.FLAME);
     * ParticleHandler.particleSphereList(world, 150, targetArea.getX(), targetArea.getY(), targetArea.getZ(), list, 1f);}
     */
    public static void particleSphereList(World world, int amount, double x, double y, double z, float sizeMod, ParticleEffect... particles) {
        for (ParticleEffect particle : particles) {
            particleSphere(world, amount, x, y, z, particle, sizeMod);
        }
    }

    public static void particleSphereList(World world, int amount, double x, double y, double z, List<ParticleEffect> particles, float sizeMod) {
        for (ParticleEffect particle : particles) {
            particleSphere(world, amount, x, y, z, particle, sizeMod);
        }
    }

    /**
     * Do note that this will put the particles in the same position as the others since nothing is randomized in the packet calculations.
     */
    public static void singleParticleList(World world, double x, double y, double z, double velX, double velY, double velZ, ParticleEffect... particles) {
        for (ParticleEffect particle : particles) {
            singleParticle(world, particle, x, y, z, velX, velY, velZ);
        }
    }

    public static void singleParticle(World world, ParticleEffect particle, double x, double y, double z, double velX, double velY, double velZ) {
        if (world.isClient) {
            world.addParticle(particle, x, y, z, velX, velY, velZ);
        } else {
            ItemStack stack = new ItemStack(Items.AIR);
            if (particle instanceof ItemStackParticleEffect par) {
                stack = par.getItemStack();
                particle = ParticleTypes.FLAME; //Placeholder since the packet replaces this particle with an ItemStackParticle
            }
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeIdentifier(Registry.PARTICLE_TYPE.getId(particle.getType()));
            buf.writeItemStack(stack);
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            buf.writeDouble(velX);
            buf.writeDouble(velY);
            buf.writeDouble(velZ);
            PacketHelper.sendToAllPlayersS2C((ServerWorld) world, new BlockPos(x, y, z), PacketIds.SINGLE_PARTICLE, buf);
        }
    }

    public static void particleOutburst(World world, int amount, double x, double y, double z, ParticleEffect particle, Vec3d velDivider, float sizeMod) {
        if (world.isClient) {
            List<Vec3d> list = getParticleOutburstCords(amount, velDivider, sizeMod);
            for (Vec3d vec : list) {
                world.addParticle(particle, x, y, z, vec.x, vec.y, vec.z);
            }
        } else {
            //Sends a packet to the server that directs the info to a client side environment that calls it back to here with client level.
            ItemStack stack = new ItemStack(Items.AIR);
            if (particle instanceof ItemStackParticleEffect par) {
                stack = par.getItemStack();
                particle = ParticleTypes.FLAME; //Placeholder since the packet can't figure out what to do with ParticleTypes.ITEM types
            }
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeIdentifier(Registry.PARTICLE_TYPE.getId(particle.getType()));
            buf.writeInt(amount);
            buf.writeItemStack(stack);
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            buf.writeDouble(velDivider.getX());
            buf.writeDouble(velDivider.getY());
            buf.writeDouble(velDivider.getZ());
            buf.writeFloat(sizeMod);
            PacketHelper.sendToAllPlayersS2C((ServerWorld) world, new BlockPos(x, y, z), PacketIds.OUTBURST_PARTICLES, buf);
        }
    }

    public static void particleSphere(World world, int amount, double x, double y, double z, ParticleEffect particle, float sizeMod) {
        if (world.isClient) {
            List<Vec3d> list = getSphereParticleCords(amount, sizeMod);
            for (Vec3d vec : list) {
                world.addParticle(particle, x, y, z, vec.getX(), vec.getY(), vec.getZ());
            }
        } else {
            //Sends a packet to the server that directs the info to a client side environment that calls it back to here with client level.
            ItemStack stack = new ItemStack(Items.AIR);
            if (particle instanceof ItemStackParticleEffect par) {
                stack = par.getItemStack();
                particle = ParticleTypes.FLAME; //Placeholder since the packet can't figure out what to do with ParticleTypes.ITEM types
            }
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeIdentifier(Registry.PARTICLE_TYPE.getId(particle.getType()));
            buf.writeInt(amount);
            buf.writeItemStack(stack);
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            buf.writeFloat(sizeMod);
            PacketHelper.sendToAllPlayersS2C((ServerWorld) world, new BlockPos(x, y, z), PacketIds.SPHERE_PARTICLES, buf);
        }
    }

    public static void flashParticle(World world, double x, double y, double z, RGB rgb, float expansion) {
        if (world.isClient) {
            Particle flash = MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.FLASH, x, y, z, 0, 0, 0);
            flash.setBoundingBox(new Box(new BlockPos(x, y, z)).expand(expansion));
            flash.setColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
        } else {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            buf.writeFloat(rgb.getRed());
            buf.writeFloat(rgb.getGreen());
            buf.writeFloat(rgb.getBlue());
            buf.writeFloat(expansion);
            PacketHelper.sendToAllPlayersS2C((ServerWorld) world, new BlockPos(x, y, z), PacketIds.FLASH_PARTICLE, buf);
        }
    }

    /**
     * Returns a list of Vec3 which contains the velocity vectors that should apply to a particle that goes in random directions.
     * @param particleAmount amount
     * @param velDividers modify the position by dividing the general direction they go
     * @param sizeMod modify the whole explosion size
     * @return list of velocity vectors for the particle
     */
    public static List<Vec3d> getParticleOutburstCords(int particleAmount, Vec3d velDividers, double sizeMod) {
        Random random = new Random();
        List<Vec3d> list = new ArrayList<>();
        double d = random.nextGaussian() * 0.05D;
        double e = random.nextGaussian() * 0.05D;
        double f = random.nextGaussian() * 0.05D;
        for(int j = 0; j < particleAmount; ++j) {
            double newX = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d) * sizeMod;
            double newZ = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e) * sizeMod;
            double newY = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + f) * sizeMod;
            Vec3d vec = new Vec3d(newX/velDividers.getX(), newY/velDividers.getY(), newZ/velDividers.getZ());
            list.add(vec);
        }
        return list;
    }

    /**
     * Returns a list of Vec3 which contains the velocity vectors that should apply to a particle forms a circle.
     * @param points amount of particles
     * @param sizeModifier modifies the size of the circle
     * @return list of velocity vectors for the particle
     */
    public static List<Vec3d> getSphereParticleCords(double points, float sizeModifier) {
        List<Vec3d> list = new ArrayList<>();
        double phi = Math.PI * (3. - Math.sqrt(5.));
        for (int i = 0; i < points; i++) {
            double velocityY = 1 - (i/(points - 1)) * 2;
            double radius = Math.sqrt(1 - velocityY*velocityY);
            double theta = phi * i;
            double velocityX = Math.cos(theta) * radius;
            double velocityZ = Math.sin(theta) * radius;
            Vec3d vec = new Vec3d(velocityX * sizeModifier, velocityY * sizeModifier, velocityZ * sizeModifier);
            list.add(vec);
        }
        return list;
    }

    /**
     * Converts basic rgb values to acceptable floats for minecraft
     */
    public static class RGB {
        float r, g, b;

        public RGB(float red, float green, float blue) {
            this.r = red / 255f;
            this.g = green / 255f;
            this.b = blue / 255f;
        }

        public float getRed() {
            return this.r;
        }

        public float getGreen() {
            return this.g;
        }

        public float getBlue() {
            return this.b;
        }
    }
}