package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.soulsweaponry.items.abilities.IAbility;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;

public record BasicPostHitAbility(
        TriConsumer<ItemStack, LivingEntity, LivingEntity> postHit,
        List<Text> tooltip
) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        this.postHit.accept(stack, target, attacker);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return this.tooltip;
    }
}
