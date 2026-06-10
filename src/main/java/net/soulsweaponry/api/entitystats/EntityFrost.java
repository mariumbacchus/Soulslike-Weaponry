package net.soulsweaponry.api.entitystats;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.soulsweaponry.config.EntityStatsConfig;
import net.soulsweaponry.entitydata.FrostData;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.items.abilities.posthit.Permafrost;
import net.soulsweaponry.registry.AttributeRegistry;

import java.util.Optional;

public class EntityFrost {

    /**
     * Calculates Frost buildup based on the Frost Buildup Resistance of the entity.
     * <p> {@code result = amount * 2^(-resistance)} </p>
     */
    public static int getFrostBuildup(LivingEntity entity, int amount) {
        float resistance = getFrostBuildupResistance(entity);
        return Math.round(EntityStatsUtil.calculateResistance(resistance, amount));
    }

    /**
     * Triggers ice explosion AOE and sets frost cooling down to true so that frost
     * buildup cannot be applied during cooldown
     */
    public static void triggerFrost(LivingEntity entity) {
        Permafrost.iceExplosion(entity.getWorld(), entity.getBlockPos(), entity, EntityStatsConfig.frost_base_damage,
                (int) EntityStatsConfig.frost_permafrost_spread_effect_amp, EntityStatsConfig.frost_percent_health_damage, EntityStatsConfig.frost_explosion_range);
        FrostData.setFrostCoolingDown((IEntityDataSaver) entity, true);
    }

    public static int getMaxFrostBuildup(LivingEntity entity) {
        Optional<EntityStats> op = EntityStatsUtil.getStats(entity);
        if (op.isPresent()) {
            EntityStats stats = op.get();
            int max = Math.round(stats.max_frost_buildup);
            if (max != 0) {
                return max;
            }
        }
        return (int) EntityStatsConfig.max_frost_buildup;
    }

    public static float getFrostTriggerDamage(LivingEntity entity, float amount) {
        float resistance = getFrostDamageResistance(entity);
        return EntityStatsUtil.calculateResistance(resistance, amount);
    }

    /**
     * Returns the Frost Buildup Resistance the entity has through datapack and its attribute (armor and stuff)
     */
    public static float getFrostBuildupResistance(LivingEntity entity) {
        float base = EntityStatsUtil.getStats(entity)
                .map(s -> s.frost_buildup_resistance)
                .orElse(EntityStatsConfig.base_frost_buildup_resistance_unless_overridden);
        var inst = entity.getAttributeInstance(AttributeRegistry.FROST_BUILDUP_RESISTANCE);
        float bonus = inst != null ? (float) inst.getValue() : 0f;
        return base + bonus;
    }

    /**
     * Returns the Frost Damage Resistance the entity has through datapack and its attribute (armor and stuff)
     */
    public static float getFrostDamageResistance(LivingEntity entity) {
        float base = EntityStatsUtil.getStats(entity)
                .map(s -> s.frost_damage_resistance)
                .orElse(EntityStatsConfig.base_frost_damage_resistance_unless_overridden);
        var inst = entity.getAttributeInstance(AttributeRegistry.FROST_DAMAGE_RESISTANCE);
        float bonus = inst != null ? (float) inst.getValue() : 0f;
        return base + bonus;
    }

    /**
     * Returns true for that entity if {@code ConfigConstructor.disable_frost_buildup_mechanic_for_all_mobs} is
     * true, the entity is in the {@code EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES} tag in either common or minecraft,
     * or if the {@code max_frost_buildup} value for that entity is {@code < 0}
     */
    public static boolean isFrostBuildupDisabled(LivingEntity entity) {
        Optional<EntityStats> op = EntityStatsUtil.getStats(entity);
        return entity.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) || EntityStatsConfig.disable_frost_buildup_mechanic_for_all_mobs
                || (op.isPresent() && op.get().max_frost_buildup < 0);
    }
}
