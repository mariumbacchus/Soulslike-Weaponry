package net.soulsweaponry.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;

/**
 * In cases where particle events like {@code OBLITERATE} may be used multiple times, all maps or lists for the events with
 * specific {@code velDividers} and {@code ParticleOptions} are predetermined.
 */
public class ParticleEvents {
    // TODO
    // NOTE: Some packets/ids from fabric have been/needs to be moved to other classes since they can be called more easily by their own over there. Those being:
    // DEATH_EXPLOSION_PACKET_ID -> can easily call ParticleHandler.particleSphereList in respective classes with right params
    // RANDOM_EXPLOSION_PACKET_ID -> WitheredWabbajackProjectile since it can easily call ParticleHandler.particleSphere with its own getRandomParticle() method
    // BIG_TELEPORT_ID -> ChaosMonarchGoal, can easily call ParticleHandler.particleSphere with the right parameters
    // DRAGON_BREATH_EXPLOSION_PACKET -> ChaosMonarchGoal, can easily call ParticleHandler.particleSphere with the right parameters
    // DRAGON_BREATH_EXPLOSION_PACKET -> ChaosMonarchGoal, can easily call ParticleHandler.particleSphere with the right parameters
    // SNOW_PARTICLES_ID -> Freezing effect, can easily call the default server.sendParticle method with the right off-set parameters
    // ICE_PARTICLES_ID -> can easily call the default server.sendParticle method with the right params in respective classes
    // MOONLIGHT_PARTICLES_ID -> MoonlightProjectile, can easily call ParticleHandler.particleSphere with right params
    // SWORD_SWIPE_ID -> can be called in respective classes using default serverLevel.sendParticle with right off-set params
    // TODO rest of packets

    public static final Vec3 FLAT_SPREADING_FLAME = new Vec3(2, 8, 2);
    public static final Vec3 FLAT_SPREADING_SMOKE = new Vec3(1, 8, 1);
    public static final Vec3 RISING_FLAME = new Vec3(8, 2, 8);
    public static final Vec3 RISING_ITEM_PARTICLE = new Vec3(2, 0.5f, 2);

    public static final HashMap<ParticleOptions, Vec3> OBLITERATE_MAP = Maps.newHashMap();
    public static final HashMap<ParticleOptions, Vec3> SOUL_RUPTURE_MAP = Maps.newHashMap();
    public static final HashMap<ParticleOptions, Vec3> DAWNBREAKER_MAP = Maps.newHashMap();
    public static final HashMap<ParticleOptions, Vec3> DARKIN_BLADE_SLAM_MAP = Maps.newHashMap();
    public static final HashMap<ParticleOptions, Vec3> CONJURE_ENTITY_MAP = Maps.newHashMap();
    public static final HashMap<ParticleOptions, Vec3> GROUND_RUPTURE_MAP = Maps.newHashMap();

    public static final List<ParticleOptions> DARK_EXPLOSION_LIST = List.of(ParticleTypes.LARGE_SMOKE, ParticleTypes.SMOKE, ParticleTypes.POOF);

    static {
        OBLITERATE_MAP.put(ParticleTypes.LARGE_SMOKE, FLAT_SPREADING_SMOKE);
        OBLITERATE_MAP.put(ParticleTypes.SOUL_FIRE_FLAME, FLAT_SPREADING_FLAME);
        OBLITERATE_MAP.put(ParticleTypes.SOUL, FLAT_SPREADING_FLAME);
        OBLITERATE_MAP.put(new ItemParticleOption(ParticleTypes.ITEM, Items.DIRT.getDefaultInstance()), new Vec3(1, 2, 1));
        OBLITERATE_MAP.put(new ItemParticleOption(ParticleTypes.ITEM, Items.STONE.getDefaultInstance()), new Vec3(1, 2, 1));

        SOUL_RUPTURE_MAP.put(ParticleTypes.SOUL, RISING_FLAME);
        SOUL_RUPTURE_MAP.put(ParticleTypes.SOUL_FIRE_FLAME, RISING_FLAME);
        SOUL_RUPTURE_MAP.put(ParticleTypes.LARGE_SMOKE, RISING_FLAME);

        DAWNBREAKER_MAP.put(ParticleTypes.LARGE_SMOKE, FLAT_SPREADING_SMOKE);
        DAWNBREAKER_MAP.put(ParticleTypes.SOUL_FIRE_FLAME, FLAT_SPREADING_FLAME);
        DAWNBREAKER_MAP.put(ParticleTypes.SOUL, FLAT_SPREADING_FLAME);

        DARKIN_BLADE_SLAM_MAP.put(ParticleTypes.LARGE_SMOKE, FLAT_SPREADING_SMOKE);
        DARKIN_BLADE_SLAM_MAP.put(ParticleTypes.FLAME, FLAT_SPREADING_FLAME);
        DARKIN_BLADE_SLAM_MAP.put(ParticleTypes.SOUL, FLAT_SPREADING_FLAME);

        CONJURE_ENTITY_MAP.put(ParticleTypes.SOUL, RISING_FLAME);
        CONJURE_ENTITY_MAP.put(ParticleTypes.DRAGON_BREATH, RISING_FLAME);

        GROUND_RUPTURE_MAP.put(ParticleTypes.LARGE_SMOKE, RISING_ITEM_PARTICLE);
        GROUND_RUPTURE_MAP.put(new ItemParticleOption(ParticleTypes.ITEM, Items.DIRT.getDefaultInstance()), RISING_ITEM_PARTICLE);
        GROUND_RUPTURE_MAP.put(new ItemParticleOption(ParticleTypes.ITEM, Items.STONE.getDefaultInstance()), RISING_ITEM_PARTICLE);
    }

    public static void dawnbreakerEvent(Level world, double x, double y, double z, float sizeMod) {
        ParticleHandler.particleOutburstMap(world, 200, x, y + .1f, z, ParticleEvents.DAWNBREAKER_MAP, sizeMod);
        ParticleHandler.particleSphere(world, 1000, x, y + .1f, z, ParticleTypes.FLAME, sizeMod);
    }
}
