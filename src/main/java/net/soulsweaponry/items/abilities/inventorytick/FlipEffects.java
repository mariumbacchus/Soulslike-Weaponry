package net.soulsweaponry.items.abilities.inventorytick;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.*;

public class FlipEffects implements IAbility {

    /**
     * Will contain harmful effects as the key, and the opposite beneficial effect as value
     */
    private static final HashMap<RegistryEntry<StatusEffect>, RegistryEntry<StatusEffect>> FLIPPABLE_EFFECTS = new HashMap<>();
    private final float newEffectDurationMod;
    private final float newEffectAmpMod;
    private final int minCooldown;
    private final int cooldown;
    private final int reducedCooldownPerLvl;

    public FlipEffects(
            float newEffectDurationMod, float newEffectAmpMod, int minCooldown, int cooldown, int reducedCooldownPerLvl
    ) {
        this.newEffectDurationMod = newEffectDurationMod;
        this.newEffectAmpMod = newEffectAmpMod;
        this.minCooldown = minCooldown;
        this.cooldown = cooldown;
        this.reducedCooldownPerLvl = reducedCooldownPerLvl;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && !this.isCoolingDown(player, stack)) {
            this.flipEffects(player, stack);
        }
    }

    private void flipEffects(PlayerEntity player, ItemStack stack) {
        List<StatusEffectInstance> statusEffectsCopy = new ArrayList<>(player.getStatusEffects());
        List<RegistryEntry<StatusEffect>> effectsToRemove = new ArrayList<>();
        boolean triggered = false;
        for (StatusEffectInstance instance : statusEffectsCopy) {
            RegistryEntry<StatusEffect> effect = instance.getEffectType();
            if (effect.value().getCategory() == StatusEffectCategory.HARMFUL) {
                int duration = (int) (instance.getDuration() * this.newEffectDurationMod);
                int amplifier = (int) (instance.getAmplifier() * this.newEffectAmpMod);
                RegistryEntry<StatusEffect> newEffect = FLIPPABLE_EFFECTS.get(instance.getEffectType());
                if (newEffect == null) {
                    newEffect = StatusEffects.REGENERATION;
                }
                effectsToRemove.add(effect);
                triggered = true;
                player.addStatusEffect(new StatusEffectInstance(newEffect, duration, amplifier));
            }
        }
        for (RegistryEntry<StatusEffect> effectToRemove : effectsToRemove) {
            player.removeStatusEffect(effectToRemove);
        }
        if (triggered && !player.isCreative()) {
            this.applyItemCooldown(stack, player, Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * WeaponUtil.getUpgradeLevel(stack)));
        }
    }

    static {
        FLIPPABLE_EFFECTS.put(StatusEffects.SLOWNESS, StatusEffects.SPEED);
        FLIPPABLE_EFFECTS.put(StatusEffects.MINING_FATIGUE, StatusEffects.HASTE);
        FLIPPABLE_EFFECTS.put(StatusEffects.WEAKNESS, StatusEffects.STRENGTH);
        FLIPPABLE_EFFECTS.put(StatusEffects.BLINDNESS, StatusEffects.NIGHT_VISION);
        FLIPPABLE_EFFECTS.put(StatusEffects.HUNGER, StatusEffects.SATURATION);
        FLIPPABLE_EFFECTS.put(StatusEffects.LEVITATION, StatusEffects.SLOW_FALLING);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.effect_reversal").formatted(Formatting.DARK_AQUA),
                Text.translatable("tooltip.soulsweapons.effect_reversal.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.effect_reversal.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.effect_reversal.3").formatted(Formatting.DARK_GRAY)
        );
    }
}
