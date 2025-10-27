package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class DawnbreakerExplosion implements IAbility {

    public final boolean affectAllEntities;
    public final int baseAmpPostHit;
    public final float bonusAmpPerLvl;
    public final double detonationChanceAddition;
    public final float explosionRange;
    public final float baseFireSeconds;
    public final float bonusSecondsPerLvl;
    public final float explosionDamage;
    public final float bonusDamagePerLvl;
    public final int fearEffectDuration;

    /**
     * Apply {@link EffectRegistry#RETRIBUTION} effect post hit. Each amp
     * increases the chance for the Dawnbreaker explosion, mainly targeting
     * undead mobs, unless boolean allows it.
     *
     * @param affectAllEntities whether the explosion should target all entities or only undead
     * @param baseAmpPostHit base Retribution amp
     * @param bonusAmpPerLvl bonus Retribution amp
     * @param detonationChanceAddition bonus chance for detonation, an addition to base chance
     * @param explosionRange explosion range
     * @param baseFireSeconds base fire seconds applied to mobs hit by the explosion
     * @param bonusSecondsPerLvl bonus fire seconds applied to mobs hit by the explosion
     * @param explosionDamage damage
     * @param bonusDamagePerLvl bonus damage per level
     * @param fearEffectDuration fear effect is applied to mobs hit by the explosion
     */
    public DawnbreakerExplosion(
            boolean affectAllEntities,
            int baseAmpPostHit,
            float bonusAmpPerLvl,
            double detonationChanceAddition,
            float explosionRange,
            float baseFireSeconds,
            float bonusSecondsPerLvl,
            float explosionDamage,
            float bonusDamagePerLvl,
            int fearEffectDuration
    ) {
        this.affectAllEntities = affectAllEntities;
        this.baseAmpPostHit = baseAmpPostHit;
        this.bonusAmpPerLvl = bonusAmpPerLvl;
        this.detonationChanceAddition = detonationChanceAddition;
        this.explosionRange = explosionRange;
        this.baseFireSeconds = baseFireSeconds;
        this.bonusSecondsPerLvl = bonusSecondsPerLvl;
        this.explosionDamage = explosionDamage;
        this.bonusDamagePerLvl = bonusDamagePerLvl;
        this.fearEffectDuration = fearEffectDuration;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int addedAmp = (int) (this.baseAmpPostHit + this.bonusAmpPerLvl * WeaponUtil.getUpgradeLevel(stack));
        if (this.shouldAffectEntity(target)) {
            if (target.hasStatusEffect(EffectRegistry.RETRIBUTION)) {
                int amplifier = target.getStatusEffect(EffectRegistry.RETRIBUTION).getAmplifier();
                amplifier += addedAmp;
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.RETRIBUTION, 80, amplifier));
            } else {
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.RETRIBUTION, 80, addedAmp));
            }
        }
    }

    @Override
    public void onTargetDeath(DamageSource damageSource, ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.hasStatusEffect(EffectRegistry.RETRIBUTION)) {
            double chance = this.detonationChanceAddition
                    + 1 - (Math.pow(.75, target.getStatusEffect(EffectRegistry.RETRIBUTION).getAmplifier()));
            double random = target.getRandom().nextDouble();
            if (random < chance) {
                this.dawnbreakerEvent(attacker.getWorld(), target, attacker, stack);
            }
        }
    }

    public boolean shouldAffectEntity(LivingEntity target) {
        return target.hasInvertedHealingAndHarm() || this.affectAllEntities;
    }

    public void dawnbreakerEvent(World world, LivingEntity target, LivingEntity attacker, ItemStack stack) {
        if (!world.isClient) {
            ParticleHandler.particleSphere(world, 1000, target.getX(), target.getEyeY() - .25f, target.getZ(), ParticleTypes.FLAME, 1f);
            ParticleHandler.particleOutburstMap(world, 200, target.getX(), target.getY(), target.getZ(), ParticleEvents.DAWNBREAKER_MAP, 1f);
        }
        world.playSound(null, target.getBlockPos(), SoundRegistry.DAWNBREAKER_EVENT, SoundCategory.HOSTILE, 2f, 1f);
        Box aoe = target.getBoundingBox().expand(this.explosionRange);
        List<Entity> entities = world.getOtherEntities(target, aoe);
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        int fireSeconds = (int) (this.baseFireSeconds + this.bonusSecondsPerLvl * lvl);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity targetHit && this.shouldAffectEntity(targetHit)) {
                if (!targetHit.equals(attacker)) {
                    targetHit.setOnFireFor(fireSeconds);
                    targetHit.damage(world.getDamageSources().explosion(null, attacker),
                            this.explosionDamage + this.bonusDamagePerLvl * lvl);
                    targetHit.addStatusEffect(new StatusEffectInstance(EffectRegistry.FEAR, this.fearEffectDuration, 0));
                }
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.meridias_retribution").formatted(Formatting.DARK_PURPLE),
                Text.translatable("tooltip.soulsweapons.meridias_retribution.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.meridias_retribution.2").formatted(Formatting.GRAY)
        );
    }
}
