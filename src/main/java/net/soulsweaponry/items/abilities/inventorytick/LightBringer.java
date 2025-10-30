package net.soulsweaponry.items.abilities.inventorytick;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record LightBringer(
        int positiveEffectsDuration, float effectsBonusDurationPerLvl,
        int positiveEffectsAmp, float effectsBonusAmpPerLvl
) implements IAbility {

    private static final List<RegistryEntry<StatusEffect>> REMOVABLE_EFFECTS = List.of(
            StatusEffects.DARKNESS, StatusEffects.BLINDNESS
    );

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof LivingEntity living && living.age % 20 == 0 && selected) { //TODO test if selected is main hand or just hovering item in inventory
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            for (RegistryEntry<StatusEffect> effect : REMOVABLE_EFFECTS) {
                if (living.hasStatusEffect(effect)) {
                    int duration = (int) (this.positiveEffectsDuration + this.effectsBonusDurationPerLvl * lvl);
                    int amp = (int) (this.positiveEffectsAmp + this.effectsBonusAmpPerLvl * lvl);
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, duration, amp));
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, duration, amp));
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, duration, amp));
                    living.removeStatusEffect(effect);
                }
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.lightbringer").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.lightbringer.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.lightbringer.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.lightbringer.3").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.lightbringer.4").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC),
                Text.translatable("tooltip.soulsweapons.lightbringer.5").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC)
        );
    }
}
