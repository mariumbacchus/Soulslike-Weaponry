package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.posthit.StormveilSurge;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

/**
 * Applies {@link EffectRegistry#STORMVEIL} effect to the user, and using again empowers it with the {@link ComponentRegistry#STORMVEIL_SURGE_EMPOWERED} component.
 * A weapon using this ability should also use or implement {@link StormveilSurge} to gain the post hit effects while having
 * the Stormveil effect granted by this ability.
 */
public record Stormveil(int stormveilEffectBaseAmp, float bonusAmpPerLevelCeiled, int stormveilEffectDuration) implements IAbility {

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (!user.hasStatusEffect(EffectRegistry.COOLDOWN)) {
            if (user.hasStatusEffect(EffectRegistry.STORMVEIL)) {
                Boolean empowered = stack.get(ComponentRegistry.STORMVEIL_SURGE_EMPOWERED);
                if (empowered != null && !empowered) {
                    stack.set(ComponentRegistry.STORMVEIL_SURGE_EMPOWERED, true);
                    return TypedActionResult.consume(stack);
                }
                stack.set(ComponentRegistry.STORMVEIL_SURGE_EMPOWERED, true);
                return TypedActionResult.fail(stack);
            } else {
                stack.set(ComponentRegistry.STORMVEIL_SURGE_EMPOWERED, false);
                stack.damage(1, user, WeaponUtil.getActiveHandSlot(user));
                int amp = MathHelper.ceil( this.stormveilEffectBaseAmp + WeaponUtil.getUpgradeLevel(stack) * this.bonusAmpPerLevelCeiled);
                user.addStatusEffect(new StatusEffectInstance(EffectRegistry.STORMVEIL, this.stormveilEffectDuration, amp));
                world.playSound(null, user.getBlockPos(), SoundRegistry.STORMVEIL_TRIGGER, SoundCategory.PLAYERS, 1f, 1f);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.stormveil").formatted(Formatting.BLUE),
                Text.translatable("tooltip.soulsweapons.stormveil.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.stormveil.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.stormveil.3").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.stormveil.4").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.stormveil.5").formatted(Formatting.GRAY)
        );
    }
}
