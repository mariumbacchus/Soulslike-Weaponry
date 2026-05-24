package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record LifeLeach(
        int duration, float bonusDurationPerLvl, int amp, float bonusAmpPerLvl,
        int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IKeybindAbility {

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (!this.isCoolingDown(player, stack)) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            int duration = (int) (this.duration + this.bonusDurationPerLvl * lvl);
            int amp = (int) (this.amp + this.bonusAmpPerLvl * lvl);
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.LIFE_LEACH, duration, amp, false, false));
            this.applyItemCooldown(stack, player, Math.max(this.minCooldown, this.cooldown - lvl * this.reducedCooldownPerLvl));
            world.playSound(null, player.getBlockPos(), SoundRegistry.DEMON_BOSS_IDLE_EVENT, SoundCategory.PLAYERS, 0.75f, 1f);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.unceasing").formatted(Formatting.DARK_PURPLE),
                Text.translatable("tooltip.soulsweapons.unceasing.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.unceasing.2").formatted(Formatting.GRAY)
        );
    }
}
