package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.bonusdamage.UndeadBonus;
import net.soulsweaponry.items.abilities.predicate.FullHealthNeeded;
import net.soulsweaponry.items.abilities.stoppedusing.SkywardStrikes;

public class MasterSword extends ModdedSword {

    private static final UndeadBonus UNDEAD_BONUS = new UndeadBonus(ConfigConstructor.master_sword_righteous_base_undead_bonus_damage, ConfigConstructor.master_sword_righteous_undead_bonus_damage_per_level);
    private static final FullHealthNeeded FULL_HEALTH_NEEDED = new FullHealthNeeded();
    private static final SkywardStrikes SKYWARD_STRIKES = new SkywardStrikes(
            (int) ConfigConstructor.master_sword_projectile_amount,
            ConfigConstructor.master_sword_bonus_projectile_amount_per_level,
            ConfigConstructor.master_sword_projectile_velocity,
            ConfigConstructor.master_sword_projectile_damage,
            ConfigConstructor.master_sword_projectile_bonus_damage_per_level
    );

    public MasterSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.master_sword_damage, ConfigConstructor.master_sword_attack_speed, settings);
        this.addAbility(UNDEAD_BONUS, FULL_HEALTH_NEEDED, SKYWARD_STRIKES);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_master_sword;
    }
}