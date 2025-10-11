package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EffectRegistry;

import java.util.List;

public record Bleed(int baseBleed, float bonusPerBloodthirstyAmp) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int amp = attacker.hasStatusEffect(EffectRegistry.BLOODTHIRSTY) ? attacker.getStatusEffect(EffectRegistry.BLOODTHIRSTY).getAmplifier() + 1 : 0;
        BleedData.addBleed(target, (int) (this.baseBleed + this.bonusPerBloodthirstyAmp * amp));
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.bleed").formatted(Formatting.RED),
                Text.translatable("tooltip.soulsweapons.bleed.1", this.baseBleed).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.bleed.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.bleed.3").formatted(Formatting.GRAY)
        );
    }
}
