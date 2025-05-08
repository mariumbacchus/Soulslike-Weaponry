package net.soulsweaponry.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.enchantments.*;
import net.soulsweaponry.items.gun.GunItem;

public class EnchantRegistry {

    public static final EnchantmentTarget GUN = EnchantmentTarget.create("gun", (item -> item instanceof GunItem));

    public static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SoulsWeaponry.ModId);

    public static final Enchantment FAST_HANDS = new FastHandsEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND);
    public static final Enchantment VISCERAL = new VisceralEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND);
    public static final Enchantment STAGGER = new StaggerEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND);
    public static final Enchantment ETHEREAL = new EtherealEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND);
    public static final Enchantment EXPLOSIVE_ROUNDS = new ExplosiveEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND);
    public static final Enchantment CHAIN_LIGHTNING = new ChainLightningEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND);
    public static final Enchantment MISFIRE_CURSE = new MisfireCurseEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND);

    static {
        if (!ConfigConstructor.disable_all_enchantments) {
            if (!ConfigConstructor.disable_enchantment_fast_hands) ENCHANTS.register("fast_hands", () -> FAST_HANDS);
            if (!ConfigConstructor.disable_enchantment_posture_breaker) ENCHANTS.register("visceral", () -> VISCERAL);
            if (!ConfigConstructor.disable_enchantment_stagger) ENCHANTS.register("stagger", () -> STAGGER);
            if (!ConfigConstructor.disable_enchantment_ethereal_ammunition) ENCHANTS.register("ethereal", () -> ETHEREAL);
            if (!ConfigConstructor.disable_enchantment_explosive_rounds) ENCHANTS.register("explosive_rounds", () -> EXPLOSIVE_ROUNDS);
            if (!ConfigConstructor.disable_enchantment_chain_lightning) ENCHANTS.register("chain_lightning", () -> CHAIN_LIGHTNING);
            if (!ConfigConstructor.disable_enchantment_misfire_curse) ENCHANTS.register("misfire_curse", () -> MISFIRE_CURSE);
        }
    }

    public static void register(IEventBus bus) {
        ENCHANTS.register(bus);
    }
}
