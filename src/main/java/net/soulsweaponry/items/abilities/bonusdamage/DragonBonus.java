package net.soulsweaponry.items.abilities.bonusdamage;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.util.ModTags;

import java.util.List;

public class DragonBonus extends EntityTagBonus {

    public DragonBonus(float baseBonusDamage, float bonusDamagePerLvl) {
        super(ModTags.Entities.DRAGONS, baseBonusDamage, bonusDamagePerLvl);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        float amount = this.getTotalBonus(stack);
        return List.of(
                Text.translatable("tooltip.soulsweapons.dragons_bane").formatted(Formatting.DARK_PURPLE),
                Text.translatable("tooltip.soulsweapons.dragons_bane.1", String.format("%.1f", amount)).formatted(Formatting.GRAY)
        );
    }
}
