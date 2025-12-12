package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

/**
 * Post hit do bonus magic damage which goes through armor.
 * The total damage the entity takes is base damage from the weapon with enchants, effects, etc.
 * being reduced by armor and other resistances, then the bonus magic damage is added which goes
 * through all those checks.
 * @param bonusMagicDamage
 * @param bonusPerLvl
 * @param targetIsPlayerMod modifier applied to the bonus damage if the target is a player
 */
public record BonusMagicDamage(float bonusMagicDamage, float bonusPerLvl, float targetIsPlayerMod) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        float dmg = this.getBonusMagicDamage(stack);
        if (target instanceof PlayerEntity) {
            dmg *= this.targetIsPlayerMod;
        }
        target.damage(DamageSourceRegistry.create(attacker.getWorld(), DamageSourceRegistry.MAGIC_DAMAGE_BYPASS_COOLDOWN), dmg);
    }

    public float getBonusMagicDamage(ItemStack stack) {
        return this.bonusMagicDamage + this.bonusPerLvl * WeaponUtil.getUpgradeLevel(stack);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.spellblade").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.spellblade.1", this.getBonusMagicDamage(stack)).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.spellblade.2", String.format("%.1f", this.getBonusMagicDamage(stack) * this.targetIsPlayerMod)).formatted(Formatting.DARK_GRAY)
        );
    }
}
