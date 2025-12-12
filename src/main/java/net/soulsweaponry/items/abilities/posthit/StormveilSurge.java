package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.ChainLightning;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

/**
 * Triggers Chain Lightning post hit only if the user has the {@link EffectRegistry#STORMVEIL} effect. Radius and
 * damage to it is based on the amplifier of the effect.
 */
public record StormveilSurge(float rangePerAmp, float damagePerAmp, int minCooldown, int cooldown, int reducedCooldownPerLvl) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.hasStatusEffect(EffectRegistry.STORMVEIL)) {
            StatusEffectInstance instance = attacker.getStatusEffect(EffectRegistry.STORMVEIL);
            int amp = instance.getAmplifier();
            float radius = this.rangePerAmp * (amp + 1);
            float damage = this.damagePerAmp * (amp + 1);
            ChainLightning.trigger(attacker.getWorld(), target, attacker, damage, radius);
            Boolean empowered = stack.get(ComponentRegistry.STORMVEIL_SURGE_EMPOWERED);
            if (empowered != null && empowered) {
                LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, attacker.getWorld());
                lightningEntity.setPos(target.getX(), target.getY(), target.getZ());
                attacker.getWorld().spawnEntity(lightningEntity);
                stack.set(ComponentRegistry.STORMVEIL_SURGE_EMPOWERED, false);
                attacker.removeStatusEffect(EffectRegistry.STORMVEIL);
                // Reduce Stormveil to 20 ticks (originally remove, but want to keep immunity to lightning for a second still)
                attacker.addStatusEffect(new StatusEffectInstance(EffectRegistry.STORMVEIL, 20));
                if (attacker instanceof PlayerEntity player) {
                    this.applyEffectCooldown(player, Math.max(this.minCooldown, this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl));
                }
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.stormveil_surge").formatted(Formatting.DARK_AQUA),
                Text.translatable("tooltip.soulsweapons.stormveil_surge.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.stormveil_surge.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.stormveil_surge.3").formatted(Formatting.GRAY)
        );
    }
}
