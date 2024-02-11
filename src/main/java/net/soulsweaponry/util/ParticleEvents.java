package net.soulsweaponry.util;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ParticleRegistry;

import java.util.HashMap;
import java.util.List;

/**
 * In cases where particle events like {@code OBLITERATE} may be used multiple times, all maps or lists for the events with
 * specific {@code velDividers} and {@code ParticleOptions} are predetermined.
 */
public class ParticleEvents {

    // NOTE: Some packets/ids from older versions have been to be moved to other classes since they can be called more easily by their own over there. Those being:
    // DEATH_EXPLOSION_PACKET_ID -> can easily call ParticleHandler.particleSphereList in respective classes with right params
    // RANDOM_EXPLOSION_PACKET_ID -> WitheredWabbajackProjectile since it can easily call ParticleHandler.particleSphere with its own getRandomParticle() method
    // BIG_TELEPORT_ID -> ChaosMonarchGoal, can easily call ParticleHandler.particleSphere with the right parameters
    // DRAGON_BREATH_EXPLOSION_PACKET -> ChaosMonarchGoal, can easily call ParticleHandler.particleSphere with the right parameters
    // DRAGON_BREATH_EXPLOSION_PACKET -> ChaosMonarchGoal, can easily call ParticleHandler.particleSphere with the right parameters
    // SNOW_PARTICLES_ID -> Freezing effect, can easily call the default server.sendParticle method with the right off-set parameters, kinda pointless though since it can call client level
    // ICE_PARTICLES_ID -> can easily call the default ParticleHandler.particleSphere method with the right params in respective classes
    // MOONLIGHT_PARTICLES_ID -> is not needed
    // SWORD_SWIPE_ID -> can be called in respective classes using default serverLevel.sendParticle with right off-set params
    // UMBRAL_TRESPASS_ID -> now that positions are doubles, it is identical to the SOUL_FLAME_SMALL_OUTBURST_MAP
    // CLAW_PARTICLES_ID -> should be reworked into curving lines and may be implemented with simple serverLevel.sendParticle
    // SOUL_FLAME_BIG_OUTBURST_ID -> identical to small version, just tweak the size modifier
    // PRE_EXPLOSION_ID -> can be called easily with ParticleHandler.particleOutburst
    // FLAME_RUPTURE_ID -> not needed if Day Stalker uses Flame Pillars instead of hardcoded attack
    // TRINITY_FLASH_ID -> can be called together with the other particle packet thanks to PacketHandler.flashParticle

    public static final Vec3d FLAT_SPREADING_FLAME = new Vec3d(2, 8, 2);
    public static final Vec3d FLAT_SPREADING_SMOKE = new Vec3d(1, 8, 1);
    public static final Vec3d RISING_FLAME = new Vec3d(8, 2, 8);
    public static final Vec3d RISING_ITEM_PARTICLE = new Vec3d(2, 0.5f, 2);
    public static final Vec3d FLAT_ITEM_PARTICLE = new Vec3d(1, 2, 1);

    public static final ParticleEffect DIRT_PARTICLE = new ItemStackParticleEffect(ParticleTypes.ITEM, Items.DIRT.getDefaultStack());
    public static final ParticleEffect STONE_PARTICLE = new ItemStackParticleEffect(ParticleTypes.ITEM, Items.STONE.getDefaultStack());
    public static final ParticleEffect ICE_PARTICLE = new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.ICE));

    public static final HashMap<ParticleEffect, Vec3d> OBLITERATE_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> MOONFALL_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> SOUL_RUPTURE_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> DAWNBREAKER_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> DARKIN_BLADE_SLAM_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> CONJURE_ENTITY_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> GROUND_RUPTURE_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> BLINDING_LIGHT_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> DEFAULT_GRAND_SKYFALL_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> BASE_GRAND_SKYFALL_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> BLINDING_LIGHT_SMASH_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> SOUL_FLAME_RUPTURE_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> SOUL_FLAME_SMALL_OUTBURST_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> ICE_SMASH_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> BLACKFLAME_SNAKE_PARTICLE_MAP = Maps.newHashMap();
    public static final HashMap<ParticleEffect, Vec3d> FLAME_RUPTURE_MAP = Maps.newHashMap();

    public static final List<ParticleEffect> DARK_EXPLOSION_LIST = List.of(ParticleTypes.LARGE_SMOKE, ParticleTypes.SMOKE, ParticleTypes.POOF);

    static {
        OBLITERATE_MAP.put(ParticleTypes.LARGE_SMOKE, FLAT_SPREADING_SMOKE);
        OBLITERATE_MAP.put(ParticleTypes.SOUL_FIRE_FLAME, FLAT_SPREADING_FLAME);
        OBLITERATE_MAP.put(ParticleTypes.SOUL, FLAT_SPREADING_FLAME);
        OBLITERATE_MAP.put(DIRT_PARTICLE, FLAT_ITEM_PARTICLE);
        OBLITERATE_MAP.put(STONE_PARTICLE, FLAT_ITEM_PARTICLE);

        MOONFALL_MAP.put(ParticleTypes.LARGE_SMOKE, FLAT_SPREADING_SMOKE);
        MOONFALL_MAP.put(ParticleTypes.SOUL_FIRE_FLAME, FLAT_SPREADING_FLAME);
        MOONFALL_MAP.put(ParticleRegistry.NIGHTFALL_PARTICLE, FLAT_SPREADING_FLAME);
        MOONFALL_MAP.put(DIRT_PARTICLE, FLAT_ITEM_PARTICLE);
        MOONFALL_MAP.put(STONE_PARTICLE, FLAT_ITEM_PARTICLE);

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
        GROUND_RUPTURE_MAP.put(DIRT_PARTICLE, RISING_ITEM_PARTICLE);
        GROUND_RUPTURE_MAP.put(STONE_PARTICLE, RISING_ITEM_PARTICLE);

        BLINDING_LIGHT_MAP.put(ParticleTypes.WAX_OFF, new Vec3d(0.05f, 0.05f, 0.05f));
        BLINDING_LIGHT_MAP.put(ParticleTypes.SOUL, new Vec3d(4, 4, 4));

        DEFAULT_GRAND_SKYFALL_MAP.put(ParticleTypes.LARGE_SMOKE, FLAT_SPREADING_SMOKE);
        BASE_GRAND_SKYFALL_MAP.put(ParticleTypes.LARGE_SMOKE, FLAT_SPREADING_SMOKE);
        DEFAULT_GRAND_SKYFALL_MAP.put(ParticleTypes.FLAME, new Vec3d(1, 6, 1));
        DEFAULT_GRAND_SKYFALL_MAP.put(DIRT_PARTICLE, FLAT_ITEM_PARTICLE);
        BASE_GRAND_SKYFALL_MAP.put(DIRT_PARTICLE, FLAT_ITEM_PARTICLE);
        DEFAULT_GRAND_SKYFALL_MAP.put(STONE_PARTICLE, FLAT_ITEM_PARTICLE);
        BASE_GRAND_SKYFALL_MAP.put(STONE_PARTICLE, FLAT_ITEM_PARTICLE);

        BLINDING_LIGHT_SMASH_MAP.put(ParticleTypes.LARGE_SMOKE, FLAT_SPREADING_SMOKE);
        BLINDING_LIGHT_SMASH_MAP.put(ParticleTypes.FIREWORK, FLAT_SPREADING_FLAME);
        BLINDING_LIGHT_SMASH_MAP.put(ParticleTypes.WAX_OFF, FLAT_SPREADING_FLAME);
        BLINDING_LIGHT_SMASH_MAP.put(DIRT_PARTICLE, FLAT_ITEM_PARTICLE);
        BLINDING_LIGHT_SMASH_MAP.put(STONE_PARTICLE, FLAT_ITEM_PARTICLE);

        SOUL_FLAME_RUPTURE_MAP.put(ParticleTypes.SOUL_FIRE_FLAME, new Vec3d(2, 0.5f, 2));
        SOUL_FLAME_RUPTURE_MAP.put(ParticleTypes.LARGE_SMOKE, new Vec3d(2, 0.5f, 2));

        SOUL_FLAME_SMALL_OUTBURST_MAP.put(ParticleTypes.LARGE_SMOKE, new Vec3d(3, 3, 3));
        SOUL_FLAME_SMALL_OUTBURST_MAP.put(ParticleTypes.SOUL_FIRE_FLAME, new Vec3d(3, 3, 3));

        ICE_SMASH_MAP.put(ParticleTypes.CLOUD, FLAT_SPREADING_SMOKE);
        ICE_SMASH_MAP.put(ParticleTypes.SOUL, FLAT_SPREADING_FLAME);
        ICE_SMASH_MAP.put(ICE_PARTICLE, new Vec3d(1, 1, 1));

        BLACKFLAME_SNAKE_PARTICLE_MAP.put(ParticleTypes.LARGE_SMOKE, RISING_ITEM_PARTICLE);
        BLACKFLAME_SNAKE_PARTICLE_MAP.put(ParticleRegistry.DAZZLING_PARTICLE, RISING_ITEM_PARTICLE);
        BLACKFLAME_SNAKE_PARTICLE_MAP.put(ParticleRegistry.DARK_STAR, RISING_ITEM_PARTICLE);

        FLAME_RUPTURE_MAP.put(ParticleTypes.FLAME, new Vec3d(4, 0.5f, 4));
        FLAME_RUPTURE_MAP.put(ParticleTypes.WAX_ON, new Vec3d(0.1f, 0.01f, 0.1f));
        FLAME_RUPTURE_MAP.put(ParticleTypes.SMALL_FLAME, new Vec3d(4, 0.5f, 4));
    }

    public static void mjolnirLeviathanAxeCollision(World world, double x, double y, double z) {
        HashMap<ParticleEffect, Vec3d> map = Maps.newHashMap();
        Vec3d vec = new Vec3d(1, 1, 1);
        map.put(ParticleTypes.ELECTRIC_SPARK, vec);
        map.put(ParticleTypes.CLOUD, vec);
        map.put(ParticleTypes.SCRAPE, vec);
        map.put(ParticleTypes.GLOW, vec);
        ParticleHandler.particleSphereList(world, 1000, x, y, z, 1f, ParticleTypes.LARGE_SMOKE, ParticleRegistry.NIGHTFALL_PARTICLE);
        ParticleHandler.particleOutburstMap(world, 500, x, y, z, map, 1f);
        ParticleHandler.flashParticle(world, x, y, z, new ParticleHandler.RGB(79, 255, 243), 10f);
    }

    public static void airCombustionEvent(World world, double x, double y, double z) {
        HashMap<ParticleEffect, Vec3d> map = Maps.newHashMap();
        Vec3d vec = new Vec3d(3, 3, 3);
        map.put(ParticleTypes.LARGE_SMOKE, vec);
        map.put(ParticleTypes.FLAME, vec);
        map.put(ParticleTypes.SMALL_FLAME, vec);
        ParticleHandler.particleOutburstMap(world, 150, x, y, z, map, 1f);
        ParticleHandler.flashParticle(world, x, y, z, new ParticleHandler.RGB(201, 64, 0), 1);
    }
}
