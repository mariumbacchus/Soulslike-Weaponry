package net.soulsweaponry.items.hammer;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.entity.projectile.noclip.DamagingWarmupEntityEvents;
import net.soulsweaponry.entity.projectile.noclip.FlamePillar;
import net.soulsweaponry.items.UltraHeavyWeapon;
import net.soulsweaponry.items.abilities.posthit.ArmorBreaker;
import net.soulsweaponry.items.abilities.userdamaged.FireThorns;
import net.soulsweaponry.items.abilities.stoppedusing.Flameburst;
import net.soulsweaponry.items.abilities.posthit.MoltenEdge;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAttributes;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Map;

public class Supernova extends UltraHeavyWeapon {

    private static final FireThorns FIRETHORNS = new FireThorns(WeaponConfig.supernova_firethorns_chance, WeaponConfig.supernova_firethorns_damage, (int) WeaponConfig.supernova_firethorns_fire_seconds);
    private static final MoltenEdge MOLTEN_EDGE = new MoltenEdge(WeaponConfig.supernova_firehit_chance, MathHelper.floor(WeaponConfig.supernova_firehit_duration_seconds));
    private static final ArmorBreaker ARMOR_BREAKER = new ArmorBreaker((int) WeaponConfig.supernova_armor_breaker_bonus_stack_damage);
    private static final Flameburst FLAMEBURST = new Flameburst(
            WeaponConfig.supernova_molten_metal_base_radius, WeaponConfig.supernova_molten_metal_radius_per_level,
            WeaponConfig.supernova_molten_metal_base_damage, WeaponConfig.supernova_molten_metal_damage_per_level,
            (int) WeaponConfig.supernova_ability_flame_pillar_amount, 1.75f,
            WeaponConfig.supernova_ability_flame_pillar_radius, WeaponConfig.supernova_ability_flame_pillar_bonus_radius_per_level,
            WeaponConfig.supernova_flame_pillar_base_damage, WeaponConfig.supernova_flame_pillar_damage_per_level,
            (int) WeaponConfig.supernova_ability_min_cooldown, (int) WeaponConfig.supernova_ability_cooldown, (int) WeaponConfig.supernova_ability_reduced_cooldown_per_level
    );

    private static final DetonateGroundAttributes ATTRIBUTES = new DetonateGroundAttributes(
            WeaponConfig.supernova_calculated_fall_base_radius,
            WeaponConfig.supernova_calculated_fall_height_increase_radius_modifier,
            WeaponConfig.supernova_calculated_fall_target_launch_modifier,
            WeaponConfig.supernova_calculated_fall_target_max_launch_power,
            WeaponConfig.supernova_calculated_fall_max_radius,
            WeaponConfig.supernova_calculated_fall_max_damage,
            WeaponConfig.supernova_calculated_fall_height_increase_damage_modifier,
            WeaponConfig.supernova_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleRegistry.SUN_PARTICLE, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {},
            (user, fallDistance, stack) -> {
                int ripples = (int) Math.min(fallDistance * WeaponConfig.supernova_calculated_fall_height_increase_ripples_modifier, WeaponConfig.supernova_calculated_fall_max_flame_pillar_ripples);
                WeaponUtil.doConsumerOnCircle(user.getWorld(), user.getYaw(), user.getPos(), 10, ripples, new Vec2f(1.5f, 1.75f), ((vec3d, warmup, yaw) -> {
                    FlamePillar pillar = new FlamePillar(user.getWorld(), user, 2f, warmup - 6, DamagingWarmupEntityEvents.SPAWN_FIRE);
                    pillar.setYaw(yaw);
                    pillar.setDamage(WeaponConfig.supernova_flame_pillar_base_damage + WeaponConfig.supernova_flame_pillar_damage_per_level * WeaponUtil.getUpgradeLevel(stack));
                    pillar.setPos(vec3d.getX(), vec3d.getY(), vec3d.getZ());
                    pillar.setParticleAmountMod(1.5f);
                    user.getWorld().spawnEntity(pillar);
                    user.getWorld().playSound(null, BlockPos.ofFloored(vec3d), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.HOSTILE, 1f, 1f);
                }));
            }
    );

    public Supernova(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.supernova_damage, WeaponConfig.supernova_attack_speed, settings, (int) WeaponConfig.supernova_posture_loss, ATTRIBUTES);
        this.addAbility(FIRETHORNS, MOLTEN_EDGE, ARMOR_BREAKER, FLAMEBURST);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_supernova;
    }
}
