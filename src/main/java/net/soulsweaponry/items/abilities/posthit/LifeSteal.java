package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class LifeSteal implements IAbility {

    public final float baseHeal;
    public final float bonusHealPerLvl;
    public final int minCooldown;
    public final int cooldown;
    public final int reducedCooldownPerLvl;

    public LifeSteal(float baseHeal, float bonusHealPerLvl, int minCooldown, int cooldown, int reducedCooldownPerLvl) {
        this.baseHeal = baseHeal;
        this.bonusHealPerLvl = bonusHealPerLvl;
        this.minCooldown = minCooldown;
        this.cooldown = cooldown;
        this.reducedCooldownPerLvl = reducedCooldownPerLvl;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        this.checkAndHeal(stack, attacker);
    }

    public void checkAndHeal(ItemStack stack, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && !this.isCoolingDown(player, stack) && attacker.getHealth() < attacker.getMaxHealth()) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            attacker.heal(this.baseHeal + this.bonusHealPerLvl * lvl);
            this.applyItemCooldown(stack, player, Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * lvl));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.life_steal").formatted(Formatting.DARK_RED),
                Text.translatable("tooltip.soulsweapons.life_steal.1").formatted(Formatting.GRAY)
        );
    }
}
