package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.effect.*;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

// NOTE: Never remove effects whenever iterating through the effect list, especially inside the onRemoved method in StatusEffect classes
public class EffectRegistry {

    public static final RegistryEntry<StatusEffect> HALLOWED_DRAGON_MIST = registerEffect(new HallowedDragonMist(), "hallowed_dragon_mist");
    public static final RegistryEntry<StatusEffect> BLOODTHIRSTY = registerEffect(new Bloodthirsty(), "bloodthirsty");
    public static final RegistryEntry<StatusEffect> POSTURE_BREAK = registerEffect(new PostureBreak(), "posture_break");
    public static final RegistryEntry<StatusEffect> LIFE_LEACH = registerEffect(new LifeLeach(), "life_leach");
    public static final RegistryEntry<StatusEffect> RETRIBUTION = registerEffect(new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0xc76700), "retribution");
    public static final RegistryEntry<StatusEffect> FEAR = registerEffect(new Fear(), "fear");
    public static final RegistryEntry<StatusEffect> DECAY = registerEffect(new Decay(), "decay");
    public static final RegistryEntry<StatusEffect> MAGIC_RESISTANCE = registerEffect(new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x80ffff), "magic_resistance");
    public static final RegistryEntry<StatusEffect> MOON_HERALD = registerEffect(new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x03e8fc), "moon_herald");
    public static final RegistryEntry<StatusEffect> FREEZING = registerEffect(new Freezing(), "freezing");
    public static final RegistryEntry<StatusEffect> DISABLE_HEAL = registerEffect(new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0xfc9d9d), "disable_heal");
    public static final RegistryEntry<StatusEffect> BLEED = registerEffect(new Bleed(), "bleed");
    public static final RegistryEntry<StatusEffect> CALCULATED_FALL = registerEffect(new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffff), "calculated_fall");
    public static final RegistryEntry<StatusEffect> VEIL_OF_FIRE = registerEffect(new VeilOfFire(), "veil_of_fire");
    public static final RegistryEntry<StatusEffect> BLIGHT = registerEffect(new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0x73013c), "blight");
    public static final RegistryEntry<StatusEffect> SHADOW_STEP = registerEffect(
            new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x020e78)
                    .addAttributeModifier(
                            EntityAttributes.GENERIC_MOVEMENT_SPEED,
                            Identifier.of(SoulsWeaponry.ModId, "effect.shadow_step"), 0.30000000298023224,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    ), "shadow_step"
    );
    public static final RegistryEntry<StatusEffect> COOLDOWN = registerEffect(new DefaultStatusEffect(StatusEffectCategory.HARMFUL, 0x525252), "cooldown");
    public static final RegistryEntry<StatusEffect> GHOSTLY = registerEffect(new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x5e9191), "ghostly");
    public static final RegistryEntry<StatusEffect> CHUNGUS_TONIC_EFFECT = registerEffect(new ChungusTonic(), "chungus_tonic_effect");
    public static final RegistryEntry<StatusEffect> FROST_MOON = registerEffect(new FrostMoon(), "frost_moon");
    public static final RegistryEntry<StatusEffect> BLADE_DANCE = registerEffect(new BladeDanceEffect(), "blade_dance");
    public static final RegistryEntry<StatusEffect> STORMVEIL = registerEffect(new Stormveil(), "stormveil");
    public static final RegistryEntry<StatusEffect> POTENCY = registerEffect(new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0x200094), "potency");
    public static final RegistryEntry<StatusEffect> SOUL_OF_CINDER = registerEffect(
            new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0xcc3300)
                    .addAttributeModifier(
                            EntityAttributes.GENERIC_ATTACK_DAMAGE,
                            Identifier.of(SoulsWeaponry.ModId, "effect.soul_of_cinder"), 2.0,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    ), "soul_of_cinder"
    );
    public static final RegistryEntry<StatusEffect> EXALTED = registerEffect(
            new DefaultStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff0000)
                    .addAttributeModifier(
                            EntityAttributes.GENERIC_ATTACK_DAMAGE,
                            Identifier.of(SoulsWeaponry.ModId, "effect.exalted.strength"), 1.5f,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    ).addAttributeModifier(
                            EntityAttributes.GENERIC_ATTACK_SPEED,
                            Identifier.of(SoulsWeaponry.ModId, "effect.exalted.haste"), 0.1F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
            , "exalted"
    );
    public static final RegistryEntry<StatusEffect> ECHO = registerEffect(new Echo(), "echo");
    public static final RegistryEntry<StatusEffect> MAGIC_FRAILTY = registerEffect(new MagicFrailty(), "magic_frailty");

    public static final RegistryEntry<Potion> WARDING = registerPotion(new Potion(new StatusEffectInstance(EffectRegistry.MAGIC_RESISTANCE, 4000)), "warding");
    public static final RegistryEntry<Potion> STRONG_WARDING = registerPotion(new Potion("warding", new StatusEffectInstance(EffectRegistry.MAGIC_RESISTANCE, 2000, 1)), "strong_warding");
    public static final RegistryEntry<Potion> LONG_WARDING = registerPotion(new Potion("warding", new StatusEffectInstance(EffectRegistry.MAGIC_RESISTANCE, 8000)), "long_warding");
    public static final RegistryEntry<Potion> TAINTED_AMBROSIA = registerPotion(new Potion(new StatusEffectInstance(EffectRegistry.DISABLE_HEAL, 600, 0)), "tainted_ambrosia");
    public static final RegistryEntry<Potion> CHUNGUS_TONIC_POTION = registerPotion(
            new Potion(
                        new StatusEffectInstance(EffectRegistry.CHUNGUS_TONIC_EFFECT, 1000, 0),
                        new StatusEffectInstance(StatusEffects.HASTE, 1000, 2),
                        new StatusEffectInstance(StatusEffects.SATURATION, 400, 1)
                ),
            "chungus_tonic"
        );

    public static void init() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, BlockRegistry.HYDRANGEA.asItem(), WARDING);
            builder.registerPotionRecipe(Potions.AWKWARD, BlockRegistry.OLEANDER.asItem(), TAINTED_AMBROSIA);
            builder.registerPotionRecipe(WARDING, Items.GLOWSTONE_DUST, STRONG_WARDING);
            builder.registerPotionRecipe(WARDING, Items.REDSTONE, LONG_WARDING);
            builder.registerPotionRecipe(Potions.AWKWARD, ItemRegistry.CHUNGUS_EMERALD, CHUNGUS_TONIC_POTION);
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!stack.isOf(Items.POTION) && !stack.isOf(Items.SPLASH_POTION) && !stack.isOf(Items.LINGERING_POTION) && !stack.isOf(Items.TIPPED_ARROW)) {
                return TypedActionResult.pass(stack);
            }
            PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
            if (contents == null || !contents.matches(EffectRegistry.CHUNGUS_TONIC_POTION)) {
                return TypedActionResult.pass(stack);
            }
            if (!world.isClient) {
                ItemStack updated = stack.copy();
                updated.set(
                        DataComponentTypes.POTION_CONTENTS,
                        new PotionContentsComponent(contents.potion(), Optional.of(randomVibrantRGBA()), contents.customEffects())
                );
                player.setStackInHand(hand, updated);
            }

            return TypedActionResult.pass(stack);
        });
    }

    public static RegistryEntry<StatusEffect> registerEffect(StatusEffect effect, String name) {
		return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(SoulsWeaponry.ModId, name), effect);
	}

    private static RegistryEntry<Potion> registerPotion(Potion potion, String name) {
        return Registry.registerReference(Registries.POTION, Identifier.of(SoulsWeaponry.ModId, name), potion);
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
