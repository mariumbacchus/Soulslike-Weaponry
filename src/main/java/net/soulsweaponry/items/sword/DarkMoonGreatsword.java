package net.soulsweaponry.items.sword;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.items.ChargeToUseItem;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

public class DarkMoonGreatsword extends ChargeToUseItem implements IKeybindAbility {

    public DarkMoonGreatsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.dark_moon_greatsword_damage, ConfigConstructor.dark_moon_greatsword_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.FREEZE, TooltipAbilities.PERMAFROST, TooltipAbilities.FROST_MOON);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            target.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING, ConfigConstructor.dark_moon_greatsword_post_hit_permafrost_base_duration,
                    ConfigConstructor.dark_moon_greatsword_post_hit_permafrost_base_amplifier + WeaponUtil.getEnchantDamageBonus(stack)));
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                stack.damage(1, playerEntity, (p) -> p.sendToolBreakStatus(user.getActiveHand()));
                int duration = ConfigConstructor.dark_moon_greatsword_projectile_permafrost_base_duration;
                int amp = ConfigConstructor.dark_moon_greatsword_projectile_permafrost_base_amplifier + WeaponUtil.getEnchantDamageBonus(stack);
                MoonlightProjectile entity = new MoonlightProjectile(EntityRegistry.DARK_MOON_PROJECTILE, world, user, stack);
                entity.setAppliedStatusEffect(EffectRegistry.FREEZING);
                entity.setEffectAmplifier(amp);
                entity.setAppliedEffectTicks(duration);
                entity.setAgeAndPoints(30, 150, 4);
                entity.setAreaParticleCount(8);
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 1.5F, 1.0F);
                entity.setDamage(ConfigConstructor.dark_moon_greatsword_projectile_damage);
                world.spawnEntity(entity);
                world.playSound(null, user.getBlockPos(), SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.PLAYERS, 1f, 1f);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.hasStatusEffect(EffectRegistry.FROST_MOON) && !user.isCreative()) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_dark_moon_greatsword;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_dark_moon_greatsword;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.dark_moon_greatsword_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.dark_moon_greatsword_enchant_reduces_cooldown_ids;
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (this.isDisabled(stack) || (player.hasStatusEffect(EffectRegistry.COOLDOWN) && !player.isCreative())) {
            return;
        }
        stack.damage(1, player, (p) -> p.sendToolBreakStatus(player.getActiveHand()));
        int duration = ConfigConstructor.dark_moon_greatsword_frost_moon_base_duration;
        int amp = ConfigConstructor.dark_moon_greatsword_frost_moon_base_amplifier;
        int cooldown = ConfigConstructor.dark_moon_greatsword_frost_moon_cooldown;
        cooldown = Math.max(ConfigConstructor.dark_moon_greatsword_frost_moon_min_cooldown, cooldown - this.getReduceCooldownEnchantLevel(stack) * 60);
        this.applyEffectCooldown(player, cooldown);
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FROST_MOON, duration, amp));
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_SKELETON_CONVERTED_TO_STRAY, player.getSoundCategory(), 1f, 1f);
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {

    }
}
