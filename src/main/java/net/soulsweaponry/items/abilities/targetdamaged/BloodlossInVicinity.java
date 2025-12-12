package net.soulsweaponry.items.abilities.targetdamaged;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;

public record BloodlossInVicinity(StatusEffectInstance... effectInstance) implements IAbility {

    @Override
    public void onTargetBleedTrigger(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        for (StatusEffectInstance effect : effectInstance) {
            attacker.addStatusEffect(new StatusEffectInstance(effect));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.scent_of_blood").formatted(Formatting.WHITE),
                Text.translatable("tooltip.soulsweapons.scent_of_blood.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.scent_of_blood.2").formatted(Formatting.GRAY)
        );
    }
}
