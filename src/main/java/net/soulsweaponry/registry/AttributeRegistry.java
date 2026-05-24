package net.soulsweaponry.registry;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;

import java.util.function.Supplier;

public class AttributeRegistry {

    public static final DeferredRegister<EntityAttribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, SoulsWeaponry.ModId);

    public static RegistryObject<EntityAttribute> POSTURE_BUILDUP_RESISTANCE = register("posture_buildup_resistance", 0.0, -10000.0, 10000.0);
    public static RegistryObject<EntityAttribute> BASE_POSTURE_INCREASE = register("base_posture_increase", 0.0, -10000.0, 10000.0);
    public static RegistryObject<EntityAttribute> BLEED_BUILDUP_RESISTANCE = register("bleed_buildup_resistance", 0.0, -10000.0, 10000.0);
    public static RegistryObject<EntityAttribute> BLEED_DAMAGE_RESISTANCE = register("bleed_damage_resistance", 0.0, -10000.0, 10000.0);
    public static RegistryObject<EntityAttribute> FROST_BUILDUP_RESISTANCE = register("frost_buildup_resistance", 0.0, -10000.0, 10000.0);
    public static RegistryObject<EntityAttribute> FROST_DAMAGE_RESISTANCE = register("frost_damage_resistance", 0.0, -10000.0, 10000.0);

    public static RegistryObject<EntityAttribute> register(String id, double fallback, double min, double max) {
        return register(id, () -> createClampedAttribute(id, fallback, min, max));
    }

    public static RegistryObject<EntityAttribute> register(String id, Supplier<EntityAttribute> attribute) {
        return ATTRIBUTES.register(id, attribute);
    }

    public static void register(IEventBus bus) {
        ATTRIBUTES.register(bus);
    }

    public static ClampedEntityAttribute createClampedAttribute(String attributeName, double fallback, double min, double max) {
        return new ClampedEntityAttribute("attribute." + SoulsWeaponry.ModId + "." + attributeName, fallback, min, max);
    }
}