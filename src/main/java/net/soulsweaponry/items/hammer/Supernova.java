package net.soulsweaponry.items.hammer;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.noclip.DamagingWarmupEntityEvents;
import net.soulsweaponry.entity.projectile.noclip.FlamePillar;
import net.soulsweaponry.items.UltraHeavyWeapon;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.DetonateGroundAttributes;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Map;

public class Supernova extends UltraHeavyWeapon {

    private final DetonateGroundAttributes attributes = new DetonateGroundAttributes(
            ConfigConstructor.supernova_calculated_fall_base_radius,
            ConfigConstructor.supernova_calculated_fall_height_increase_radius_modifier,
            ConfigConstructor.supernova_calculated_fall_target_launch_modifier,
            ConfigConstructor.supernova_calculated_fall_target_max_launch_power,
            ConfigConstructor.supernova_calculated_fall_max_radius,
            ConfigConstructor.supernova_calculated_fall_max_damage,
            ConfigConstructor.supernova_calculated_fall_height_increase_damage_modifier,
            ConfigConstructor.supernova_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleRegistry.SUN_PARTICLE.get(), new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {},
            (user, fallDistance, stack) -> {
                int ripples = (int) Math.min(fallDistance * ConfigConstructor.supernova_calculated_fall_height_increase_ripples_modifier, ConfigConstructor.supernova_calculated_fall_max_flame_pillar_ripples);
                WeaponUtil.doConsumerOnCircle(user.getWorld(), user.getYaw(), user.getPos(), 10, ripples, new Vec2f(1.5f, 1.75f), ((vec3d, warmup, yaw) -> {
                    FlamePillar pillar = new FlamePillar(user.getWorld(), user, 2f, warmup - 6, DamagingWarmupEntityEvents.SPAWN_FIRE);
                    pillar.setYaw(yaw);
                    pillar.setDamage(ConfigConstructor.supernova_flame_pillar_damage);
                    pillar.setPos(vec3d.getX(), vec3d.getY(), vec3d.getZ());
                    pillar.setParticleAmountMod(1.5f);
                    user.getWorld().spawnEntity(pillar);
                    user.getWorld().playSound(null, BlockPos.ofFloored(vec3d), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f);
                }));
            }
    );

    public Supernova(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.supernova_damage, ConfigConstructor.supernova_attack_speed, settings, true);
        this.addTooltipAbility(TooltipAbilities.MOLTEN_EDGE, TooltipAbilities.ARMOR_BREAKER, TooltipAbilities.FIRETHORNS, TooltipAbilities.FLAMEBURST);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            if (attacker.getRandom().nextFloat() < ConfigConstructor.supernova_firehit_chance) {
                target.setOnFireFor(MathHelper.floor(ConfigConstructor.supernova_firehit_duration_seconds));
            }
            for (EquipmentSlot slot : new EquipmentSlot[]{
                    EquipmentSlot.HEAD,
                    EquipmentSlot.CHEST,
                    EquipmentSlot.LEGS,
                    EquipmentSlot.FEET
            }) {
                ItemStack armorStack = target.getEquippedStack(slot);
                if (!armorStack.isEmpty() && armorStack.isDamageable()) {
                    armorStack.damage(
                            (int) ConfigConstructor.supernova_armor_breaker_bonus_stack_damage,
                            target,
                            e -> e.sendEquipmentBreakStatus(slot)
                    );
                }
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            int chargeTime = WeaponUtil.getChargeTime(stack, remainingUseTicks);
            if (chargeTime >= 10) {
                if (!world.isClient) {
                    float radius = ConfigConstructor.supernova_molten_metal_radius + EnchantmentHelper.getLevel(EnchantRegistry.STAGGER, stack);
                    float damage = ConfigConstructor.supernova_molten_metal_damage + WeaponUtil.getEnchantDamageBonus(stack);
                    int pillars = (int) ConfigConstructor.supernova_ability_flame_pillar_amount;
                    WeaponUtil.doConsumerOnLine(world, user.getYaw() + 90, user.getPos(), 4, pillars, 1.75f,
                            (Vec3d position, Integer warmup, Float yaw) -> {
                                FlamePillar pillar = new FlamePillar(world, user, 1.85f + EnchantmentHelper.getLevel(EnchantRegistry.STAGGER, stack) * 0.5f, warmup, DamagingWarmupEntityEvents.SPAWN_MOLTEN_METAL);
                                pillar.setYaw(yaw);
                                pillar.setOtherAttributes(new DamagingWarmupEntityEvents.OtherAttributes(damage, radius));
                                pillar.setDamage(ConfigConstructor.supernova_flame_pillar_damage + WeaponUtil.getEnchantDamageBonus(stack));
                                pillar.setPos(position.getX(), position.getY(), position.getZ());
                                world.spawnEntity(pillar);
                            }
                    );
                    this.applyItemCooldown(player, MathHelper.floor(Math.max(ConfigConstructor.supernova_ability_min_cooldown, ConfigConstructor.supernova_ability_cooldown - this.getReduceCooldownEnchantLevel(stack) * 15)));
                }
            }
        }
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_supernova;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_supernova;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.supernova_ability_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.supernova_ability_enchant_reduces_cooldown_ids;
    }

    @Override
    public DetonateGroundAttributes getDetonationAttributes() {
        return this.attributes;
    }

    @Override
    public int getPostureLoss() {
        return (int) ConfigConstructor.supernova_posture_loss;
    }
}