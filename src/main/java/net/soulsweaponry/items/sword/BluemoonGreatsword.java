package net.soulsweaponry.items.sword;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.IChargeNeeded;
import net.soulsweaponry.util.TooltipAbilities;

public class BluemoonGreatsword extends MoonlightGreatsword implements IChargeNeeded {

    public BluemoonGreatsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.bluemoon_greatsword_damage, ConfigConstructor.bluemoon_greatsword_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.NEED_CHARGE, TooltipAbilities.CHARGE);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        this.addCharge(stack, this.getAddedCharge(stack));
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_bluemoon_greatsword;
    }

    @Override
    public int getMaxCharge() {
        return ConfigConstructor.bluemoon_greatsword_charge_needed;
    }

    @Override
    public int getAddedCharge(ItemStack stack) {
        return ConfigConstructor.bluemoon_greatsword_charge_added_post_hit;
    }

    @Override
    public boolean acceptsMoonHeraldEffect(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_bluemoon_greatsword;
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public float getProjectileDamage() {
        return ConfigConstructor.bluemoon_greatsword_projectile_damage;
    }
}