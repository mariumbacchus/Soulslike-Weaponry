package net.soulsweaponry.items.staff;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.TooltipAbilities;

public class ChungusStaff extends ModdedSword {

    public ChungusStaff(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.chungus_staff_damage, ConfigConstructor.chungus_staff_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.CHUNGUS_INFUSED);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!this.isDisabled(stack) && entity.age % 100 == 0 && entity instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.CHUNGUS_TONIC_EFFECT, 120, 0, true, false));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.isDisabled(stack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(stack);
        }
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1200, 2));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200, 2));
        user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
        if (!user.isCreative()) {
            stack.damage(3, user, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
            user.getItemCooldownManager().set(this, 2400 - this.getReduceCooldownEnchantLevel(stack) * 160);
        }
        return TypedActionResult.success(stack);
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_chungus_staff;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_chungus_staff;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.chungus_staff_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.chungus_staff_enchant_reduces_cooldown_ids;
    }
}
