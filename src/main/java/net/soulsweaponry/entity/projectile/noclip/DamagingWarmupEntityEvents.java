package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.ParticleRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class DamagingWarmupEntityEvents {

    public static final Map<Integer, BiConsumer<DamagingWarmupEntity, OtherAttributes>> EVENTS = new HashMap<>();
    private static int idCounter = 0;
    public static final int SPAWN_FIRE = getEventId();
    public static final int SPAWN_MOLTEN_METAL = getEventId();
    public static final int SPAWN_LIGHTNING = getEventId();

    static {
        EVENTS.put(SPAWN_FIRE, (entity, otherAttributes) -> {
            World world = entity.getEntityWorld();
            if (world.getBlockState(entity.getBlockPos()).isAir()) {
                world.setBlockState(entity.getBlockPos(), Blocks.FIRE.getDefaultState());
            }
            if (entity.getParticleAmountMod() > 0) {
                Map<ParticleEffect, Vec3d> map = Map.of(ParticleTypes.WAX_ON, entity.getParticleVec(), ParticleTypes.FLAME, entity.getParticleVec(), ParticleRegistry.SUN_PARTICLE.get(), entity.getParticleVec());
                ParticleHandler.particleOutburstMap(world, Math.min(30 * (int) entity.getParticleAmountMod(), 100), entity.getX(), entity.getY(), entity.getZ(), map, 0.4f);
            }
        });
        EVENTS.put(SPAWN_MOLTEN_METAL, (entity, otherAttributes) -> {
            World world = entity.getEntityWorld();
            MoltenMetal crack = new MoltenMetal(world, otherAttributes.newEntityRadius);
            crack.setDamage(otherAttributes.newEntityDamage);
            crack.setOwner(entity.getOwner());
            crack.setYaw(entity.getYaw());
            crack.setPos(entity.getX(), entity.getY(), entity.getZ());
            world.spawnEntity(crack);
        });
        EVENTS.put(SPAWN_LIGHTNING, (entity, otherAttributes) -> {
            World world = entity.getEntityWorld();
            if (world.isSkyVisible(entity.getBlockPos())) {
                LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                lightningEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
                world.spawnEntity(lightningEntity);
            }
        });
    }

    private static int getEventId() {
        return idCounter++;
    }

    /**
     * Used in the {@link #EVENTS} {@link BiConsumer} to give additional values to the stuff the event does, like for example
     * providing damage and radius values to the {@link MoltenMetal} entities spawned in the {@link #SPAWN_MOLTEN_METAL}
     * event without relying on the original {@link DamagingWarmupEntity}
     */
    public record OtherAttributes(float newEntityDamage, float newEntityRadius) {

        public OtherAttributes() {
            this(0f, 0f);
        }

        public static OtherAttributes getInstanceFromNbt(NbtCompound nbt) {
            return new OtherAttributes(nbt.getFloat("NewEntityDamage"), nbt.getFloat("NewEntityRadius"));
        }

        public void writeCustomDataToNbt(NbtCompound nbt) {
            nbt.putFloat("NewEntityDamage", this.newEntityDamage);
            nbt.putFloat("NewEntityRadius", this.newEntityRadius);
        }
    }
}