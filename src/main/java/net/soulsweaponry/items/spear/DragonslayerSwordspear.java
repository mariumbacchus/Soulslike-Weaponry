package net.soulsweaponry.items.spear;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.abilitykeybind.LightningCall;
import net.soulsweaponry.items.abilities.bonusdamage.DragonBonus;
import net.soulsweaponry.items.abilities.statboost.RainBoostsStats;
import net.soulsweaponry.items.abilities.stoppedusing.ThrowDragonslayerSwordspear;

public class DragonslayerSwordspear extends ModdedSword {

    private static final RainBoostsStats RAIN_BOOSTS_STATS = new RainBoostsStats(WeaponConfig.dragonslayer_swordspear_rain_bonus_damage, WeaponConfig.dragonslayer_swordspear_rain_bonus_attack_speed);
    private static final DragonBonus DRAGON_BONUS = new DragonBonus(WeaponConfig.dragonslayer_swordspear_dragons_scourge_bonus, WeaponConfig.dragonslayer_swordspear_dragons_scourge_bonus_per_level);
    private static final ThrowDragonslayerSwordspear THROW_DRAGONSLAYER_SWORDSPEAR = new ThrowDragonslayerSwordspear(
            5f, (int) WeaponConfig.dragonslayer_swordspear_throw_min_cooldown,
            (int) WeaponConfig.dragonslayer_swordspear_throw_cooldown,
            (int) WeaponConfig.dragonslayer_swordspear_throw_reduced_cooldown_per_level,
            WeaponConfig.dragonslayer_swordspear_throw_cooldown_modifier_when_raining
    );
    private static final LightningCall LIGHTNING_CALL = new LightningCall(
            WeaponConfig.dragonslayer_swordspear_lightning_call_block_radius,
            (int) WeaponConfig.dragonslayer_swordspear_lightning_call_lightning_amount,
            WeaponConfig.dragonslayer_swordspear_lightning_call_bonus_lightning_amount_per_level,
            WeaponConfig.dragonslayer_swordspear_storm_stomp_damage,
            WeaponConfig.dragonslayer_swordspear_storm_stomp_bonus_damage_per_level,
            WeaponConfig.dragonslayer_swordspear_storm_stomp_knockback,
            (int) WeaponConfig.dragonslayer_swordspear_lightning_call_min_cooldown,
            (int) WeaponConfig.dragonslayer_swordspear_lightning_call_ability_cooldown,
            (int) WeaponConfig.dragonslayer_swordspear_lightning_call_reduced_cooldown_per_level,
            WeaponConfig.dragonslayer_swordspear_lightning_call_cooldown_modifier_when_raining
    );

    public DragonslayerSwordspear(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.dragonslayer_swordspear_damage, WeaponConfig.dragonslayer_swordspear_attack_speed, settings);
        this.addAbility(DRAGON_BONUS, RAIN_BOOSTS_STATS, THROW_DRAGONSLAYER_SWORDSPEAR, LIGHTNING_CALL);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_dragonslayer_swordspear;
    }
}