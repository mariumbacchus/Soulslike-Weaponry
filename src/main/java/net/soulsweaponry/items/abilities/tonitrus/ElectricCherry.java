package net.soulsweaponry.items.abilities.tonitrus;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.ChainLightning;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.List;

public record ElectricCherry(float chance, float baseDamage, float damagePerAmp, float baseRange, float rangePerAmp, int stunDuration) implements IAbility {

    public static final ElectricCherry EFFECT_INSTANCE = new ElectricCherry(
            ConfigConstructor.tonitrus_stormveil_effect_electric_cherry_chance,
            ConfigConstructor.tonitrus_stormveil_effect_electric_cherry_base_damage, ConfigConstructor.tonitrus_stormveil_effect_electric_cherry_damage_per_amp_level,
            ConfigConstructor.tonitrus_stormveil_effect_electric_cherry_base_radius, ConfigConstructor.tonitrus_stormveil_effect_electric_cherry_radius_per_amp_level,
            (int) ConfigConstructor.tonitrus_stormveil_effect_electric_cherry_heavy_slow_duration
    );

    public void trigger(LivingEntity user, LivingEntity attacker, float amp) {
        if (user.getRandom().nextFloat() < this.chance) {
            float radius = this.baseRange + this.rangePerAmp * (amp + 1);
            float damage = this.baseDamage + this.damagePerAmp * (amp + 1);
            ChainLightning.trigger(attacker.getWorld(), attacker, user, damage, radius, (target) -> target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, this.stunDuration, 20)));

            user.getWorld().playSound(null, user.getBlockPos(), SoundRegistry.STORMVEIL_THORNS, SoundCategory.PLAYERS, 1f, 1f);
            if (!user.getWorld().isClient) {
                for (int i = 0; i < 50; i++) {
                    ParticleHandler.singleParticle(user.getWorld(), ParticleRegistry.SOUL_SPARK, user.getParticleX(1D), user.getBodyY(0.5) + user.getRandom().nextDouble() * 2 - 1D, user.getParticleZ(1D),
                            user.getRandom().nextGaussian() / 10f, user.getRandom().nextGaussian() / 10f, user.getRandom().nextGaussian() / 10f);
                }
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.electric_cherry").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.electric_cherry.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.electric_cherry.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.electric_cherry.3").formatted(Formatting.GRAY)
        );
    }
}
