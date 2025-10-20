package net.soulsweaponry.items.sword;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.misc.SkofnungStone;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Optional;

public class Skofnung extends ModdedSword {

    /**
     * The Skofnung sword will add a status effect that disables all healing on the target for a period of time.
     * This effect can be removed, however, by using the Skofnung Stone. Additionally, when the stone is used while 
     * Skofnung is in the other hand, it temporarily sharpens the Skofnung sword, empowering it for the next 8 hits.
     * The empowering of the sword is coded in the {@link SkofnungStone} class.
     */
    public Skofnung(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.skofnung_damage, ConfigConstructor.skofnung_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.DISABLE_HEAL, TooltipAbilities.SHARPEN, TooltipAbilities.IS_SHARPENED);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        int duration = (int) (ConfigConstructor.skofnung_disable_heal_duration + (WeaponUtil.getEnchantDamageBonus(stack) * 40));
        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.DISABLE_HEAL, duration, 0));
        if (isEmpowered(stack)) {
            BleedData.addBleed(target, (int) ConfigConstructor.skofnung_empowered_bleed_post_hit);
            target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEED, (int) ConfigConstructor.skofnung_empowered_bleed_effect_duration, (int) ConfigConstructor.skofnung_empowered_bleed_effect_amp));
            if (attacker instanceof PlayerEntity player) {
                if (!player.getItemCooldownManager().isCoolingDown(this)) {
                    this.reduceEmpowered(stack, player.getWorld(), attacker);
                    player.getItemCooldownManager().set(this, 5);
                }
            } else {
                this.reduceEmpowered(stack, attacker.getWorld(), attacker);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (this.isDisabled(stack) || world.isClient) {
            return;
        }
        float bonus = isEmpowered(stack) ? ConfigConstructor.skofnung_bonus_damage : 0;
        WeaponUtil.modifyStackAttributes(stack, this.getAttackDamage() + bonus - 1, this.getAttackSpeed());
    }

    public static boolean isEmpowered(ItemStack stack) {
        return Optional.ofNullable(stack.get(ComponentRegistry.SKOFNUNG_EMPOWERED_STRIKES)).orElse(0) > 0 && !ConfigConstructor.disable_use_skofnung;
    }

    public static Integer empAttacksLeft(ItemStack stack) {
        if (isEmpowered(stack)) {
            return Optional.ofNullable(stack.get(ComponentRegistry.SKOFNUNG_EMPOWERED_STRIKES)).orElse(0);
        } else {
            return 0;
        }
    }

    private void reduceEmpowered(ItemStack stack, World world, LivingEntity attacker) {
        if (isEmpowered(stack)) {
            stack.set(ComponentRegistry.SKOFNUNG_EMPOWERED_STRIKES, Optional.ofNullable(stack.get(ComponentRegistry.SKOFNUNG_EMPOWERED_STRIKES)).orElse(1) - 1);
            if (Optional.ofNullable(stack.get(ComponentRegistry.SKOFNUNG_EMPOWERED_STRIKES)).orElse(0) <= 0) {
                world.playSound(null, attacker.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, .75f, 1f);
            }
        }
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_skofnung;
    }
}