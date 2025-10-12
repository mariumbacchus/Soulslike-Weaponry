package net.soulsweaponry.items.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.sword.Skofnung;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.TooltipAbilities;

import java.util.ArrayList;
import java.util.List;

public class SkofnungStone extends ModdedItem {

    public SkofnungStone(Settings settings) {
        super(settings);
        this.addTooltipAbility(TooltipAbilities.DISABLE_DEBUFS);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stoneStack = user.getStackInHand(hand);
        if (this.isDisabled(stoneStack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(stoneStack);
        }
        boolean shouldDamage = false;
        for (Hand offHand : Hand.values()) {
            ItemStack swordStack = user.getStackInHand(offHand);
            if (swordStack.getItem() instanceof Skofnung) {
                swordStack.set(ComponentRegistry.AMOUNT_USED, (int) ConfigConstructor.skofnung_stone_additional_empowered_strikes);
                shouldDamage = true;
                world.playSound(user, user.getBlockPos(), SoundRegistry.SHARPEN_EVENT, SoundCategory.PLAYERS, .5f, 1f);
                world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, .5f, .5f);
            }
        }
        List<RegistryEntry<StatusEffect>> effects = new ArrayList<>();
        for (StatusEffectInstance effectInstance : user.getStatusEffects()) {
            if (effectInstance.getEffectType().value().getCategory().equals(StatusEffectCategory.HARMFUL)) {
                effects.add(effectInstance.getEffectType());
                shouldDamage = true;
            }
        }
        effects.forEach(user::removeStatusEffect);
        if (shouldDamage) {
            world.playSound(user, user.getBlockPos(), SoundRegistry.RESTORE_EVENT, SoundCategory.PLAYERS, 1f, 1f);
            stoneStack.damage(1, user, LivingEntity.getSlotForHand(hand));
            return TypedActionResult.success(user.getStackInHand(hand));
        } else {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_skofnung_stone;
    }
}