package net.soulsweaponry.mixin;

import net.soulsweaponry.entity.mobs.DarkSorcerer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import net.soulsweaponry.entity.mobs.EvilForlorn;
import net.soulsweaponry.registry.EntityRegistry;

@Mixin(SpawnRestriction.class)
public class EntityMixin {
    
    @Shadow
    public static <T extends MobEntity> void register(EntityType<T> type, SpawnRestriction.Location location,
     Heightmap.Type hType, SpawnRestriction.SpawnPredicate<T> predicate) {

    }

    static {
        register(EntityRegistry.BIG_CHUNGUS, SpawnRestriction.Location.ON_GROUND,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);

        register(EntityRegistry.WITHERED_DEMON, SpawnRestriction.Location.ON_GROUND,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);

        register(EntityRegistry.EVIL_FORLORN, SpawnRestriction.Location.ON_GROUND,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);

        register(EntityRegistry.DARK_SORCERER, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnIgnoreLightLevel);
    }
}
