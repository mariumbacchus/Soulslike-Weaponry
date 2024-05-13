package net.soulsweaponry.registry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.tag.EntityTypeTags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.effect.*;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.ModTags;

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
    public static final RegistryObject<StatusEffect> BLOODTHIRSTY = EFFECTS.register("bloodthirsty", () -> new DefaultStatusEffect(StatusEffectCategory.NEUTRAL, 0x630109));
    public static final RegistryObject<StatusEffect> MAGIC_RESISTANCE = EFFECTS.register("magic_resistance", () -> new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x80ffff));
    public static final RegistryObject<StatusEffect> MOON_HERALD = EFFECTS.register("moon_herald", () -> new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x03e8fc));
    public static final RegistryObject<StatusEffect> DISABLE_HEAL = EFFECTS.register("disable_heal", () -> new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0xfc9d9d));
    public static final RegistryObject<StatusEffect> BLEED = EFFECTS.register("bleed", () -> new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0xba0c00));
    public static final RegistryObject<StatusEffect> CALCULATED_FALL = EFFECTS.register("calculated_fall", () -> new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffff));
    public static final RegistryObject<StatusEffect> BLIGHT = EFFECTS.register("blight", () -> new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0x73013c));
    public static final RegistryObject<StatusEffect> SHADOW_STEP = EFFECTS.register("shadow_step", () -> new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x020e78).addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "48403ce1-d9b3-4757-b1ef-9fbacff0ed37", 0.30000000298023224, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<StatusEffect> COOLDOWN = EFFECTS.register("cooldown", () -> new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0x525252));

    public static final RegistryObject<Potion> WARDING = POTIONS.register("warding", () -> new Potion(new StatusEffectInstance(MAGIC_RESISTANCE.get(), 4000, 0)));
    public static final RegistryObject<Potion> STRONG_WARDING = POTIONS.register("strong_warding", () -> new Potion(new StatusEffectInstance(MAGIC_RESISTANCE.get(), 2000, 1)));
    public static final RegistryObject<Potion> LONG_WARDING = POTIONS.register("long_warding", () -> new Potion(new StatusEffectInstance(MAGIC_RESISTANCE.get(), 8000, 0)));
    public static final RegistryObject<Potion> TAINTED_AMBROSIA = POTIONS.register("tainted_ambrosia", () -> new Potion(new StatusEffectInstance(DISABLE_HEAL.get(), 600, 0)));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
        POTIONS.register(eventBus);
    }

    static class DefaultStatusEffect extends StatusEffect {

        public DefaultStatusEffect(StatusEffectCategory pCategory, int pColor) {
            super(pCategory, pColor);
        }

        @Override
        public boolean canApplyUpdateEffect(int duration, int amplifier) {
            if (this == EffectRegistry.BLEED.get()) {
                int i = 15 >> amplifier;
                if (i > 0) {
                    return duration % i == 0;
                }
                return true;
            }
            if (this == EffectRegistry.BLOODTHIRSTY.get()) {
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
            if (this == EffectRegistry.BLEED.get() && !entity.getType().isIn(EntityTypeTags.SKELETONS) && !entity.getType().isIn(ModTags.Entities.SKELETONS)) {
                entity.damage(CustomDamageSource.BLEED, 1f + amplifier);
            }
            if (this == EffectRegistry.BLOODTHIRSTY.get()) {
                entity.damage(DamageSource.WITHER, 1F);
            }
        }
    }
}
