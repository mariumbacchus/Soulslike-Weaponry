package net.soulsweaponry.registry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.effect.*;
import net.soulsweaponry.util.CustomDamageSource;

public class EffectRegistry {

    public static final StatusEffect HALLOWED_DRAGON_MIST = new HallowedDragonMist();
    public static final StatusEffect BLOODTHIRSTY = new DefaultStatusEffect(StatusEffectCategory.NEUTRAL, 0x630109);
    public static final StatusEffect POSTURE_BREAK = new PostureBreak();
    public static final StatusEffect LIFE_LEACH = new LifeLeach();
    public static final StatusEffect RETRIBUTION = new Retribution();
    public static final StatusEffect FEAR = new Fear();
    public static final StatusEffect DECAY = new Decay();
    public static final StatusEffect MAGIC_RESISTANCE = new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x80ffff);
    public static final StatusEffect MOON_HERALD = new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x03e8fc);
    public static final StatusEffect FREEZING = new Freezing();
    public static final StatusEffect DISABLE_HEAL = new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0xfc9d9d);
    public static final StatusEffect BLEED = new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0xba0c00);
    public static final StatusEffect CALCULATED_FALL = new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffff);

    public static final Potion WARDING = new Potion(new StatusEffectInstance(EffectRegistry.MAGIC_RESISTANCE, 4000));
    public static final Potion STRONG_WARDING = new Potion("warding", new StatusEffectInstance(EffectRegistry.MAGIC_RESISTANCE, 2000, 1));
    public static final Potion LONG_WARDING = new Potion("warding", new StatusEffectInstance(EffectRegistry.MAGIC_RESISTANCE, 8000));

    public static void init() {
        registerEffect(HALLOWED_DRAGON_MIST, "hallowed_dragon_mist");
        registerEffect(BLOODTHIRSTY, "bloodthirsty");
        registerEffect(POSTURE_BREAK, "posture_break");
        registerEffect(LIFE_LEACH, "life_leach");
        registerEffect(RETRIBUTION, "retribution");
        registerEffect(FEAR, "fear");
        registerEffect(DECAY, "decay");
        registerEffect(MAGIC_RESISTANCE, "magic_resistance");
        registerEffect(MOON_HERALD, "moon_herald");
        registerEffect(FREEZING, "freezing");
        registerEffect(DISABLE_HEAL, "disable_heal");
        registerEffect(BLEED, "bleed");
        registerEffect(CALCULATED_FALL, "calculated_fall");

        registerPotion(WARDING, "warding");
        registerPotion(STRONG_WARDING, "strong_warding");
        registerPotion(LONG_WARDING, "long_warding");

        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, BlockRegistry.HYDRANGEA.asItem(), WARDING);
        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, BlockRegistry.OLEANDER.asItem(), WARDING);
        BrewingRecipeRegistry.registerPotionRecipe(WARDING, Items.GLOWSTONE_DUST, STRONG_WARDING);
        BrewingRecipeRegistry.registerPotionRecipe(WARDING, Items.REDSTONE, LONG_WARDING);
    }

    public static <I extends StatusEffect> I registerEffect(I effect, String name) {
		return Registry.register(Registries.STATUS_EFFECT, new Identifier(SoulsWeaponry.ModId, name), effect);
	}

    private static Potion registerPotion(Potion potion, String name) {
        return Registry.register(Registries.POTION, name, potion);
    }

    static class DefaultStatusEffect extends StatusEffect {

        public DefaultStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
            super(statusEffectCategory, color);
        }

        @Override
        public boolean canApplyUpdateEffect(int duration, int amplifier) {
            if (this == EffectRegistry.BLEED) {
                int i = 15 >> amplifier;
                if (i > 0) {
                    return duration % i == 0;
                }
                return true;
            }
            if (this == EffectRegistry.BLOODTHIRSTY) {
                int k = 40 >> amplifier;
                if (k > 0) {
                    return duration % k == 0;
                } else {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public void applyUpdateEffect(LivingEntity entity, int amplifier) {
            if (this == EffectRegistry.BLEED && !(entity instanceof AbstractSkeletonEntity || entity instanceof SkeletonHorseEntity)) {
                entity.damage(CustomDamageSource.create(entity.getWorld(), CustomDamageSource.BLEED), 1f + amplifier);
            }
            if (this == EffectRegistry.BLOODTHIRSTY) {
                entity.damage(entity.getWorld().getDamageSources().wither(), 1f);
            }
        }
    }
}
