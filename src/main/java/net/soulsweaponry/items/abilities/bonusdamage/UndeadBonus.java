package net.soulsweaponry.items.abilities.bonusdamage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record UndeadBonus(float baseBonusDamage, float bonusPerLvl) implements IAbility {

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if (target.getType().isIn(EntityTypeTags.UNDEAD)) {
            if (damageSource.getAttacker() instanceof PlayerEntity player) {
                ItemStack stack = player.getMainHandStack();
                return this.getTotalUndeadBonus(stack);
            }
        }
        return 0;
    }

    public float getTotalUndeadBonus(ItemStack stack) {
        return this.baseBonusDamage + this.bonusPerLvl * WeaponUtil.getUpgradeLevel(stack);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        float amount = this.getTotalUndeadBonus(stack);
        return List.of(
                Text.translatable("tooltip.soulsweapons.righteous").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.righteous.description.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.righteous.description.2", String.format("%.1f", amount)).formatted(Formatting.DARK_GRAY)
        );
    }
}
