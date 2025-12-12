package net.soulsweaponry.items.abilities.bonusdamage;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class UndeadBonus extends EntityTagBonus {

    public UndeadBonus(float baseBonusDamage, float bonusDamagePerLvl) {
        super(EntityTypeTags.UNDEAD, baseBonusDamage, bonusDamagePerLvl);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        float amount = this.getTotalBonus(stack);
        return List.of(
                Text.translatable("tooltip.soulsweapons.righteous").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.righteous.description.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.righteous.description.2", String.format("%.1f", amount)).formatted(Formatting.DARK_GRAY)
        );
    }
}
