package net.soulsweaponry.items.spear;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.noclip.GhostGlaiveEntity;
import net.soulsweaponry.items.BladeDanceItem;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

public class GlaiveOfHodir extends BladeDanceItem {

    public GlaiveOfHodir(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.glaive_of_hodir_damage, ConfigConstructor.glaive_of_hodir_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.GHOST_GLAIVE);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                stack.damage(3, (LivingEntity)playerEntity, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                GhostGlaiveEntity entity = new GhostGlaiveEntity(world, playerEntity, 10);
                entity.setDamage(ConfigConstructor.glaive_of_hodir_projectile_damage + WeaponUtil.getEnchantDamageBonus(stack));
                entity.setPos(playerEntity.getX(), playerEntity.getEyeY() - 0.3f, playerEntity.getZ());
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2f, 1.0F);
                world.spawnEntity(entity);
                world.playSound(playerEntity, playerEntity.getBlockPos(), SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1f, .5f);
                this.applyItemCooldown(playerEntity, Math.max(ConfigConstructor.glaive_of_hodir_projectile_min_cooldown, ConfigConstructor.glaive_of_hodir_projectile_cooldown - this.getReduceCooldownEnchantLevel(stack) * 12));
            }
        }
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_glaive_of_hodir;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_glaive_of_hodir;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.glaive_of_hodir_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.glaive_of_hodir_enchant_reduces_cooldown_ids;
    }

    @Override
    public float getBonusDamagePerStack() {
        return ConfigConstructor.glaive_of_hodir_bonus_damage_per_stack;
    }

    @Override
    public float getBonusAttackSpeedPerStack() {
        return ConfigConstructor.glaive_of_hodir_bonus_attack_speed_per_stack;
    }

    @Override
    public int getMaxStacks() {
        return ConfigConstructor.glaive_of_hodir_max_stacks;
    }

    @Override
    public void applyMaxStacksEffects(LivingEntity entity, ItemStack stack) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, ConfigConstructor.glaive_of_hodir_effects_duration, ConfigConstructor.glaive_of_hodir_resistance_amplifier - 1));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, ConfigConstructor.glaive_of_hodir_effects_duration, ConfigConstructor.glaive_of_hodir_absorption_amplifier - 1));
    }

    @Override
    public int getMaxStacksCooldown() {
        return ConfigConstructor.glaive_of_hodir_add_effects_cooldown;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this.isDisabled(user.getStackInHand(hand))) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        ItemStack itemStack = user.getStackInHand(hand);
        if (ConfigConstructor.prioritize_off_hand_shield_over_weapon && user.getOffHandStack().getItem() instanceof ShieldItem) {
            return TypedActionResult.fail(itemStack);
        }
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        }
        else {
            user.setCurrentHand(hand);
            return TypedActionResult.success(itemStack);
        }
    }
}
