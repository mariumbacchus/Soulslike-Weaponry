package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record Unbreakable(
        int minCooldown, int cooldown, int reducedCooldownPerLvl,
        int effectsDuration, int absorptionAmp, int resistanceAmp
) implements IKeybindAbility {

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (!this.isCoolingDown(player, stack)) {
            this.applyItemCooldown(stack, player, this.getScaledCooldownShield(stack));
            stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, this.effectsDuration, this.absorptionAmp));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, this.effectsDuration, this.resistanceAmp));
            world.playSound(null, player.getBlockPos(), SoundRegistry.NIGHTFALL_SHIELD_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        }
    }

    private int getScaledCooldownShield(ItemStack stack) {
        return Math.max(this.minCooldown, this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.unbreakable").formatted(Formatting.DARK_PURPLE),
                Text.translatable("tooltip.soulsweapons.unbreakable.description.1").formatted(Formatting.GRAY)
        );
    }
}
