package net.soulsweaponry.items;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.HashMap;
import java.util.Map;

public class DragonslayerSwordBerserk extends UltraHeavyWeapon implements IKeybindAbility {

    private static final StatusEffectInstance[] CALCULATED_FALL_EFFECTS = new StatusEffectInstance[] {
            new StatusEffectInstance(StatusEffects.WITHER, 140, 1)
    };

    public DragonslayerSwordBerserk(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.heap_of_raw_iron_damage, ConfigConstructor.heap_of_raw_iron_attack_speed, settings, true);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.RAGE);
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_heap_of_raw_iron;
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity user) {
        if (!user.getItemCooldownManager().isCoolingDown(this)) {
            stack.damage(1, user, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
            user.getItemCooldownManager().set(this, ConfigConstructor.heap_of_raw_iron_cooldown);
            int power = MathHelper.floor(WeaponUtil.getEnchantDamageBonus(stack));
            user.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY, 200, power));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 0));
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, .75f, 1f);
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    @Override
    public float getBaseExpansion() {
        return ConfigConstructor.heap_of_raw_iron_calculated_fall_base_radius;
    }

    @Override
    public float getExpansionModifier() {
        return ConfigConstructor.heap_of_raw_iron_calculated_fall_height_increase_radius_modifier;
    }

    @Override
    public float getLaunchModifier() {
        return ConfigConstructor.heap_of_raw_iron_calculated_fall_target_launch_modifier;
    }

    @Override
    public float getMaxExpansion() {
        return ConfigConstructor.heap_of_raw_iron_calculated_fall_max_radius;
    }

    @Override
    public float getMaxDetonationDamage() {
        return ConfigConstructor.heap_of_raw_iron_calculated_fall_max_damage;
    }

    @Override
    public float getFallDamageIncreaseModifier() {
        return ConfigConstructor.heap_of_raw_iron_calculated_fall_height_increase_damage_modifier;
    }

    @Override
    public boolean shouldHeal() {
        return ConfigConstructor.heap_of_raw_iron_calculated_fall_should_heal;
    }

    @Override
    public float getHealFromDamageModifier() {
        return ConfigConstructor.heap_of_raw_iron_calculated_fall_heal_from_damage_modifier;
    }

    @Override
    public void doCustomEffects(LivingEntity target, LivingEntity user) {
        for (StatusEffectInstance effect : CALCULATED_FALL_EFFECTS) {
            target.addStatusEffect(effect);
        }
    }

    @Override
    public Map<ParticleEffect, Vec3d> getParticles() {
        Map<ParticleEffect, Vec3d> map = new HashMap<>();
        map.put(ParticleRegistry.DARK_STAR, new Vec3d(1, 6, 1));
        map.put(ParticleTypes.FLAME, new Vec3d(1, 6, 1));
        return map;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_heap_of_raw_iron;
    }
}