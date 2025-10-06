package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;

public record MoltenEdge(float chance, int fireSeconds) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.getRandom().nextFloat() < this.chance) {
            target.setOnFireFor(this.fireSeconds);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.molten_edge").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.molten_edge.1").formatted(Formatting.GRAY)
        );
    }
}
