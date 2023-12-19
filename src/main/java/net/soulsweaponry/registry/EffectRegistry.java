package net.soulsweaponry.registry;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.effect.*;

public class EffectRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SoulsWeaponry.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, SoulsWeaponry.MOD_ID);

    public static final RegistryObject<MobEffect> DECAY = EFFECTS.register("decay", Decay::new);
    public static final RegistryObject<MobEffect> FEAR = EFFECTS.register("fear", Fear::new);
    public static final RegistryObject<MobEffect> FREEZING = EFFECTS.register("freezing", Freezing::new);
    public static final RegistryObject<MobEffect> HALLOWED_DRAGON_MIST = EFFECTS.register("hallowed_dragon_mist", HallowedDragonMist::new);
    public static final RegistryObject<MobEffect> LIFE_LEACH = EFFECTS.register("life_leach", LifeLeach::new);
    public static final RegistryObject<MobEffect> POSTURE_BREAK = EFFECTS.register("posture_break", PostureBreak::new);
    public static final RegistryObject<MobEffect> RETRIBUTION = EFFECTS.register("retribution", () -> new DefaultStatusEffect(MobEffectCategory.HARMFUL, 0xc76700));
    public static final RegistryObject<MobEffect> VEIL_OF_FIRE = EFFECTS.register("veil_of_fire", VeilOfFire::new);
    public static final RegistryObject<MobEffect> BLOODTHIRSTY = EFFECTS.register("bloodthirsty", () -> new DefaultStatusEffect(MobEffectCategory.NEUTRAL, 0x630109));
    public static final RegistryObject<MobEffect> MAGIC_RESISTANCE = EFFECTS.register("magic_resistance", () -> new DefaultStatusEffect(MobEffectCategory.BENEFICIAL, 0x80ffff));
    public static final RegistryObject<MobEffect> MOON_HERALD = EFFECTS.register("moon_herald", () -> new DefaultStatusEffect(MobEffectCategory.BENEFICIAL, 0x03e8fc));
    public static final RegistryObject<MobEffect> DISABLE_HEAL = EFFECTS.register("disable_heal", () -> new DefaultStatusEffect(MobEffectCategory.HARMFUL, 0xfc9d9d));
    public static final RegistryObject<MobEffect> BLEED = EFFECTS.register("bleed", () -> new DefaultStatusEffect(MobEffectCategory.HARMFUL, 0xba0c00));
    public static final RegistryObject<MobEffect> CALCULATED_FALL = EFFECTS.register("calculated_fall", () -> new DefaultStatusEffect(MobEffectCategory.BENEFICIAL, 0xffffff));
    public static final RegistryObject<MobEffect> BLIGHT = EFFECTS.register("blight", () -> new DefaultStatusEffect(MobEffectCategory.HARMFUL, 0x73013c));

    public static final RegistryObject<Potion> WARDING = POTIONS.register("warding", () -> new Potion(new MobEffectInstance(MAGIC_RESISTANCE.get(), 4000, 0)));
    public static final RegistryObject<Potion> STRONG_WARDING = POTIONS.register("strong_warding", () -> new Potion(new MobEffectInstance(MAGIC_RESISTANCE.get(), 2000, 1)));
    public static final RegistryObject<Potion> LONG_WARDING = POTIONS.register("long_warding", () -> new Potion(new MobEffectInstance(MAGIC_RESISTANCE.get(), 8000, 0)));
    public static final RegistryObject<Potion> TAINTED_AMBROSIA = POTIONS.register("tainted_ambrosia", () -> new Potion(new MobEffectInstance(DISABLE_HEAL.get(), 600, 0)));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
        POTIONS.register(eventBus);
    }

    static class DefaultStatusEffect extends MobEffect {

        public DefaultStatusEffect(MobEffectCategory pCategory, int pColor) {
            super(pCategory, pColor);
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
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
        public void applyEffectTick(LivingEntity entity, int amplifier) {
            if (this == EffectRegistry.BLEED.get() && !(entity instanceof AbstractSkeleton || entity instanceof SkeletonHorse)) {
                entity.hurt(DamageSource.WITHER/*CustomDamageSource.BLEED*/, 1f + amplifier); //TODO add custom damage sources
            }
            if (this == EffectRegistry.BLOODTHIRSTY.get()) {
                entity.hurt(DamageSource.WITHER, 1F);
            }
        }
    }
}
