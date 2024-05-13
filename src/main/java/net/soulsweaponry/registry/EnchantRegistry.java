package net.soulsweaponry.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.enchantments.FastHandsEnchant;
import net.soulsweaponry.enchantments.VisceralEnchant;
import net.soulsweaponry.items.GunItem;

public class EnchantRegistry {

    public static final EnchantmentTarget GUN = EnchantmentTarget.create("gun", (item -> item instanceof GunItem));

    public static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SoulsWeaponry.ModId);

    public static final Enchantment FAST_HANDS = new FastHandsEnchant(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND);
    public static final Enchantment VISCERAL = new VisceralEnchant(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND);
    public static final Enchantment STAGGER = new VisceralEnchant(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND);

    static {
        if (!CommonConfig.DISABLE_ENCHANTS.get()) {
            if (!CommonConfig.DISABLE_FAST_HANDS.get()) ENCHANTS.register("fast_hands", () -> FAST_HANDS);
            if (!CommonConfig.DISABLE_POSTURE_BREAKER.get()) ENCHANTS.register("visceral", () -> VISCERAL);
            if (!CommonConfig.DISABLE_STAGGER.get()) ENCHANTS.register("stagger", () -> STAGGER);
        }
    }

    public static void register(IEventBus bus) {
        ENCHANTS.register(bus);
    }
}
