package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.bonusdamage.AffinityPotency;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.posthit.SwitchPostHit;

public class NightlordsSword extends ModdedSword {

    private static final IAbility AFFINITY_POTENCY = new AffinityPotency(WeaponConfig.sword_of_the_nightlord_bonus_damage_per_potency_amp);
    public static final IAbility SWITCH_POST_HIT =
            SwitchPostHit.builder()
                    .bonusAgainstEffect(WeaponConfig.sword_of_the_nightlord_bonus_damage_if_target_is_afflicted)
                    .bleed(
                            (int) WeaponConfig.sword_of_the_nightlord_base_bleed,
                            (int) WeaponConfig.sword_of_the_nightlord_bleed_duration,
                            (int) WeaponConfig.sword_of_the_nightlord_bleed_amp
                    )
                    .poison(
                            (int) WeaponConfig.sword_of_the_nightlord_poison_duration,
                            (int) WeaponConfig.sword_of_the_nightlord_poison_amp
                    )
                    .chainLightning(
                            WeaponConfig.sword_of_the_nightlord_chain_lightning_base_radius,
                            WeaponConfig.sword_of_the_nightlord_chain_lightning_radius_per_level,
                            WeaponConfig.sword_of_the_nightlord_chain_lightning_base_damage,
                            WeaponConfig.sword_of_the_nightlord_chain_lightning_damage_per_level
                    )
                    .wither(
                            (int) WeaponConfig.sword_of_the_nightlord_wither_duration,
                            (int) WeaponConfig.sword_of_the_nightlord_wither_amp
                    )
                    .freeze(
                            (int) WeaponConfig.sword_of_the_nightlord_frost_buildup_post_hit,
                            (int) WeaponConfig.sword_of_the_nightlord_permafrost_duration,
                            (int) WeaponConfig.sword_of_the_nightlord_permafrost_amp
                    )
                    .fireTicks((int) WeaponConfig.sword_of_the_nightlord_fire_ticks)
                    .crippleDuration((int) WeaponConfig.sword_of_the_nightlord_cripple_duration)
                    .debuffAmps(
                            (int) WeaponConfig.sword_of_the_nightlord_slowness_amp,
                            (int) WeaponConfig.sword_of_the_nightlord_weakness_amp,
                            (int) WeaponConfig.sword_of_the_nightlord_mining_fatigue_amp
                    )
                    .decay(
                            (int) WeaponConfig.sword_of_the_nightlord_decay_duration,
                            (int) WeaponConfig.sword_of_the_nightlord_decay_amp
                    )
                    .blight(
                            (int) WeaponConfig.sword_of_the_nightlord_blight_duration,
                            (int) WeaponConfig.sword_of_the_nightlord_blight_amp
                    )
                    .magicDamage(
                            WeaponConfig.sword_of_the_nightlord_spellblade_bonus_magic_damage,
                            WeaponConfig.sword_of_the_nightlord_spellblade_bonus_magic_damage_per_level,
                            WeaponConfig.sword_of_the_nightlord_spellblade_target_is_player_mod
                    )
                    .build();

    /**
     *  Night of the Lord
     *  - Switching to this weapon makes it apply a random on-hit effect post hit: bleed, poison, chain lightning, wither, permafrost/freeze, fire, slow & weakness & mining fatigue, blight & decay
     *  - Attacking targets with either of these effects deals bonus damage
     *  - Switching to this weapon makes it deal bonus damage a few seconds
     */
    public NightlordsSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.sword_of_the_nightlord_damage, WeaponConfig.sword_of_the_nightlord_attack_speed, settings);
        this.addAbility(AFFINITY_POTENCY, SWITCH_POST_HIT);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_sword_of_the_nightlord;
    }
}
