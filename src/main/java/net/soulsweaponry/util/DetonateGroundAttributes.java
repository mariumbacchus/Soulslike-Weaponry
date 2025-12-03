package net.soulsweaponry.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.items.hammer.Nightfall;
import net.soulsweaponry.particles.ParticleEvents;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Map;

/**
 * @param baseExpansion
 * @param expansionMod Modifier to multiply the fall distance (after division of 10) before adding to the total expansion
 *                     of the bounding box of the player, which determines the total range of the AOE
 * @param launchMod
 * @param maxLaunchPower
 * @param maxExpansion
 * @param maxDamage on each entity
 * @param fallDistanceDamageMod Modifier to multiply the fall distance before adding to total damage
 * @param healMod Healing modifier to multiply the total damage dealt to the target
 *                <p>NOTE: This is called on every target hit, so multiple targets = big heal</p>
 * @param particles Additional particles on top of {@linkplain ParticleEvents#BASE_GRAND_SKYFALL_MAP}
 *                  with vector divider to determine the direction of the particles
 * @param onEntityDamage Post hit effects for all the targets hit, like applying custom status effects or spawning a
 *                      {@linkplain net.soulsweaponry.entity.mobs.Remnant} like in {@linkplain Nightfall}'s case.
 *                      Called for each individual living entity hit. The {@link TriConsumer} supplies
 *                      {@code LivingEntity target, LivingEntity user, float fallDistance}
 * @param onTrigger {@link TriConsumer} supplying {@code LivingEntity user, float fallDistance, ItemStack stack} to
 *                  add additional effects on impact in general, like spawning entities around the user, i.e. Flame Pillars
 */
public record DetonateGroundAttributes(float baseExpansion, float expansionMod, float launchMod, float maxLaunchPower, float maxExpansion, float maxDamage, float fallDistanceDamageMod, float healMod, Map<ParticleEffect, Vec3d> particles, TriConsumer<LivingEntity, LivingEntity, Float> onEntityDamage, TriConsumer<LivingEntity, Float, ItemStack> onTrigger) {

}