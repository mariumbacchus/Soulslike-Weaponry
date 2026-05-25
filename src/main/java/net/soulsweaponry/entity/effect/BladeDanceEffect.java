package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.posthit.BladeDance;
import net.soulsweaponry.registry.EffectRegistry;

import java.util.Optional;

public class BladeDanceEffect extends StatusEffect {

    public BladeDanceEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xa8ffff);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        ItemStack mainHandStack = entity.getMainHandStack();
        if (mainHandStack.getItem() instanceof IHasAbilities hasAbilities) {
            Optional<BladeDance> op = hasAbilities.findAbility(BladeDance.class);
            if (op.isPresent()) {
                BladeDance bladeDance = op.get();
                bladeDance.updateBladeDanceItem(mainHandStack, amplifier);
                if (amplifier >= bladeDance.maxBladeDanceAmp() && !entity.hasStatusEffect(EffectRegistry.COOLDOWN.get())) {
                    bladeDance.onMaxStacksEffects().accept(entity, mainHandStack);
                    entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.COOLDOWN.get(),
                            bladeDance.totalMaxStacksEffectCooldown(mainHandStack), 0));
                }
            }
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        ItemStack mainHandStack = entity.getMainHandStack();
        if (mainHandStack.getItem() instanceof IHasAbilities hasAbilities) {
            Optional<BladeDance> op = hasAbilities.findAbility(BladeDance.class);
            if (op.isPresent()) {
                BladeDance bladeDance = op.get();
                bladeDance.resetBladeDanceItem(mainHandStack);
            }
        }
    }
}