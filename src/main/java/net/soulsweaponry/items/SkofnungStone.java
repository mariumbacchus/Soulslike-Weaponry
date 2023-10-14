package net.soulsweaponry.items;

import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class SkofnungStone extends Item {

    public SkofnungStone(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stoneStack = user.getStackInHand(hand);
        boolean shouldDamage = false;
        for (Hand offHand : Hand.values()) {
            ItemStack swordStack = user.getStackInHand(offHand);
            if (swordStack.getItem() instanceof Skofnung && swordStack.hasNbt()) {
                swordStack.getNbt().putInt(Skofnung.EMPOWERED, ConfigConstructor.skofnung_stone_additional_empowered_strikes);
                shouldDamage = true;
                world.playSound(user, user.getBlockPos(), SoundRegistry.SHARPEN_EVENT, SoundCategory.PLAYERS, .5f, 1f);
                world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, .5f, .5f);
            }
        }
        for (StatusEffect effect : this.harmfulEffects()) {
            if (user.hasStatusEffect(effect)) {
                user.removeStatusEffect(effect);
                world.playSound(user, user.getBlockPos(), SoundRegistry.RESTORE_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                shouldDamage = true;
            }
        }
        
        if (shouldDamage) {
            stoneStack.damage(1, user, e -> e.sendToolBreakStatus(hand));
            return TypedActionResult.success(user.getStackInHand(hand));
        } else {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.DISABLE_DEBUFS, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    public StatusEffect[] harmfulEffects() {
        return new StatusEffect[] {
            StatusEffects.BLINDNESS,
            StatusEffects.DARKNESS,
            StatusEffects.HUNGER,
            StatusEffects.INSTANT_DAMAGE,
            StatusEffects.LEVITATION,
            StatusEffects.MINING_FATIGUE,
            StatusEffects.NAUSEA,
            StatusEffects.POISON,
            StatusEffects.SLOWNESS,
            StatusEffects.UNLUCK,
            StatusEffects.WEAKNESS,
            StatusEffects.WITHER,
            EffectRegistry.POSTURE_BREAK,
            EffectRegistry.BLEED,
            EffectRegistry.DISABLE_HEAL,
            EffectRegistry.FEAR,
            EffectRegistry.FREEZING,
            EffectRegistry.DECAY,
            EffectRegistry.RETRIBUTION,
            EffectRegistry.BLIGHT,
        };
    }
}
