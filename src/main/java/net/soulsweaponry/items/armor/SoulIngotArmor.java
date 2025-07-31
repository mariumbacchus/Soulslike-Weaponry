package net.soulsweaponry.items.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ArmorRegistry;

public class SoulIngotArmor extends SetBonusArmor {

    public SoulIngotArmor(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public boolean isSlotActive(PlayerEntity player, EquipmentSlot slot) {
        return false;
    }

    @Override
    protected void tickAdditionalSetEffects(ItemStack stack, PlayerEntity player) {}

    @Override
    protected Item getMatchingBoots() {
        return ArmorRegistry.SOUL_INGOT_BOOTS;
    }

    @Override
    protected Item getMatchingLegs() {
        return ArmorRegistry.SOUL_INGOT_LEGGINGS;
    }

    @Override
    protected Item getMatchingChest() {
        return ArmorRegistry.SOUL_INGOT_CHESTPLATE;
    }

    @Override
    protected Item getMatchingHead() {
        return ArmorRegistry.SOUL_INGOT_HELMET;
    }

    @Override
    public StatusEffectInstance[] getFullSetEffects() {
        return new StatusEffectInstance[] {
                new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 0, false, false)
        };
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_soul_ingot_armor;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return new String[0];
    }

    @Override
    public float[] getBleedBuildupResistances() {
        return ConfigConstructor.soul_ingot_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ConfigConstructor.soul_ingot_bleed_damage_resistances;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ConfigConstructor.soul_ingot_posture_buildup_resistances;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ConfigConstructor.soul_ingot_base_posture_increase;
    }
}