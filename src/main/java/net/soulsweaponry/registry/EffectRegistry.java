package net.soulsweaponry.registry;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.effect.*;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class EffectRegistry {

    public static final DeferredRegister<StatusEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SoulsWeaponry.ModId);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, SoulsWeaponry.ModId);

    public static final RegistryObject<StatusEffect> DECAY = EFFECTS.register("decay", Decay::new);
    public static final RegistryObject<StatusEffect> FEAR = EFFECTS.register("fear", Fear::new);
    public static final RegistryObject<StatusEffect> FREEZING = EFFECTS.register("freezing", Freezing::new);
    public static final RegistryObject<StatusEffect> HALLOWED_DRAGON_MIST = EFFECTS.register("hallowed_dragon_mist", HallowedDragonMist::new);
    public static final RegistryObject<StatusEffect> LIFE_LEACH = EFFECTS.register("life_leach", LifeLeach::new);
    public static final RegistryObject<StatusEffect> POSTURE_BREAK = EFFECTS.register("posture_break", PostureBreak::new);
    public static final RegistryObject<StatusEffect> RETRIBUTION = EFFECTS.register("retribution", () -> new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0xc76700));
    public static final RegistryObject<StatusEffect> VEIL_OF_FIRE = EFFECTS.register("veil_of_fire", VeilOfFire::new);
    public static final RegistryObject<StatusEffect> BLOODTHIRSTY = EFFECTS.register("bloodthirsty", Bloodthirsty::new);
    public static final RegistryObject<StatusEffect> MAGIC_RESISTANCE = EFFECTS.register("magic_resistance", () -> new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x80ffff));
    public static final RegistryObject<StatusEffect> MOON_HERALD = EFFECTS.register("moon_herald", () -> new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x03e8fc));
    public static final RegistryObject<StatusEffect> DISABLE_HEAL = EFFECTS.register("disable_heal", () -> new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0xfc9d9d));
    public static final RegistryObject<StatusEffect> BLEED = EFFECTS.register("bleed", Bleed::new);
    public static final RegistryObject<StatusEffect> CALCULATED_FALL = EFFECTS.register("calculated_fall", () -> new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffff));
    public static final RegistryObject<StatusEffect> BLIGHT = EFFECTS.register("blight", () -> new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0x73013c));
    public static final RegistryObject<StatusEffect> SHADOW_STEP = EFFECTS.register("shadow_step", () -> new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x020e78).addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "48403ce1-d9b3-4757-b1ef-9fbacff0ed37", 0.30000000298023224, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<StatusEffect> COOLDOWN = EFFECTS.register("cooldown", () -> new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0x525252));
    public static final RegistryObject<StatusEffect> GHOSTLY = EFFECTS.register("ghostly", () -> new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x5e9191));
    public static final RegistryObject<StatusEffect> CHUNGUS_TONIC_EFFECT = EFFECTS.register("chungus_tonic_effect", ChungusTonic::new);
    public static final RegistryObject<StatusEffect> FROST_MOON = EFFECTS.register("frost_moon", FrostMoon::new);
    public static final RegistryObject<StatusEffect> BLADE_DANCE = EFFECTS.register("blade_dance", BladeDance::new);
    public static final RegistryObject<StatusEffect> STORMVEIL = EFFECTS.register("stormveil", Stormveil::new);

    public static final RegistryObject<Potion> WARDING = POTIONS.register("warding", () -> new Potion(new StatusEffectInstance(MAGIC_RESISTANCE.get(), 4000, 0)));
    public static final RegistryObject<Potion> STRONG_WARDING = POTIONS.register("strong_warding", () -> new Potion(new StatusEffectInstance(MAGIC_RESISTANCE.get(), 2000, 1)));
    public static final RegistryObject<Potion> LONG_WARDING = POTIONS.register("long_warding", () -> new Potion(new StatusEffectInstance(MAGIC_RESISTANCE.get(), 8000, 0)));
    public static final RegistryObject<Potion> TAINTED_AMBROSIA = POTIONS.register("tainted_ambrosia", () -> new Potion(new StatusEffectInstance(DISABLE_HEAL.get(), 600, 0)));

    public static final RegistryObject<Potion> CHUNGUS_TONIC_POTION = POTIONS.register("chungus_tonic", () -> new Potion(
            new StatusEffectInstance(StatusEffects.HASTE, 1000, 2),
            new StatusEffectInstance(StatusEffects.SATURATION, 400, 1),
            new StatusEffectInstance(EffectRegistry.CHUNGUS_TONIC_EFFECT.get(), 1000, 0)
    ));

    public static void registerEffects(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

    public static void registerPotions(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

    static class DefaultStatusEffect extends StatusEffect {

        public DefaultStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
            super(statusEffectCategory, color);
        }
    }

    public static int randomVibrantRGBA() {
        float h = ThreadLocalRandom.current().nextFloat();
        float s = 0.65f + ThreadLocalRandom.current().nextFloat() * 0.35f; // 0.65–1.0
        float v = 0.75f + ThreadLocalRandom.current().nextFloat() * 0.25f; // 0.75–1.0
        int rgb = Color.HSBtoRGB(h, s, v); // to hex
        return 0xFF000000 | rgb; // full alpha
    }
}
