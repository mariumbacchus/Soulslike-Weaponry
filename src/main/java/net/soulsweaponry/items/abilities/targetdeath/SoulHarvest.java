package net.soulsweaponry.items.abilities.targetdeath;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.registry.ComponentRegistry;

import java.util.List;
import java.util.Optional;

public class SoulHarvest implements ISoulHarvest {

    @Override
    public void onTargetDeath(DamageSource damageSource, ItemStack stack, LivingEntity target, LivingEntity attacker) {
        this.handleKill(target, stack);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        String kills = String.valueOf(Optional.ofNullable(stack.get(ComponentRegistry.SOULS_HARVESTED)).orElse(0));
        return List.of(
                Text.translatable("tooltip.soulsweapons.soul_trap").formatted(Formatting.DARK_PURPLE),
                Text.translatable("tooltip.soulsweapons.soul_trap_description").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.soul_trap_kills", kills).formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.collect_1").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC),
                Text.translatable("tooltip.soulsweapons.collect_2").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC),
                Text.translatable("tooltip.soulsweapons.collect_3").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC)
        );
    }
}
