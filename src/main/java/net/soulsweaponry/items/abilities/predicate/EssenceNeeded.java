package net.soulsweaponry.items.abilities.predicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IHasEssence;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.NbtHelper;
import net.soulsweaponry.util.NbtIds;

import java.util.ArrayList;
import java.util.List;

public record EssenceNeeded(int maxEssence, boolean acceptWithMoonHerald) implements IHasEssence {

    @Override
    public boolean preventUsePredicate(ItemStack stack, PlayerEntity user) {
        return !this.hasMaxEssence(stack)
                && !user.isCreative()
                && this.acceptWithMoonHerald
                && !user.hasStatusEffect(EffectRegistry.MOON_HERALD);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        NbtHelper.putInt(stack, NbtIds.ESSENCE, 0);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        tooltip.add(Text.translatable("tooltip.soulsweapons.need_essence").formatted(Formatting.RED));
        tooltip.add(Text.translatable("tooltip.soulsweapons.need_essence.1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.need_essence.2").formatted(Formatting.GRAY));
        if (this.acceptWithMoonHerald) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.need_essence.3").formatted(Formatting.GRAY));
        }
        return tooltip;
    }

    @Override
    public int getMaxEssence() {
        return this.maxEssence;
    }
}
