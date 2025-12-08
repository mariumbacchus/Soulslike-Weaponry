package net.soulsweaponry.items.sword;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class MehrunesRazor extends ModdedSword {

    private static final List<EntityType<?>> NO_KILL_ENTITIES = WeaponUtil.getEntityListOffArray(ConfigConstructor.mehrunes_razor_no_instakill_entity_blacklist);

    public MehrunesRazor(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.mehrunes_razor_damage, ConfigConstructor.mehrunes_razor_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.FINAL_WOUNDS);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack) && !NO_KILL_ENTITIES.contains(target.getType())) {
            double rand = attacker.getRandom().nextDouble();
            float ratio = target.getMaxHealth() >= ConfigConstructor.mehrunes_razor_kill_trigger_cap ? ConfigConstructor.mehrunes_razor_kill_chance_over_health_cap : ConfigConstructor.mehrunes_razor_kill_chance_under_health_cap;
            if (rand <= ratio) {
                target.kill();
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_mehrunes_razor;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_mehrunes_razor;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return new String[0];
    }
}