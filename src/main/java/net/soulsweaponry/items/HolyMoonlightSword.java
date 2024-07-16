package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.WeaponUtil;

public class HolyMoonlightSword extends TrickWeapon implements IChargeNeeded {

    public HolyMoonlightSword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, 4, attackSpeed, settings, 3, 4, false, true);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled()) {
            this.addCharge(stack, this.getAddedCharge(stack));
        }
        return super.postHit(stack, target, attacker);
    }

    private float getBonusDamage(ItemStack stack) {
        if (this.isDisabled()) return 0;
        float per = (float) this.getCharge(stack) / (float) CommonConfig.HOLY_MOON_ABILITY_CHARGE_NEED.get();
        return (float) CommonConfig.HOLY_MOON_SWORD_MAX_BONUS.get() * per;
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
        return CommonConfig.HOLY_MOON_ABILITY_CHARGE_NEED.get();
    }

    @Override
    public int getAddedCharge(ItemStack stack) {
        int base = CommonConfig.HOLY_MOON_SWORD_CHARGE_ADDED.get();
        return base + WeaponUtil.getEnchantDamageBonus(stack);
    }
}