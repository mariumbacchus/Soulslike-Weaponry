package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Use to remove all harmful effects.
 */
public class Cleanse implements IAbility {

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        List<StatusEffect> effects = new ArrayList<>();
        boolean shouldDamage = false;
        for (StatusEffectInstance effectInstance : user.getStatusEffects()) {
            if (effectInstance.getEffectType().getCategory().equals(StatusEffectCategory.HARMFUL)) {
                effects.add(effectInstance.getEffectType());
                shouldDamage = true;
            }
        }
        effects.forEach(user::removeStatusEffect);
        if (shouldDamage) {
            world.playSound(user, user.getBlockPos(), SoundRegistry.RESTORE_EVENT, SoundCategory.PLAYERS, 1f, 1f);
            stack.damage(1, user, p -> p.sendToolBreakStatus(hand));
            return TypedActionResult.success(user.getStackInHand(hand));
        } else {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.cleanse").formatted(Formatting.LIGHT_PURPLE),
                Text.translatable("tooltip.soulsweapons.cleanse.1").formatted(Formatting.GRAY)
        );
    }
}
