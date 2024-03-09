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
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.WeaponUtil;

public class HolyMoonlightSword extends TrickWeapon implements IChargeNeeded {

    private final float attackSpeed;

    public HolyMoonlightSword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, 4, attackSpeed, settings, 3, 4, false, true);
        this.attackSpeed = attackSpeed;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        this.addCharge(stack, this.getAddedCharge(stack));
        return super.postHit(stack, target, attacker);
    }

    private float getBonusDamage(ItemStack stack) {
        float per = (float) this.getCharge(stack) / (float) ConfigConstructor.holy_moonlight_ability_charge_needed;
        return (float) ConfigConstructor.holy_moonlight_sword_max_bonus_damage * per;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.getAttackDamage() + this.getBonusDamage(stack), EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", this.attackSpeed, EntityAttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot);
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
}
