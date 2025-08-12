package net.soulsweaponry.items.sword;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.IDragonBonus;
import net.soulsweaponry.items.UltraHeavyWeapon;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.DetonateGroundAttributes;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Map;

public class DragonslayerSwordBerserk extends UltraHeavyWeapon implements IKeybindAbility, IDragonBonus {

    private static final StatusEffectInstance[] CALCULATED_FALL_EFFECTS = new StatusEffectInstance[] {
            new StatusEffectInstance(StatusEffects.WITHER, 140, 1)
    };
    private final DetonateGroundAttributes attributes = new DetonateGroundAttributes(
            ConfigConstructor.heap_of_raw_iron_calculated_fall_base_radius,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_height_increase_radius_modifier,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_target_launch_modifier,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_target_max_launch_power,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_max_radius,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_max_damage,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_height_increase_damage_modifier,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleTypes.FLAME, new Vec3d(1, 6, 1), ParticleRegistry.DARK_STAR, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {
                for (StatusEffectInstance effect : CALCULATED_FALL_EFFECTS) {
                    target.addStatusEffect(effect);
                }
            },
            (user, fallDistance, stack) -> {}
    );

    public DragonslayerSwordBerserk(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.heap_of_raw_iron_damage, ConfigConstructor.heap_of_raw_iron_attack_speed, settings, true);
        this.addTooltipAbility(TooltipAbilities.RAGE, TooltipAbilities.DRAGONS_SCOURGE);
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity user) {
        if (!user.getItemCooldownManager().isCoolingDown(this)) {
            stack.damage(1, user, WeaponUtil.getActiveHandSlot(user));
            this.applyItemCooldown(user, this.getScaledCooldown(stack));
            int power = MathHelper.floor(WeaponUtil.getEnchantDamageBonus(stack) / 2f);
            user.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY, 200, power));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 0));
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, .75f, 1f);
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.heap_of_raw_iron_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.heap_of_raw_iron_enchant_reduces_cooldown_ids;
    }

    protected int getScaledCooldown(ItemStack stack) {
        float base = ConfigConstructor.heap_of_raw_iron_cooldown;
        return (int) Math.max(ConfigConstructor.heap_of_raw_iron_min_cooldown, base - this.getReduceCooldownEnchantLevel(stack) * 20);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_heap_of_raw_iron;
    }

    @Override
    public float getBaseDragonBonus(ItemStack stack) {
        return ConfigConstructor.heap_of_raw_iron_dragons_scourge_bonus;
    }

    @Override
    public DetonateGroundAttributes getDetonationAttributes() {
        return this.attributes;
    }

    @Override
    public int getPostureLoss() {
        return (int) ConfigConstructor.heap_of_raw_iron_posture_loss;
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return this.getDragonBonus(target, baseAttackDamage, damageSource);
    }
}