package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.bonusdamage.UndeadBonus;
import net.soulsweaponry.items.abilities.predicate.FullHealthNeeded;
import net.soulsweaponry.items.abilities.stoppedusing.SkywardStrikes;

public class MasterSword extends ModdedSword {

    private static final UndeadBonus UNDEAD_BONUS = new UndeadBonus(WeaponConfig.master_sword_righteous_base_undead_bonus_damage, WeaponConfig.master_sword_righteous_undead_bonus_damage_per_level);
    private static final FullHealthNeeded FULL_HEALTH_NEEDED = new FullHealthNeeded((int) WeaponConfig.master_sword_item_upgrade_needed_to_remove_health_requirement);
    private static final SkywardStrikes SKYWARD_STRIKES = new SkywardStrikes(
            (int) WeaponConfig.master_sword_projectile_amount,
            WeaponConfig.master_sword_bonus_projectile_amount_per_level,
            WeaponConfig.master_sword_projectile_velocity,
            WeaponConfig.master_sword_projectile_damage,
            WeaponConfig.master_sword_projectile_bonus_damage_per_level
    );

    public MasterSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.master_sword_damage, WeaponConfig.master_sword_attack_speed, settings);
        this.addAbility(UNDEAD_BONUS, FULL_HEALTH_NEEDED, SKYWARD_STRIKES);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_master_sword;
    }
}