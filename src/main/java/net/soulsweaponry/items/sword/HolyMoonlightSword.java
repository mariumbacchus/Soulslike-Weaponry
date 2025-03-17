package net.soulsweaponry.items.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.IChargeNeeded;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

public class HolyMoonlightSword extends TrickWeapon implements IChargeNeeded {

    public HolyMoonlightSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.holy_moonlight_sword_damage, ConfigConstructor.holy_moonlight_sword_attack_speed, settings,false,
                ConfigConstructor.holy_moonlight_sword_righteous_undead_bonus_damage, ConfigConstructor.is_fireproof_holy_moonlight_sword, ConfigConstructor.disable_use_holy_moonlight_sword);
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
        if (this.isDisabled(stack)) return 0;
        float per = (float) this.getCharge(stack) / (float) ConfigConstructor.holy_moonlight_ability_charge_needed;
        return (float) ConfigConstructor.holy_moonlight_sword_max_bonus_damage * per;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.getAttackDamage() + this.getBonusDamage(stack), EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", this.getAttackSpeed(), EntityAttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }

    @Override
    public int getMaxCharge() {
        return ConfigConstructor.holy_moonlight_ability_charge_needed;
    }

    @Override
    public int getAddedCharge(ItemStack stack) {
        int base = ConfigConstructor.holy_moonlight_sword_charge_added_post_hit;
        return base + WeaponUtil.getEnchantDamageBonus(stack);
    }

    @Override
    public boolean acceptsMoonHeraldEffect(ItemStack stack) {
        return true;
    }
}