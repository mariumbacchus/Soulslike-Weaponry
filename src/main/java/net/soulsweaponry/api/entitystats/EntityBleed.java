package net.soulsweaponry.api.entitystats;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.config.EntityStatsConfig;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.registry.AttributeRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.Optional;

public class EntityBleed {

    /**
     * Calculates Bleed buildup based on the Bleed Buildup Resistance of the entity.
     * <p> {@code result = amount * 2^(-resistance)} </p>
     */
    public static int getBleedBuildup(LivingEntity entity, int amount) {
        float resistance = getBleedBuildupResistance(entity);
        return Math.round(EntityStatsUtil.calculateResistance(resistance, amount));
    }

    public static void triggerBloodLoss(LivingEntity entity) {
        entity.damage(DamageSourceRegistry.create(entity.getWorld(), DamageSourceRegistry.BLEED), getBleedDamage(entity,
                EntityStatsConfig.bleed_base_damage + entity.getMaxHealth() * EntityStatsConfig.bleed_percent_health_damage));
        entity.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.BLOOD_LOSS, entity.getSoundCategory(), 1f, 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState()),
                    entity.getParticleX(0.5), entity.getBodyY(0.5) + entity.getRandom().nextDouble() * 2 - 1D, entity.getParticleZ(0.5), 30, 0, 0, 0, 0);
        }
        BleedData.setBleed((IEntityDataSaver) entity, 0);
        // Give strength to entities with Bloodlust (or other items later) in hand upon bleed proc
        for (Entity entity1 : entity.getWorld().getOtherEntities(entity, entity.getBoundingBox().expand(20D))) {
            if (entity1 instanceof LivingEntity livingEntity) {
                for (Hand hand : Hand.values()) {
                    ItemStack stack = livingEntity.getStackInHand(hand);
                    if (stack.getItem() instanceof IHasAbilities hasAbilities && !hasAbilities.isDisabled(stack)) {
                        hasAbilities.getAbilities().forEach(a -> a.onTargetBleedTrigger(stack, entity, livingEntity));
                    }
                }
            }
        }
    }

    public static int getMaxBleed(LivingEntity entity) {
        Optional<EntityStats> op = EntityStatsUtil.getStats(entity);
        if (op.isPresent()) {
            EntityStats stats = op.get();
            int max = Math.round(stats.max_bleed);
            if (max != 0) {
                return max;
            }
        }
        return (int) EntityStatsConfig.max_bleed;
    }

    public static float getBleedDamage(LivingEntity entity, float amount) {
        float resistance = getBleedDamageResistance(entity);
        return EntityStatsUtil.calculateResistance(resistance, amount);
    }

    /**
     * Returns the Bleed Buildup Resistance the entity has through datapack and its attribute (armor and stuff)
     */
    public static float getBleedBuildupResistance(LivingEntity entity) {
        float base = EntityStatsUtil.getStats(entity)
                .map(s -> s.bleed_buildup_resistance)
                .orElse(EntityStatsConfig.base_bleed_buildup_resistance_unless_overridden);
        var inst = entity.getAttributeInstance(AttributeRegistry.BLEED_BUILDUP_RESISTANCE);
        float bonus = inst != null ? (float) inst.getValue() : 0f;
        return base + bonus;
    }

    /**
     * Returns the Bleed Damage Resistance the entity has through datapack and its attribute (armor and stuff)
     */
    public static float getBleedDamageResistance(LivingEntity entity) {
        float base = EntityStatsUtil.getStats(entity)
                .map(s -> s.bleed_damage_resistance)
                .orElse(EntityStatsConfig.base_bleed_damage_resistance_unless_overridden);
        var inst = entity.getAttributeInstance(AttributeRegistry.BLEED_DAMAGE_RESISTANCE);
        float bonus = inst != null ? (float) inst.getValue() : 0f;
        return base + bonus;
    }

    /**
     * Returns true for that entity if {@code ConfigConstructor.disable_bleed_mechanic_for_all_mobs} is
     * true, the entity is in the skeletons tags in either common or minecraft, or if the {@code max_bleed}
     * value for that entity is {@code < 0}
     */
    public static boolean isBleedDisabled(LivingEntity entity) {
        Optional<EntityStats> op = EntityStatsUtil.getStats(entity);
        return entity.getType().isIn(EntityTypeTags.SKELETONS) || entity.getType().isIn(ModTags.Entities.SKELETONS) || EntityStatsConfig.disable_bleed_mechanic_for_all_mobs
                || (op.isPresent() && op.get().max_bleed < 0);
    }
}
