package net.soulsweaponry.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.soulsweaponry.SoulsWeaponry;

public class CustomDamageSource {

    public static RegistryKey<DamageType> BLEED = createType("bleed");
    public static RegistryKey<DamageType> OBLITERATED = createType("obliterated");
    public static RegistryKey<DamageType> OBLIVION = createType("oblivion");
    public static RegistryKey<DamageType> FREYR_SWORD = createType("freyr_sword");
    public static RegistryKey<DamageType> SHADOW_ORB = createType("shadow_orb");
    public static RegistryKey<DamageType> BEAM = createType("beam");
    public static RegistryKey<DamageType> DRAGON_MIST = createType("dragon_mist");

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
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(SoulsWeaponry.ModId, name));
    }
}
