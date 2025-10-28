package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IHasEssence;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;

public record GivesEssence(int essencePostHit, int bonusPerLvl, int maxEssence) implements IHasEssence {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        this.addEssence(stack, this.essenceAdded(stack));
    }

    public int essenceAdded(ItemStack stack) {
        return this.essencePostHit + this.bonusPerLvl * WeaponUtil.getUpgradeLevel(stack);
    }

    public void addEssence(ItemStack stack, int amount) {
        Integer essence = stack.get(ComponentRegistry.ESSENCE);
        if (essence != null) {
            int newEssence = IHasEssence.getEssence(stack) + amount;
            stack.set(ComponentRegistry.ESSENCE, Math.min(newEssence, this.maxEssence));
        } else {
            stack.set(ComponentRegistry.ESSENCE, 0);
        }
    }

    public int essenceProgressPercent(ItemStack stack) {
        return (int) (IHasEssence.getEssence(stack) / (float) this.maxEssence * 100);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_essence").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_essence.1", this.essenceAdded(stack)).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_essence.2", this.essenceProgressPercent(stack) + "%").formatted(Formatting.GRAY));
        return tooltip;
    }

    @Override
    public int getMaxEssence() {
        return this.maxEssence;
    }
}
