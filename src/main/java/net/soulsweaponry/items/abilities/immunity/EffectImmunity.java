package net.soulsweaponry.items.abilities.immunity;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.*;

/**
 * Grants immunity to the effects while also triggering the consumer.
 * An example usage is granting immunity to Decay and granting the user Regeneration afterward.
 */
public class EffectImmunity implements IAbility {

    private final List<StatusEffectCategory> categoriesImmuneTo;
    private final Set<RegistryEntry<StatusEffect>> statusEffectsImmuneTo;
    private final TriConsumer<LivingEntity, StatusEffectInstance, ItemStack> onEffectDeclined;

    public EffectImmunity(List<StatusEffectCategory> categoriesImmuneTo, Set<RegistryEntry<StatusEffect>> statusEffectsImmuneTo, TriConsumer<LivingEntity, StatusEffectInstance, ItemStack> onEffectDeclined) {
        this.categoriesImmuneTo = categoriesImmuneTo;
        this.statusEffectsImmuneTo = statusEffectsImmuneTo;
        this.onEffectDeclined = onEffectDeclined;
    }

    public EffectImmunity(Set<RegistryEntry<StatusEffect>> statusEffectsImmuneTo, TriConsumer<LivingEntity, StatusEffectInstance, ItemStack> onEffectDeclined) {
        this(List.of(), statusEffectsImmuneTo, onEffectDeclined);
    }

    public EffectImmunity(Set<RegistryEntry<StatusEffect>> statusEffectsImmuneTo) {
        this(List.of(), statusEffectsImmuneTo, (entity, effect, stack) -> {});
    }

    public EffectImmunity(List<StatusEffectCategory> categoriesImmuneTo) {
        this(categoriesImmuneTo, Set.of(), (entity, effect, stack) -> {});
    }

    @Override
    public Set<RegistryEntry<StatusEffect>> getStatusEffectsImmuneTo() {
        if (this.categoriesImmuneTo.isEmpty()) {
            return this.statusEffectsImmuneTo;
        }
        // Immunity to all effects with the categories inside categoriesImmuneTo
        Set<RegistryEntry<StatusEffect>> out = new LinkedHashSet<>(this.statusEffectsImmuneTo);
        Registries.STATUS_EFFECT.streamEntries()
                .filter(entry -> this.categoriesImmuneTo.contains(entry.value().getCategory()))
                .forEach(out::add);
        return out;
    }

    @Override
    public void onMainHandEquip(PlayerEntity player, ItemStack stack) {
        // onEquipStack already handles removing effects and then calling onStatusEffectDeclined
        this.onEquipStack(player, EquipmentSlot.MAINHAND, stack, stack);
    }

    @Override
    public void onStatusEffectDeclined(LivingEntity entity, StatusEffectInstance declinedEffectInstance, ItemStack stack) {
        this.onEffectDeclined.accept(entity, declinedEffectInstance, stack);
    }

    @Override
    public void onEquipStack(LivingEntity entity, EquipmentSlot slot, ItemStack oldStack, ItemStack newStack) {
        // Catch the effect with the highest amp so it can be passed to onStatusEffectDeclined
        List<StatusEffectInstance> effects = new ArrayList<>();
        for (StatusEffectInstance effectInstance : entity.getStatusEffects()) {
            if (this.getStatusEffectsImmuneTo().contains(effectInstance.getEffectType())) {
                effects.add(effectInstance);
            }
        }
        StatusEffectInstance highest = effects.stream()
                .max(Comparator.comparingInt(StatusEffectInstance::getAmplifier))
                .orElse(null);
        effects.forEach(effectInstance -> entity.removeStatusEffect(effectInstance.getEffectType()));
        if (highest != null) {
            this.onStatusEffectDeclined(entity, highest, newStack);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.cleansing_artifact").formatted(Formatting.DARK_RED),
                Text.translatable("tooltip.soulsweapons.cleansing_artifact.1", this.getLocalizedEffectNames(this.statusEffectsImmuneTo)).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.cleansing_artifact.2").formatted(Formatting.GRAY)
        );
    }
}
