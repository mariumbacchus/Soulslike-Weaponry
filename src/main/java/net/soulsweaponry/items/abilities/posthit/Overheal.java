package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record Overheal(int baseAbsorptionAmp, float bonusAmpPerLvl, int baseDuration, float bonusDurationPerLvl, int minCooldown, int cooldown, int reducedCooldownPerLvl) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && attacker.getHealth() >= attacker.getMaxHealth() && !this.isCoolingDown(player, stack)) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            int duration = (int) (this.baseDuration + this.bonusDurationPerLvl * lvl);
            int amp = (int) (this.baseAbsorptionAmp + this.bonusAmpPerLvl * lvl);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, duration, amp));
            this.applyItemCooldown(stack.getItem(), player, Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * lvl));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.overheal").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.overheal.1").formatted(Formatting.GRAY)
        );
    }
}
