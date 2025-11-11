package net.soulsweaponry.items.abilities.targetdamaged;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.posthit.LifeSteal;

import java.util.List;

public class Omnivamp extends LifeSteal {

    public Omnivamp(float baseHeal, float bonusHealPerLvl, int minCooldown, int cooldown, int reducedCooldownPerLvl) {
        super(baseHeal, bonusHealPerLvl, minCooldown, cooldown, reducedCooldownPerLvl);
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {}

    @Override
    public boolean onTargetDamaged(DamageSource source, float amount, ItemStack stack, LivingEntity target) {
        if (source.getAttacker() instanceof LivingEntity attacker) {
            this.checkAndHeal(stack, attacker);
        }
        return true;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.omnivamp").formatted(Formatting.DARK_RED),
                Text.translatable("tooltip.soulsweapons.omnivamp.1").formatted(Formatting.GRAY)
        );
    }
}
