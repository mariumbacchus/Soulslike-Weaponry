package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public class AttributeRegistry {

    public static EntityAttribute POSTURE_BUILDUP_RESISTANCE;
    public static EntityAttribute BASE_POSTURE_INCREASE;
    public static EntityAttribute BLEED_BUILDUP_RESISTANCE;
    public static EntityAttribute BLEED_DAMAGE_RESISTANCE;

    public static void init() {
        POSTURE_BUILDUP_RESISTANCE = register("posture_buildup_resistance", 0.0, -10000.0, 10000.0);
        BASE_POSTURE_INCREASE = register("base_posture_increase", 0.0, -10000.0, 10000.0);
        BLEED_BUILDUP_RESISTANCE = register("bleed_buildup_resistance", 0.0, -10000.0, 10000.0);
        BLEED_DAMAGE_RESISTANCE = register("bleed_damage_resistance", 0.0, -10000.0, 10000.0);

        DefaultAttributeContainer.Builder playerAttrs =
                PlayerEntity.createPlayerAttributes()
                        .add(AttributeRegistry.POSTURE_BUILDUP_RESISTANCE)
                        .add(AttributeRegistry.BASE_POSTURE_INCREASE)
                        .add(AttributeRegistry.BLEED_BUILDUP_RESISTANCE)
                        .add(AttributeRegistry.BLEED_DAMAGE_RESISTANCE);
        FabricDefaultAttributeRegistry.register(EntityType.PLAYER, playerAttrs);
    }

    public static EntityAttribute register(String id, double fallback, double min, double max) {
        return register(id, createClampedAttribute(id, fallback, min, max));
    }

    public static EntityAttribute register(String id, EntityAttribute attribute) {
        return Registry.register(Registries.ATTRIBUTE, new Identifier(SoulsWeaponry.ModId, id), attribute);
    }

    public static ClampedEntityAttribute createClampedAttribute(String attributeName, double fallback, double min, double max) {
        return new ClampedEntityAttribute("attribute." + SoulsWeaponry.ModId + "." + attributeName, fallback, min, max);
    }
}
