package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.bonusdamage.AffinityPotency;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.posthit.SwitchPostHit;

public class NightlordsSword extends ModdedSword {

    private static final IAbility AFFINITY_POTENCY = new AffinityPotency(ConfigConstructor.sword_of_the_nightlord_bonus_damage_per_potency_amp);
    public static final IAbility SWITCH_POST_HIT =
            SwitchPostHit.builder()
                    .bonusAgainstEffect(ConfigConstructor.sword_of_the_nightlord_bonus_damage_if_target_is_afflicted)
                    .bleed(
                            (int) ConfigConstructor.sword_of_the_nightlord_base_bleed,
                            (int) ConfigConstructor.sword_of_the_nightlord_bleed_duration,
                            (int) ConfigConstructor.sword_of_the_nightlord_bleed_amp
                    )
                    .poison(
                            (int) ConfigConstructor.sword_of_the_nightlord_poison_duration,
                            (int) ConfigConstructor.sword_of_the_nightlord_poison_amp
                    )
                    .chainLightning(
                            ConfigConstructor.sword_of_the_nightlord_chain_lightning_base_radius,
                            ConfigConstructor.sword_of_the_nightlord_chain_lightning_radius_per_level,
                            ConfigConstructor.sword_of_the_nightlord_chain_lightning_base_damage,
                            ConfigConstructor.sword_of_the_nightlord_chain_lightning_damage_per_level
                    )
                    .wither(
                            (int) ConfigConstructor.sword_of_the_nightlord_wither_duration,
                            (int) ConfigConstructor.sword_of_the_nightlord_wither_amp
                    )
                    .freeze(
                            (int) ConfigConstructor.sword_of_the_nightlord_frost_buildup_post_hit,
                            (int) ConfigConstructor.sword_of_the_nightlord_permafrost_duration,
                            (int) ConfigConstructor.sword_of_the_nightlord_permafrost_amp
                    )
                    .fireTicks((int) ConfigConstructor.sword_of_the_nightlord_fire_ticks)
                    .crippleDuration((int) ConfigConstructor.sword_of_the_nightlord_cripple_duration)
                    .debuffAmps(
                            (int) ConfigConstructor.sword_of_the_nightlord_slowness_amp,
                            (int) ConfigConstructor.sword_of_the_nightlord_weakness_amp,
                            (int) ConfigConstructor.sword_of_the_nightlord_mining_fatigue_amp
                    )
                    .decay(
                            (int) ConfigConstructor.sword_of_the_nightlord_decay_duration,
                            (int) ConfigConstructor.sword_of_the_nightlord_decay_amp
                    )
                    .blight(
                            (int) ConfigConstructor.sword_of_the_nightlord_blight_duration,
                            (int) ConfigConstructor.sword_of_the_nightlord_blight_amp
                    )
                    .build();

    /**
     *  Night of the Lord
     *  - Switching to this weapon makes it apply a random on-hit effect post hit: bleed, poison, chain lightning, wither, permafrost/freeze, fire, slow & weakness & mining fatigue, blight & decay
     *  - Attacking targets with either of these effects deals bonus damage
     *  - Switching to this weapon makes it deal bonus damage a few seconds
     */
    public NightlordsSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.sword_of_the_nightlord_damage, ConfigConstructor.sword_of_the_nightlord_attack_speed, settings);
        this.addAbility(AFFINITY_POTENCY, SWITCH_POST_HIT);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_sword_of_the_nightlord;
    }
}
