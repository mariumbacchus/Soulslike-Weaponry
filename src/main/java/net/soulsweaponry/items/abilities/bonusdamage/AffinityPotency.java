package net.soulsweaponry.items.abilities.bonusdamage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EffectRegistry;

import java.util.List;

public record AffinityPotency(float damagePerAmp) implements IAbility {

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if (damageSource.getAttacker() instanceof PlayerEntity player && !player.getWorld().isClient && player.hasStatusEffect(EffectRegistry.POTENCY)) {
            return this.damagePerAmp * (player.getStatusEffect(EffectRegistry.POTENCY).getAmplifier() + 1);
        }
        return 0;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.potency").formatted(Formatting.DARK_BLUE),
                Text.translatable("tooltip.soulsweapons.potency.1").formatted(Formatting.GRAY)
        );
    }
}
