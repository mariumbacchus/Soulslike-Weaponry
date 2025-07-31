package net.soulsweaponry.items.katana;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.IDragonBonus;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.ChainLightning;
import net.soulsweaponry.util.TooltipAbilities;

public class Dragonbane extends ModdedSword implements IDragonBonus {

    public Dragonbane(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.dragonbane_damage, ConfigConstructor.dragonbane_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.DRAGONS_SCOURGE, TooltipAbilities.CHAIN_LIGHTNING);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            float radius = ConfigConstructor.dragonbane_chain_lightning_range_per_enchant_level * (this.getReduceCooldownEnchantLevel(stack) + 1);
            float damage = ConfigConstructor.dragonbane_chain_lightning_damage_per_level * (this.getReduceCooldownEnchantLevel(stack) + 1);
            ChainLightning.trigger(attacker.getWorld(), target, attacker, true, damage, radius);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_dragonbane;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        // TODO currently using this to add the chain lightning multiplier instead of reducing cooldown
        // add option to increase abilities with enchant inside ICooldownItem?
        return ConfigConstructor.dragonbane_chain_lightning_enchant_can_boost;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        // TODO currently using this to add the chain lightning multiplier instead of reducing cooldown
        // add option to increase abilities with enchant inside ICooldownItem?
        return ConfigConstructor.dragonbane_chain_lightning_boost_enchant;
    }

    @Override
    public float getBaseDragonBonus(ItemStack stack) {
        return ConfigConstructor.dragonbane_dragons_scourge_bonus;
    }
}