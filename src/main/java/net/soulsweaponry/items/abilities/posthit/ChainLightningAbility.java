package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.ChainLightning;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record ChainLightningAbility(float baseRange, float bonusRangePerLvl, float baseDamage, float bonusDamagePerLvl) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        float radius = this.baseRange + this.bonusRangePerLvl * lvl;
        float damage = this.baseDamage + this.bonusDamagePerLvl * lvl;
        ChainLightning.trigger(attacker.getWorld(), target, attacker, damage, radius);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.chain_lightning").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.chain_lightning.1").formatted(Formatting.GRAY)
        );
    }
}
