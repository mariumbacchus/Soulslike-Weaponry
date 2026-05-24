package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record LunarHerald(
        int duration, float bonusDurationPerLvl, int amp, float bonusAmpPerLvl
) implements IKeybindAbility {

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (!player.hasStatusEffect(EffectRegistry.MOON_HERALD)) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            int duration = (int) (this.duration + this.bonusDurationPerLvl * lvl);
            int amp = (int) (this.amp + this.bonusAmpPerLvl * lvl);
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.MOON_HERALD, duration, amp));
            world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, 1f, 1f);
            stack.damage(1, player, p -> p.sendToolBreakStatus(hand == null ? player.getActiveHand() : hand));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.lunar_herald").formatted(Formatting.DARK_AQUA),
                Text.translatable("tooltip.soulsweapons.lunar_herald.1",
                        KeyBindRegistry.effectShootMoonlight.getBoundKeyLocalizedText()).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.lunar_herald.2").formatted(Formatting.GRAY)
        );
    }
}
