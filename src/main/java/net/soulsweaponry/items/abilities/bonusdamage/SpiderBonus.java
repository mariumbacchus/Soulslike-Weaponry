package net.soulsweaponry.items.abilities.bonusdamage;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class SpiderBonus extends EntityTagBonus {

    public SpiderBonus(float baseBonusDamage, float bonusDamagePerLvl) {
        super(EntityTypeTags.ARTHROPOD, baseBonusDamage, bonusDamagePerLvl);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.spiders_bane").formatted(Formatting.WHITE),
                Text.translatable("tooltip.soulsweapons.spiders_bane.1", this.getTotalBonus(stack)).formatted(Formatting.GRAY)
        );
    }
}
