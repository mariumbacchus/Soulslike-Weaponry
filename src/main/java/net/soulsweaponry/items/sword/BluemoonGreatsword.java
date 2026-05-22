package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.predicate.EssenceNeeded;
import net.soulsweaponry.items.abilities.posthit.GivesEssence;
import net.soulsweaponry.items.abilities.stoppedusing.ShootMoonlight;

public class BluemoonGreatsword extends ModdedSword {

    private static final ShootMoonlight SHOOT_MOONLIGHT = new ShootMoonlight(
            (int) ConfigConstructor.bluemoon_greatsword_projectile_amount,
            ConfigConstructor.bluemoon_greatsword_bonus_projectile_amount_per_level,
            ConfigConstructor.bluemoon_greatsword_projectile_velocity,
            ConfigConstructor.bluemoon_greatsword_projectile_damage,
            ConfigConstructor.bluemoon_greatsword_projectile_bonus_damage_per_level
    );
    private static final GivesEssence GIVES_ESSENCE = new GivesEssence(
            (int) ConfigConstructor.bluemoon_greatsword_essence_added_post_hit,
            (int) ConfigConstructor.bluemoon_greatsword_bonus_essence_added_post_hit_per_level,
            (int) ConfigConstructor.bluemoon_greatsword_essence_needed
    );
    private static final EssenceNeeded ESSENCE_NEEDED = new EssenceNeeded(
            (int) ConfigConstructor.bluemoon_greatsword_essence_needed, true, (int) ConfigConstructor.bluemoon_greatsword_item_upgrade_needed_to_remove_essence_requirement
    );

    public BluemoonGreatsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.bluemoon_greatsword_damage, ConfigConstructor.bluemoon_greatsword_attack_speed, settings);
        this.addAbility(SHOOT_MOONLIGHT, GIVES_ESSENCE, ESSENCE_NEEDED);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_bluemoon_greatsword;
    }
}