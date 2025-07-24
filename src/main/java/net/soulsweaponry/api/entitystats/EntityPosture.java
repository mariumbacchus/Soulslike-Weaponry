package net.soulsweaponry.api.entitystats;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.PostureData;
import net.soulsweaponry.registry.AttributeRegistry;

import java.util.Optional;

public class EntityPosture {

    public static final int BASE_POSTURE = (int) ConfigConstructor.base_posture_unit;

    /**
     * Returns the Max Posture Loss for the entity.
     * This will either be the {@code max_posture_loss} value inside the json file or calculated based on the
     * entity hitbox size with {@code base_posture_unit}.
     * <p>
     * If the {@code max_posture_loss} value inside the file {@code == 0} (automatically 0 if not specified in file),
     * then the max loss will be calculated based on the {@code base_posture_unit and hitbox_volume} instead. For
     * context, volume of the player is around 0.648. {@code base_posture_unit} is defined in config as the base
     * for all entities, but having other values in json file will override the config. Entities with the
     * {@code BASE_POSTURE_INCREASE} attribute also increases the base value, in turn increasing the max posture.
     * </p>
     * <p>
     * If the {@code max_posture_loss} value inside the file {@code < 0}, then that entity will not take posture
     * loss and the mechanic is disabled.
     * </p>
     */
    public static int getMaxPostureLoss(LivingEntity entity) {
        if (entity instanceof PlayerEntity player && entity.getWorld().isClient) {
            return PostureData.getMaxPosture(player);
        }
        Optional<EntityStats> op = EntityStatsUtil.getStats(entity);
        int base = BASE_POSTURE;
        if (op.isPresent()) {
            EntityStats stats = op.get();
            if (stats.max_posture_loss != 0) {
                return Math.round(stats.max_posture_loss);
            }
            if (stats.base_posture_unit > 0) {
                base = Math.round(stats.base_posture_unit);
            }
        }
        var inst = entity.getAttributeInstance(AttributeRegistry.BASE_POSTURE_INCREASE);
        double bonus = inst != null ? inst.getValue() : 0f;
        base += (int) bonus;
        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        float volume = dimensions.height() * dimensions.width() * dimensions.width();
        int value = (int) (base * (1 + Math.log1p(volume) / 3.5));
        if (entity instanceof PlayerEntity player) {
            PostureData.updateMaxPosture(player, value);
        }
        return value;
    }

    /**
     * Returns true for that entity if either {@code ConfigConstructor.disable_posture_mechanic_for_all_mobs} is
     * true or the {@code max_posture_loss} value inside the json file for that entity is {@code < 0}.
     */
    public static boolean isPostureDisabled(LivingEntity entity) {
        Optional<EntityStats> op = EntityStatsUtil.getStats(entity);
        if (op.isPresent()) {
            EntityStats stats = op.get();
            return (stats.max_posture_loss < 0) || ConfigConstructor.disable_posture_mechanic_for_all_mobs;
        }
        return ConfigConstructor.disable_posture_mechanic_for_all_mobs;
    }

    /**
     * Calculates Posture Loss buildup based on the Posture Loss Resistance of the entity.
     * <p> {@code result = amount * 2^(-resistance)} </p>
     */
    public static int getPostureLoss(LivingEntity entity, int amount) {
        float resistance = getPostureResistance(entity);
        return Math.round(EntityStatsUtil.calculateResistance(resistance, amount));
    }

    /**
     * Returns the Posture Loss Buildup Resistance the entity has through datapack and its attribute (armor and stuff)
     */
    public static float getPostureResistance(LivingEntity entity) {
        float base = EntityStatsUtil.getStats(entity)
                .map(s -> s.posture_loss_buildup_resistance)
                .orElse(ConfigConstructor.base_posture_buildup_resistance_unless_overridden);
        var inst = entity.getAttributeInstance(AttributeRegistry.POSTURE_BUILDUP_RESISTANCE);
        float bonus = inst != null ? (float) inst.getValue() : 0f;
        return base + bonus;
    }
}