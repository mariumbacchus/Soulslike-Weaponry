package net.soulsweaponry.items.sword;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ChargeToUseItem;
import net.soulsweaponry.items.ILifeGuard;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;
import java.util.function.Predicate;

public class Excalibur extends ChargeToUseItem implements ILifeGuard {

    private static final List<RegistryEntry<StatusEffect>> EFFECTS = List.of(
            StatusEffects.DARKNESS, StatusEffects.BLINDNESS
    );

    public Excalibur(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.excalibur_damage, ConfigConstructor.excalibur_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.SONIC_BOOM, TooltipAbilities.LIFE_GUARD, TooltipAbilities.LIGHTBRINGER);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof LivingEntity living && living.age % 20 == 0 && (living.getMainHandStack().getItem() instanceof Excalibur || living.getOffHandStack().getItem() instanceof Excalibur)) {
            for (RegistryEntry<StatusEffect> effect : EFFECTS) {
                if (living.hasStatusEffect(effect)) {
                    int duration = (int) ConfigConstructor.excalibur_lightbringer_effects_duration;
                    int amp = (int) ConfigConstructor.excalibur_lightbringer_effects_amplifier;
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, duration, amp));
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, duration, amp));
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, duration, amp));
                    living.removeStatusEffect(effect);
                }
            }
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(this) && world instanceof ServerWorld serverWorld) {
            int time = WeaponUtil.getChargeTime(stack, user, remainingUseTicks);
            if (time >= 10) {
                LivingEntity target;
                Predicate<LivingEntity> nonTeammate = entity -> !entity.isTeammate(user) && !(entity instanceof ArmorStandEntity);
                TargetPredicate targetPredicate = TargetPredicate.createNonAttackable()
                        .setBaseMaxDistance(ConfigConstructor.excalibur_sonic_boom_target_search_range)
                        .ignoreVisibility()
                        .setPredicate(nonTeammate);
                if (user.getAttacking() != null) {
                    target = user.getAttacking();
                    if (user.distanceTo(target) > ConfigConstructor.excalibur_sonic_boom_max_range) {
                        target = world.getClosestEntity(LivingEntity.class, targetPredicate, user, user.getX(), user.getY(), user.getZ(), user.getBoundingBox().expand(16.0));
                    }
                } else {
                    target = world.getClosestEntity(LivingEntity.class, targetPredicate, user, user.getX(), user.getY(), user.getZ(), user.getBoundingBox().expand(16.0));
                }
                if (target != null) {
                    boolean withinRange = user.distanceTo(target) <= ConfigConstructor.excalibur_sonic_boom_max_range;
                    if (!withinRange) {
                        world.playSound(null, user.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE.value(), SoundCategory.PLAYERS, 1f, 1f);
                        if (ConfigConstructor.inform_player_about_out_of_range) {
                            player.sendMessage(Text.translatable("soulsweapons.weapon.out_of_range"), true);
                        }
                        return;
                    }
                    world.sendEntityStatus(user, EntityStatuses.SONIC_BOOM);
                    Vec3d vec3d = user.getPos().add(0.0, 1.6F, 0.0);
                    Vec3d vec3d2 = target.getEyePos().subtract(vec3d);
                    Vec3d vec3d3 = vec3d2.normalize();
                    for (int i = 1; i < MathHelper.floor(vec3d2.length()) + 7; i++) {
                        Vec3d vec3d4 = vec3d.add(vec3d3.multiply(i));
                        ((ServerWorld)world).spawnParticles(ParticleTypes.SONIC_BOOM, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 0.0);
                    }
                    world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 3.0F, 1.0F);
                    target.damage(world.getDamageSources().sonicBoom(user), ConfigConstructor.excalibur_sonic_boom_damage
                            + EnchantmentHelper.getDamage(serverWorld, stack, target, world.getDamageSources().playerAttack(player), 0));
                    user.onAttacking(target);
                    double d = 0.5 * (1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                    double e = 2.5 * (1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                    Vec3d vec = new Vec3d(vec3d3.getX() * e, vec3d3.getY() * d, vec3d3.getZ() * e).multiply(ConfigConstructor.excalibur_sonic_boom_knockback_power);
                    target.addVelocity(vec);
                    stack.damage(2, player, WeaponUtil.getActiveHandSlot(player));
                    this.applyItemCooldown(player, (int) Math.max(ConfigConstructor.excalibur_sonic_boom_min_cooldown, ConfigConstructor.excalibur_sonic_boom_cooldown - this.getReduceCooldownEnchantLevel(stack) * 8));
                } else {
                    world.playSound(null, user.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE.value(), SoundCategory.PLAYERS, 1f, 1f);
                    if (ConfigConstructor.inform_player_about_out_of_range) {
                        player.sendMessage(Text.translatable("soulsweapons.weapon.out_of_range"), true);
                    }
                }
            }
        }
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_excalibur;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.excalibur_enchant_reduces_ability_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.excalibur_enchant_reduces_ability_cooldown_ids;
    }

    @Override
    public double getLifeGuardPercent(ItemStack stack) {
        return ConfigConstructor.excalibur_life_guard_percent;
    }

    @Override
    public double getLifeSaveChance(ItemStack stack) {
        return ConfigConstructor.excalibur_life_save_chance_percent;
    }

    @Override
    public float getLifeSaveExplosionDamage(ItemStack stack) {
        return ConfigConstructor.excalibur_life_save_explosion_damage;
    }

    @Override
    public float getLifeSaveExplosionKnockback(ItemStack stack) {
        return ConfigConstructor.excalibur_life_save_explosion_knockback;
    }

    @Override
    public int getLifeSaveStackDamage(ItemStack stack) {
        return (int) ConfigConstructor.excalibur_life_save_stack_damage;
    }
}
