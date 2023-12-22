package net.soulsweaponry.util;

import com.google.common.collect.Maps;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

/**
 * In cases where particle events like {@code OBLITERATE} may be used multiple times, all maps or lists for the events with
 * specific {@code velDividers} and {@code ParticleOptions} are predetermined.
 */
public class ParticleMaps {

    public static final HashMap<ParticleOptions, Vec3> OBLITERATE = Maps.newHashMap();
    public static final HashMap<ParticleOptions, Vec3> SOUL_RUPTURE = Maps.newHashMap();

    static {
        OBLITERATE.put(ParticleTypes.LARGE_SMOKE, new Vec3(1, 8, 1));
        OBLITERATE.put(ParticleTypes.SOUL_FIRE_FLAME, new Vec3(2, 8, 2));
        OBLITERATE.put(ParticleTypes.SOUL, new Vec3(2, 8, 2));
        OBLITERATE.put(new ItemParticleOption(ParticleTypes.ITEM, Items.DIRT.getDefaultInstance()), new Vec3(1, 2, 1));
        OBLITERATE.put(new ItemParticleOption(ParticleTypes.ITEM, Items.STONE.getDefaultInstance()), new Vec3(1, 2, 1));

        SOUL_RUPTURE.put(ParticleTypes.SOUL, new Vec3(8, 2, 8));
        SOUL_RUPTURE.put(ParticleTypes.SOUL_FIRE_FLAME, new Vec3(8, 2, 8));
        SOUL_RUPTURE.put(ParticleTypes.LARGE_SMOKE, new Vec3(8, 2, 8));
    }
}
