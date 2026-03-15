package net.soulsweaponry.items.abilities.bonusdamage;

import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.util.ModTags;

import java.util.List;

public class SpiderBonus extends EntityTagBonus {

    public SpiderBonus(float baseBonusDamage, float bonusDamagePerLvl) {
        super(ModTags.Entities.ARTHROPOD, EntityGroup.ARTHROPOD, baseBonusDamage, bonusDamagePerLvl);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.spiders_bane").formatted(Formatting.WHITE),
                Text.translatable("tooltip.soulsweapons.spiders_bane.1", this.getTotalBonus(stack)).formatted(Formatting.GRAY)
        );
    }
}
