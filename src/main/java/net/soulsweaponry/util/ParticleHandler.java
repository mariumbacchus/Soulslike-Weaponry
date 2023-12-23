package net.soulsweaponry.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.FlashParticleS2C;
import net.soulsweaponry.networking.packets.S2C.ParticleOutburstS2C;
import net.soulsweaponry.networking.packets.S2C.ParticleSphereS2C;

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
    public static void particleOutburstMap(Level world, int amount, double x, double y, double z, HashMap<ParticleOptions, Vec3> particleMap, float sizeMod) {
        for (ParticleOptions particle : particleMap.keySet()) {
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
    public static void particleSphereList(Level world, int amount, double x, double y, double z, float sizeMod, ParticleOptions... particles) {
        for (ParticleOptions particle : particles) {
            particleSphere(world, amount, x, y, z, particle, sizeMod);
        }
    }

    public static void particleOutburst(Level world, int amount, double x, double y, double z, ParticleOptions particle, Vec3 velDivider, float sizeMod) {
        if (world.isClientSide) {
            List<Vec3> list = getParticleOutburstCords(amount, velDivider, sizeMod);
            for (Vec3 vec : list) {
                world.addParticle(particle, x, y, z, vec.x, vec.y, vec.z);
            }
        } else {
            //Sends a packet to the server that directs the info to a client side environment that calls it back to here with client level.
            ItemStack stack = new ItemStack(Items.AIR);
            if (particle instanceof ItemParticleOption par) {
                stack = par.getItem();
                particle = ParticleTypes.FLAME; //Placeholder since the packet can't figure out what to do with ParticleTypes.ITEM types
            }
            ModMessages.sendToAllPlayers(new ParticleOutburstS2C(amount, x, y, z, particle, velDivider, sizeMod, stack));
        }
    }

    public static void particleSphere(Level world, int amount, double x, double y, double z, ParticleOptions particle, float sizeMod) {
        if (world.isClientSide) {
            List<Vec3> list = getSphereParticleCords(amount, sizeMod);
            for (Vec3 vec : list) {
                world.addParticle(particle, x, y, z, vec.x(), vec.y(), vec.z());
            }
        } else {
            //Sends a packet to the server that directs the info to a client side environment that calls it back to here with client level.
            ItemStack stack = new ItemStack(Items.AIR);
            if (particle instanceof ItemParticleOption par) {
                stack = par.getItem();
                particle = ParticleTypes.FLAME; //Placeholder since the packet can't figure out what to do with ParticleTypes.ITEM types
            }
            ModMessages.sendToAllPlayers(new ParticleSphereS2C(amount, x, y, z, particle, sizeMod, stack));
        }
    }

    public static void flashParticle(Level world, double x, double y, double z, RGB rgb, float expansion) {
        if (world.isClientSide) {
            Particle flash = Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.FLASH, x, y, z, 0, 0, 0);
            flash.setBoundingBox(new AABB(new BlockPos(x, y, z)).inflate(expansion));
            flash.setColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
        } else {
            ModMessages.sendToAllPlayers(new FlashParticleS2C(x, y, z, rgb, expansion));
        }
    }

    /**
     * Returns a list of Vec3 which contains the velocity vectors that should apply to a particle that goes in random directions.
     * @param particleAmount amount
     * @param velDividers modify the position by dividing the general direction they go
     * @param sizeMod modify the whole explosion size
     * @return list of velocity vectors for the particle
     */
    public static List<Vec3> getParticleOutburstCords(int particleAmount, Vec3 velDividers, double sizeMod) {
        Random random = new Random();
        List<Vec3> list = new ArrayList<>();
        double d = random.nextGaussian() * 0.05D;
        double e = random.nextGaussian() * 0.05D;
        double f = random.nextGaussian() * 0.05D;
        for(int j = 0; j < particleAmount; ++j) {
            double newX = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d) * sizeMod;
            double newZ = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e) * sizeMod;
            double newY = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + f) * sizeMod;
            Vec3 vec = new Vec3(newX/velDividers.x(), newY/velDividers.y(), newZ/velDividers.z());
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
    public static List<Vec3> getSphereParticleCords(double points, float sizeModifier) {
        List<Vec3> list = new ArrayList<>();
        double phi = Math.PI * (3. - Math.sqrt(5.));
        for (int i = 0; i < points; i++) {
            double velocityY = 1 - (i/(points - 1)) * 2;
            double radius = Math.sqrt(1 - velocityY*velocityY);
            double theta = phi * i;
            double velocityX = Math.cos(theta) * radius;
            double velocityZ = Math.sin(theta) * radius;
            Vec3 vec = new Vec3(velocityX * sizeModifier, velocityY * sizeModifier, velocityZ * sizeModifier);
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
