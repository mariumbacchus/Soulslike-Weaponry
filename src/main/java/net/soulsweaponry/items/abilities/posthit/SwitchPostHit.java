package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.entitydata.FrostData;
import net.soulsweaponry.items.abilities.ChainLightning;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.ModTags;
import net.soulsweaponry.util.NbtHelper;
import net.soulsweaponry.util.NbtIds;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;
import java.util.Random;

public final class SwitchPostHit implements IAbility {

    private final float bonusAgainstEffect;
    private final int baseBleed, bleedDuration, bleedAmp;
    private final int poisonDuration, poisonAmp;
    private final float baseCLRadius, clRadiusPerLvl, baseCLDamage, clDamagePerLvl;
    private final int witherDuration, witherAmp;
    private final int baseFreeze, freezeDuration, freezeAmp;
    private final int fireTicks;
    private final int crippleDuration;
    private final int slowAmp, weakAmp, fatigueAmp;
    private final int decayDuration, decayAmp;
    private final int blightDuration, blightAmp;
    private final BonusMagicDamage magicDamageAbility;

    private static final Random RAND = new Random();

    private SwitchPostHit(Builder b) {
        this.bonusAgainstEffect = b.bonusAgainstEffect;
        this.baseBleed = b.baseBleed;
        this.bleedDuration = b.bleedDuration;
        this.bleedAmp = b.bleedAmp;
        this.poisonDuration = b.poisonDuration;
        this.poisonAmp = b.poisonAmp;
        this.baseCLRadius = b.baseCLRadius;
        this.clRadiusPerLvl = b.clRadiusPerLvl;
        this.baseCLDamage = b.baseCLDamage;
        this.clDamagePerLvl = b.clDamagePerLvl;
        this.witherDuration = b.witherDuration;
        this.witherAmp = b.witherAmp;
        this.baseFreeze = b.baseFreeze;
        this.freezeDuration = b.freezeDuration;
        this.freezeAmp = b.freezeAmp;
        this.fireTicks = b.fireTicks;
        this.crippleDuration = b.crippleDuration;
        this.slowAmp = b.slowAmp;
        this.weakAmp = b.weakAmp;
        this.fatigueAmp = b.fatigueAmp;
        this.decayDuration = b.decayDuration;
        this.decayAmp = b.decayAmp;
        this.blightDuration = b.blightDuration;
        this.blightAmp = b.blightAmp;
        this.magicDamageAbility = new BonusMagicDamage(b.bonusMagicDamage, b.bonusMagicDmgPerLvl, b.magicDamageTargetIsPlayerMod);
    }

    @Override
    public void onMainHandEquip(PlayerEntity player, ItemStack stack) {
        this.selectRandomEffect(stack);
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.POTENCY, 140, 0));
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int level = WeaponUtil.getUpgradeLevel(stack);
        switch (this.getPostHitEffect(stack)) {
            case BLEED -> {
                BleedData.addBleed(target, this.baseBleed);
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEED, this.bleedDuration, this.bleedAmp));
            }
            case POISON -> {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, this.poisonDuration, this.poisonAmp));
            }
            case CHAIN_LIGHTNING -> {
                float radius = this.baseCLRadius + clRadiusPerLvl * level;
                float damage = this.baseCLDamage + clDamagePerLvl * level;
                ChainLightning.trigger(attacker.getWorld(), target, attacker, damage, radius);
            }
            case WITHER -> {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, this.witherDuration, this.witherAmp));
            }
            case FREEZE -> {
                FrostData.setFrostSource(target, attacker);
                FrostData.addFrost(target, this.baseFreeze);
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING, this.freezeDuration, this.freezeAmp));
            }
            case FIRE -> {
                target.setOnFireFor((int) Math.floor(this.fireTicks / 20f));
            }
            case CRIPPLE -> {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, this.crippleDuration, this.slowAmp));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, this.crippleDuration, this.fatigueAmp));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, this.crippleDuration, this.weakAmp));
            }
            case DECAY -> {
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.DECAY, this.decayDuration, this.decayAmp));
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLIGHT, this.blightDuration, this.blightAmp));
            }
            case MAGIC_DAMAGE -> {
                this.magicDamageAbility.postHit(stack, target, attacker);
            }
            default -> {}
        }
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        float res = 0;
        if (target instanceof LivingEntity living) {
            if (living.isOnFire() || living.getStatusEffects().stream().anyMatch(
                    p -> Registries.STATUS_EFFECT.getEntry(p.getEffectType())
                            .isIn(ModTags.Effects.NIGHTLORD_ATTACK_BOOST_GAINED_FROM))) {
                res += this.bonusAgainstEffect;
            }
        }
        return res;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.nightlord").formatted(Formatting.BLUE),
                Text.translatable("tooltip.soulsweapons.nightlord.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.nightlord.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.nightlord.3").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.nightlord.4").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.nightlord.5").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.nightlord.6").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.nightlord.7", this.formatPostHitEffect(this.getPostHitEffect(stack))).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.nightlord.8").formatted(Formatting.GRAY)
        );
    }

    private void selectRandomEffect(ItemStack stack) {
        int rand = RAND.nextInt(1, PostHitEffect.values().length);
        NbtHelper.putInt(stack, NbtIds.POST_HIT_EFFECT_ID, rand);
    }

    private PostHitEffect getPostHitEffect(ItemStack stack) {
        return PostHitEffect.values()[NbtHelper.getInt(stack, NbtIds.POST_HIT_EFFECT_ID, 0)];
    }

    private Text formatPostHitEffect(PostHitEffect effect) {
        return Text.translatable("tooltip.soulsweapons.nightlord." + effect.toString().toLowerCase());
    }

    enum PostHitEffect {
        EMPTY(0x8a8a8a), BLEED(0x820505), POISON(0x00e31e),
        CHAIN_LIGHTNING(0x47edff), WITHER(0x1c1c1c), FREEZE(0xb0d5ff),
        FIRE(0xff9100), CRIPPLE(0x8f71b0), DECAY(0x8800ff), MAGIC_DAMAGE(0x40ffd9);

        private final int color;
        PostHitEffect(int color) { this.color = color; }
        public int color() { return color; }
    }

    /**
     * NB! Need to register the texture being changed based on the effect by calling this with
     * ColorProviderRegistry.ITEM.register() and applying necessary changed to the model
     * (having 2 texture layers, see WeaponRegistry.NIGHTLORDS_SWORD as example).
     */
    public static int getModelColor(ItemStack stack) {
        return IHasAbilities.getAbility(stack, SwitchPostHit.class)
                .map(a -> a.getPostHitEffect(stack).color())
                .orElse(PostHitEffect.EMPTY.color());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private float bonusAgainstEffect;
        private int baseBleed, bleedDuration, bleedAmp;
        private int poisonDuration, poisonAmp;
        private float baseCLRadius, clRadiusPerLvl, baseCLDamage, clDamagePerLvl;
        private int witherDuration, witherAmp;
        private int baseFreeze, freezeDuration, freezeAmp;
        private int fireTicks;
        private int crippleDuration;
        private int slowAmp, weakAmp, fatigueAmp;
        private int decayDuration, decayAmp;
        private int blightDuration, blightAmp;
        private float bonusMagicDamage, bonusMagicDmgPerLvl, magicDamageTargetIsPlayerMod;

        public Builder bonusAgainstEffect(float v) {
            this.bonusAgainstEffect = v; return this;
        }

        public Builder bleed(int base, int duration, int amp) {
            this.baseBleed = base;
            this.bleedDuration = duration;
            this.bleedAmp = amp;
            return this;
        }

        public Builder poison(int duration, int amp) {
            this.poisonDuration = duration;
            this.poisonAmp = amp;
            return this;
        }

        public Builder chainLightning(float baseRadius, float radiusPerLvl, float baseDamage, float damagePerLvl) {
            this.baseCLRadius = baseRadius;
            this.clRadiusPerLvl = radiusPerLvl;
            this.baseCLDamage = baseDamage;
            this.clDamagePerLvl = damagePerLvl;
            return this;
        }

        public Builder wither(int duration, int amp) {
            this.witherDuration = duration;
            this.witherAmp = amp;
            return this;
        }

        public Builder freeze(int baseFreeze, int duration, int amp) {
            this.baseFreeze = baseFreeze;
            this.freezeDuration = duration;
            this.freezeAmp = amp;
            return this;
        }

        public Builder fireTicks(int ticks) {
            this.fireTicks = ticks;
            return this;
        }

        public Builder crippleDuration(int duration) {
            this.crippleDuration = duration;
            return this;
        }

        public Builder debuffAmps(int slowAmp, int weakAmp, int fatigueAmp) {
            this.slowAmp = slowAmp;
            this.weakAmp = weakAmp;
            this.fatigueAmp = fatigueAmp;
            return this;
        }

        public Builder decay(int duration, int amp) {
            this.decayDuration = duration;
            this.decayAmp = amp;
            return this;
        }

        public Builder blight(int duration, int amp) {
            this.blightDuration = duration;
            this.blightAmp = amp;
            return this;
        }

        public Builder magicDamage(float bonusMagicDamage, float bonusMagicDmgPerLvl, float magicDamageTargetIsPlayerMod) {
            this.bonusMagicDamage = bonusMagicDamage;
            this.bonusMagicDmgPerLvl = bonusMagicDmgPerLvl;
            this.magicDamageTargetIsPlayerMod = magicDamageTargetIsPlayerMod;
            return this;
        }

        public SwitchPostHit build() {
            return new SwitchPostHit(this);
        }
    }
}