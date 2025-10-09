package net.soulsweaponry.items.sword;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.IChargeNeeded;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

public class HolyMoonlightSword extends TrickWeapon implements IChargeNeeded {

    public HolyMoonlightSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.holy_moonlight_sword_damage, ConfigConstructor.holy_moonlight_sword_attack_speed, settings, ConfigConstructor.disable_use_holy_moonlight_sword,
                ConfigConstructor.holy_moonlight_sword_righteous_base_undead_bonus_damage, ConfigConstructor.holy_moonlight_sword_righteous_undead_bonus_damage_per_level);
        this.addTooltipAbility(TooltipAbilities.CHARGE, TooltipAbilities.CHARGE_BONUS_DAMAGE);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            this.addCharge(stack, this.getAddedCharge(stack));
        }
        return super.postHit(stack, target, attacker);
    }

    private float getBonusDamage(ItemStack stack) {
        float per = (float) this.getCharge(stack) / ConfigConstructor.holy_moonlight_ability_charge_needed;
        return ConfigConstructor.holy_moonlight_sword_max_bonus_damage * per;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (this.isDisabled(stack) || world.isClient) {
            return;
        }
        WeaponUtil.modifyStackAttributes(stack, this.getAttackDamage() + this.getBonusDamage(stack) - 1, this.getAttackSpeed());
    }

    @Override
    public int getMaxCharge() {
        return (int) ConfigConstructor.holy_moonlight_ability_charge_needed;
    }

    @Override
    public int getAddedCharge(ItemStack stack) {
        float base = ConfigConstructor.holy_moonlight_sword_charge_added_post_hit;
        return (int) (base + WeaponUtil.getEnchantDamageBonus(stack));
    }

    @Override
    public boolean acceptsMoonHeraldEffect(ItemStack stack) {
        return true;
    }
}