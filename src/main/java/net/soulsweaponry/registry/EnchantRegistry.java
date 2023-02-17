package net.soulsweaponry.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.enchantments.FastHandsEnchantment;
import net.soulsweaponry.enchantments.StaggerEnchantment;
import net.soulsweaponry.enchantments.VisceralEnchantment;

public class EnchantRegistry {

    public static final Enchantment FAST_HANDS = new FastHandsEnchantment(Enchantment.Rarity.RARE, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    public static final Enchantment VISCERAL = new VisceralEnchantment(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    public static final Enchantment STAGGER = new StaggerEnchantment(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});

    public static void init() {
        if (!ConfigConstructor.disable_all_enchantments) {
            if (!ConfigConstructor.disable_enchantment_fast_hands) registerEnchantment(FAST_HANDS, "fast_hands");
            if (!ConfigConstructor.disable_enchantment_posture_breaker) registerEnchantment(VISCERAL, "visceral");
            if (!ConfigConstructor.disable_enchantment_stagger) registerEnchantment(STAGGER, "stagger");
        }
    }

    public static <I extends Enchantment> I registerEnchantment(I enchantment, String name) {
		return Registry.register(Registries.ENCHANTMENT, new Identifier(SoulsWeaponry.ModId, name), enchantment);
	}
}
