package net.soulsweaponry.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.enchantments.*;
import net.soulsweaponry.util.ModTags;

public class EnchantRegistry {

    public static final MapCodec<? extends EnchantmentEntityEffect> STAGGER_EFFECT_TYPE = registerEntityEffect("stagger", StaggerEnchantmentEffect.CODEC);

    // TODO gotta make sure gun enchants dont apply to bows during enchantment
    public static final RegistryKey<Enchantment> FAST_HANDS = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, "fast_hands"));
    public static final RegistryKey<Enchantment> STAGGER = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, "stagger"));
    public static final RegistryKey<Enchantment> VISCERAL = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, "visceral"));
    public static final RegistryKey<Enchantment> TETHER = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, "tether"));
    public static final RegistryKey<Enchantment> RICOCHET = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, "ricochet"));
    public static final RegistryKey<Enchantment> PHANTOM_TRACE = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, "phantom_trace"));
    public static final RegistryKey<Enchantment> FROSTSILVER = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, "frostsilver"));
    public static final RegistryKey<Enchantment> BLIGHT_CARRIER = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, "blight_carrier"));
    public static final RegistryKey<Enchantment> MISFIRE_CURSE = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, "misfire_curse"));
    public static final RegistryKey<Enchantment> CHAIN_LIGHTNING = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, "chain_lightning"));
    public static final RegistryKey<Enchantment> EXPLOSIVE_ROUNDS = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, "explosive_rounds"));
    public static final RegistryKey<Enchantment> ETHEREAL = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, "ethereal"));

    // TODO since enchants are now datapack based, config values are useless such as max level and stuff
    // TODO gotta also figure out how to disable them, or just make people delete/create empty datapack files instead
    public static void bootstrap(Registerable<Enchantment> registerable) {
        RegistryEntryLookup<Enchantment> enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        RegistryEntryLookup<Item> itemLookup = registerable.getRegistryLookup(RegistryKeys.ITEM);
        // TODO I guess I could make custom enchant-trigger files for each gun enchant one day like how they exist for punch & power
        registerable.register(FAST_HANDS, Enchantment.builder(
                        Enchantment.definition(
                                itemLookup.getOrThrow(ModTags.Items.GUN_ENCHANTABLE),
                                2,
                                3,
                                Enchantment.leveledCost(10, 10),
                                Enchantment.leveledCost(15, 10),
                                2,
                                AttributeModifierSlot.MAINHAND
                        )).build(FAST_HANDS.getValue()));

        registerable.register(STAGGER, Enchantment.builder(
                        Enchantment.definition(
                                itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                5,
                                3,
                                Enchantment.leveledCost(10, 10),
                                Enchantment.leveledCost(15, 10),
                                2,
                                AttributeModifierSlot.MAINHAND)
                ).addEffect(
                        EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER,
                        EnchantmentEffectTarget.VICTIM,
                        new StaggerEnchantmentEffect()
                ).build(STAGGER.getValue())
        );

        registerable.register(VISCERAL, Enchantment.builder(
                        Enchantment.definition(
                                itemLookup.getOrThrow(ModTags.Items.GUN_ENCHANTABLE),
                                5,
                                3,
                                Enchantment.leveledCost(10, 10),
                                Enchantment.leveledCost(15, 10),
                                2,
                                AttributeModifierSlot.MAINHAND
                        )).build(VISCERAL.getValue()));

        registerable.register(TETHER, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ModTags.Items.GUN_ENCHANTABLE),
                        5,
                        2,
                        Enchantment.leveledCost(12, 15),
                        Enchantment.leveledCost(15, 10),
                        2,
                        AttributeModifierSlot.MAINHAND
                )).build(TETHER.getValue()));

        registerable.register(RICOCHET, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ModTags.Items.GUN_ENCHANTABLE),
                        2,
                        3,
                        Enchantment.leveledCost(0, 25),
                        Enchantment.constantCost(75),
                        2,
                        AttributeModifierSlot.MAINHAND
                )).exclusiveSet(enchantments.getOrThrow(ModTags.Enchantments.BULLET_COLLISION_EXCLUSIVE_SET))
                .build(RICOCHET.getValue()));

        registerable.register(PHANTOM_TRACE, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ModTags.Items.GUN_ENCHANTABLE),
                        2,
                        3,
                        Enchantment.leveledCost(0, 25),
                        Enchantment.constantCost(75),
                        2,
                        AttributeModifierSlot.MAINHAND
                )).build(PHANTOM_TRACE.getValue()));

        registerable.register(FROSTSILVER, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ModTags.Items.GUN_ENCHANTABLE),
                        5,
                        2,
                        Enchantment.leveledCost(10, 10),
                        Enchantment.leveledCost(15, 10),
                        2,
                        AttributeModifierSlot.MAINHAND
                )).build(FROSTSILVER.getValue()));

        registerable.register(BLIGHT_CARRIER, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ModTags.Items.GUN_ENCHANTABLE),
                        2,
                        2,
                        Enchantment.leveledCost(10, 10),
                        Enchantment.leveledCost(15, 10),
                        2,
                        AttributeModifierSlot.MAINHAND
                )).build(BLIGHT_CARRIER.getValue()));

        registerable.register(MISFIRE_CURSE, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ModTags.Items.GUN_ENCHANTABLE),
                        2,
                        1,
                        Enchantment.constantCost(25),
                        Enchantment.constantCost(50),
                        2,
                        AttributeModifierSlot.MAINHAND
                )).build(MISFIRE_CURSE.getValue()));

        registerable.register(CHAIN_LIGHTNING, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ModTags.Items.GUN_ENCHANTABLE),
                        2,
                        3,
                        Enchantment.leveledCost(0, 20),
                        Enchantment.constantCost(75),
                        2,
                        AttributeModifierSlot.MAINHAND
                )).build(CHAIN_LIGHTNING.getValue()));

        registerable.register(EXPLOSIVE_ROUNDS, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ModTags.Items.GUN_ENCHANTABLE),
                        1,
                        3,
                        Enchantment.leveledCost(0, 10),
                        Enchantment.leveledCost(15, 10),
                        2,
                        AttributeModifierSlot.MAINHAND
                )).build(EXPLOSIVE_ROUNDS.getValue()));

        registerable.register(ETHEREAL, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ModTags.Items.GUN_ENCHANTABLE),
                        1,
                        1,
                        Enchantment.leveledCost(0, 25),
                        Enchantment.leveledCost(50, 25),
                        2,
                        AttributeModifierSlot.MAINHAND
                )).exclusiveSet(enchantments.getOrThrow(ModTags.Enchantments.BULLET_COLLISION_EXCLUSIVE_SET))
                .build(ETHEREAL.getValue()));
    }

    public static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(name), codec);
    }

    public static void init() {}
}
