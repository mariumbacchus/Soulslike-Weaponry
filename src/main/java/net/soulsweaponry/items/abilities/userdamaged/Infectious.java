package net.soulsweaponry.items.abilities.userdamaged;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * When having the {@link EffectRegistry#LIFE_LEACH}, deal damage back to mobs attacking the user
 * and apply other effects.
 */
public record Infectious(
        float damage, float bonusDamagePerLvl, float knockback, float bonusKnockbackPerLvl,
        List<RegistryEntry<StatusEffect>> effectsOnTarget, int effectsDuration, float effectsDurationPerLvl,
        int effectsAmp, float effectsAmpPerLvl, int fireSeconds, int fireSecondsPerLvl
) implements IAbility {

    @Override
    public boolean onUserDamaged(DamageSource source, float amount, ItemStack stack, LivingEntity player) {
        if (source.getAttacker() instanceof LivingEntity attacker && player.hasStatusEffect(EffectRegistry.LIFE_LEACH)) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            int duration = (int) (this.effectsDuration + this.effectsDurationPerLvl * lvl);
            int amp = (int) (this.effectsAmp + this.effectsAmpPerLvl * lvl);
            int fire = this.fireSeconds + this.fireSecondsPerLvl * lvl;
            double x = player.getX() - attacker.getX();
            double z = player.getZ() - attacker.getZ();
            attacker.damage(player.getDamageSources().wither(), this.damage + this.bonusDamagePerLvl * lvl);
            attacker.takeKnockback(this.knockback + this.bonusKnockbackPerLvl * lvl, x, z);
            this.effectsOnTarget.forEach(effect -> attacker.addStatusEffect(new StatusEffectInstance(effect, duration, amp)));
            if (fire > 0) {
                attacker.setOnFireFor(fire);
            }
            if (!player.getWorld().isClient) {
                for (int i = 0; i < 50; i++) {
                    ParticleHandler.singleParticle(player.getWorld(), ParticleRegistry.BLACK_FLAME,
                            player.getParticleX(1D), player.getBodyY(0.5) + player.getRandom().nextDouble() * 2 - 1D, player.getParticleZ(1D),
                            player.getRandom().nextGaussian() * 0.1f, player.getRandom().nextGaussian() * 0.1f, player.getRandom().nextGaussian() * 0.1f);
                }
            }
            player.playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1f, 1f);
        }
        return true;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        int fire = this.fireSeconds + this.fireSecondsPerLvl * WeaponUtil.getUpgradeLevel(stack);
        List<Text> tooltip = new ArrayList<>();
        tooltip.add(Text.translatable("tooltip.soulsweapons.infectious").formatted(Formatting.DARK_RED));
        tooltip.add(Text.translatable("tooltip.soulsweapons.infectious.1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.infectious.2").formatted(Formatting.GRAY));
        if (fire > 0) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.infectious.3").formatted(Formatting.GRAY));
        }
        return tooltip;
    }
}
