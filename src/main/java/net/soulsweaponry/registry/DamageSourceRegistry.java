package net.soulsweaponry.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.soulsweaponry.SoulsWeaponry;

public class DamageSourceRegistry {

    public static RegistryKey<DamageType> BLEED = createType("bleed");
    public static RegistryKey<DamageType> OBLITERATED = createType("obliterated");
    public static RegistryKey<DamageType> OBLIVION = createType("oblivion");
    public static RegistryKey<DamageType> FREYR_SWORD = createType("freyr_sword");
    public static RegistryKey<DamageType> SHADOW_ORB = createType("shadow_orb");
    public static RegistryKey<DamageType> BEAM = createType("beam");
    public static RegistryKey<DamageType> DRAGON_MIST = createType("dragon_mist");
    public static RegistryKey<DamageType> PLAYER_LIGHTNING = createType("player_lightning");
    public static RegistryKey<DamageType> PLAYER_FIRE = createType("player_fire");
    public static RegistryKey<DamageType> MAGIC_DAMAGE_BYPASS_COOLDOWN = createType("magic_damage_bypass_cooldown");

    public static DamageSource create(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(key).get());
    }

    public static DamageSource create(World world, RegistryKey<DamageType> key, Entity attacker) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(key).get(), attacker);
    }

    public static DamageSource create(World world, RegistryKey<DamageType> key, Entity source, Entity attacker) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(key).get(), source, attacker);
    }

    public static RegistryKey<DamageType> createType(String name) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(SoulsWeaponry.ModId, name));
    }

    public static void bootstrap(Registerable<DamageType> registerable) {
        registerable.register(BLEED, new DamageType("bleed", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        registerable.register(OBLITERATED, new DamageType("obliterated", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        registerable.register(OBLIVION, new DamageType("oblivion", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        registerable.register(FREYR_SWORD, new DamageType("freyr_sword", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        registerable.register(SHADOW_ORB, new DamageType("shadow_orb", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        registerable.register(BEAM, new DamageType("beam", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        registerable.register(DRAGON_MIST, new DamageType("dragon_mist", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        registerable.register(PLAYER_LIGHTNING, new DamageType("player_lightning", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        registerable.register(PLAYER_FIRE, new DamageType("player_fire", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        registerable.register(MAGIC_DAMAGE_BYPASS_COOLDOWN, new DamageType("magic_damage_bypass_cooldown", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
    }

    public static void init() {}
}
