package net.soulsweaponry.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.enchantments.FastHandsEnchantment;
import net.soulsweaponry.enchantments.StaggerEnchantment;
import net.soulsweaponry.enchantments.VisceralEnchantment;
import net.soulsweaponry.items.GunItem;

public class EnchantRegistry {

    public static final EnchantmentTarget GUN = EnchantmentTarget.create("gun", (item -> item instanceof GunItem));

    public static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SoulsWeaponry.ModId);

    public static final Enchantment FAST_HANDS = new FastHandsEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND);
    public static final Enchantment VISCERAL = new VisceralEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND);
    public static final Enchantment STAGGER = new StaggerEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND);

    static {
        if (!ConfigConstructor.disable_all_enchantments) {
            if (!ConfigConstructor.disable_enchantment_fast_hands) ENCHANTS.register("fast_hands", () -> FAST_HANDS);
            if (!ConfigConstructor.disable_enchantment_posture_breaker) ENCHANTS.register("visceral", () -> VISCERAL);
            if (!ConfigConstructor.disable_enchantment_stagger) ENCHANTS.register("stagger", () -> STAGGER);
        }
    }

    public static void register(IEventBus bus) {
        ENCHANTS.register(bus);
    }
}
