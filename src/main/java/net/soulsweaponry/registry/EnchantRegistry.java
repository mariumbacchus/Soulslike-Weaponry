package net.soulsweaponry.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.enchantments.*;

import java.util.ArrayList;
import java.util.List;

public class EnchantRegistry {

    public static final List<Enchantment> GUN_ENCHANTS = new ArrayList<>();

    public static final Enchantment FAST_HANDS = new FastHandsEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND);
    public static final Enchantment VISCERAL = new VisceralEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND);
    public static final Enchantment STAGGER = new StaggerEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND);
    public static final Enchantment ETHEREAL = new EtherealEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND);
    public static final Enchantment EXPLOSIVE_ROUNDS = new ExplosiveEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND);
    public static final Enchantment CHAIN_LIGHTNING = new ChainLightningEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND);
    public static final Enchantment MISFIRE_CURSE = new MisfireCurseEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND);

    public static void init() {
        if (!ConfigConstructor.disable_all_enchantments) {
            if (!ConfigConstructor.disable_enchantment_fast_hands) registerEnchantment(FAST_HANDS, "fast_hands", true);
            if (!ConfigConstructor.disable_enchantment_posture_breaker) registerEnchantment(VISCERAL, "visceral", true);
            if (!ConfigConstructor.disable_enchantment_stagger) registerEnchantment(STAGGER, "stagger", false);
            if (!ConfigConstructor.disable_enchantment_ethereal_ammunition) registerEnchantment(ETHEREAL, "ethereal", true);
            if (!ConfigConstructor.disable_enchantment_explosive_rounds) registerEnchantment(EXPLOSIVE_ROUNDS, "explosive_rounds", true);
            if (!ConfigConstructor.disable_enchantment_chain_lightning) registerEnchantment(CHAIN_LIGHTNING, "chain_lightning", true);
            if (!ConfigConstructor.disable_enchantment_misfire_curse) registerEnchantment(MISFIRE_CURSE, "misfire_curse", true);
        }
    }

    public static <I extends Enchantment> I registerEnchantment(I enchantment, String name, boolean isGunEnchant) {
        if (isGunEnchant) {
            GUN_ENCHANTS.add(enchantment);
        }
		return Registry.register(Registries.ENCHANTMENT, new Identifier(SoulsWeaponry.ModId, name), enchantment);
	}
}
