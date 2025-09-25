package net.soulsweaponry.util;

import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public class UpgradeUtil {

    private static final Identifier UPGRADE_DAMAGE_ID = Identifier.of(SoulsWeaponry.ModId, "upgrade.damage");
    private static final Identifier UPGRADE_ATTACK_SPEED_ID = Identifier.of(SoulsWeaponry.ModId, "upgrade.attack_speed");
    private static final Identifier UPGRADE_RANGED_DAMAGE_ID = Identifier.of(SoulsWeaponry.ModId, "upgrade.ranged.damage");
    private static final Identifier UPGRADE_RANGED_HASTE_ID  = Identifier.of(SoulsWeaponry.ModId, "upgrade.ranged.haste");

    private static Identifier armorUpgradeId(String slotName) {
        return Identifier.of(SoulsWeaponry.ModId, "upgrade.armor." + slotName);
    }

    private static Identifier armorToughnessUpgradeId(String slotName) {
        return Identifier.of(SoulsWeaponry.ModId, "upgrade.armor_toughness." + slotName);
    }

    private static AttributeModifierSlot getArmorSlot(ArmorItem armor) {
        return switch (armor.getSlotType()) {
            case CHEST -> AttributeModifierSlot.CHEST;
            case LEGS -> AttributeModifierSlot.LEGS;
            case FEET -> AttributeModifierSlot.FEET;
            default -> AttributeModifierSlot.HEAD;
        };
    }

    public static AttributeModifiersComponent getEffectiveAttrComponent(ItemStack stack) {
        AttributeModifiersComponent fromStack = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        return fromStack != null && !fromStack.modifiers().isEmpty() ? fromStack : stack.getItem().getAttributeModifiers();
    }

    /**
     * Adds/replaces a single upgrade modifier while preserving all other modifiers.
     * This adds a "blue" addition modifier instead of changing the main value unlike other methods in WeaponUtil.
     */
    private static void addOrReplaceUpgradeModifier(ItemStack stack, RegistryEntry<EntityAttribute> attribute, AttributeModifierSlot slot, Identifier id, float amount, EntityAttributeModifier.Operation operation) {
        AttributeModifiersComponent current = getEffectiveAttrComponent(stack);

        AttributeModifiersComponent.Builder b = AttributeModifiersComponent.builder();
        for (AttributeModifiersComponent.Entry e : current.modifiers()) {
            Identifier existing = e.modifier().id();
            if (existing == null || !existing.equals(id)) {
                b.add(e.attribute(), e.modifier(), e.slot());
            }
        }

        EntityAttributeModifier mod = new EntityAttributeModifier(id, amount, operation);
        b.add(attribute, mod, slot);
        stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, b.build());
    }

    private static void addOrReplaceUpgradeModifier(ItemStack stack, RegistryEntry<EntityAttribute> attribute, AttributeModifierSlot slot, Identifier id, float amount) {
        addOrReplaceUpgradeModifier(stack, attribute, slot, id, amount, EntityAttributeModifier.Operation.ADD_VALUE);
    }

    /**
     * Tool/weapon path: cumulative +ATTACK_DAMAGE (MAINHAND).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceDamageUpgrade(ItemStack stack, float total) {
        addOrReplaceUpgradeModifier(stack, EntityAttributes.GENERIC_ATTACK_DAMAGE, AttributeModifierSlot.MAINHAND, UPGRADE_DAMAGE_ID, total);
    }

    /**
     * Tool/weapon path: cumulative +ATTACK_SPEED (MAINHAND).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceAttackSpeedUpgrade(ItemStack stack, float total) {
        addOrReplaceUpgradeModifier(stack, EntityAttributes.GENERIC_ATTACK_SPEED, AttributeModifierSlot.MAINHAND, UPGRADE_ATTACK_SPEED_ID, total);
    }

    /**
     * Armor path: cumulative +ARMOR (Armor slot).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceArmorUpgrade(ItemStack stack, ArmorItem armor, float total) {
        AttributeModifierSlot slot = getArmorSlot(armor);
        addOrReplaceUpgradeModifier(stack, EntityAttributes.GENERIC_ARMOR, slot, armorUpgradeId(armor.getSlotType().getName().toLowerCase()), total);
    }

    /**
     * Armor path: cumulative +ARMOR_TOUGHNESS (Armor slot).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceArmorToughnessUpgrade(ItemStack stack, ArmorItem armor, float total) {
        AttributeModifierSlot slot = getArmorSlot(armor);
        addOrReplaceUpgradeModifier(stack, EntityAttributes.GENERIC_ARMOR_TOUGHNESS, slot, armorToughnessUpgradeId(armor.getSlotType().getName().toLowerCase()), total);
    }

    /**
     * Smithing upgrade for ranged damage: +total as a MULTIPLIER to base.
     * total = 0.1f -> +10%; 0.3f -> +30%, etc.
     */
    public static void setOrReplaceRangedDamageUpgrade(ItemStack stack, float totalMultiplier) {
        addOrReplaceUpgradeModifier(stack, EntityAttributes_RangedWeapon.DAMAGE.entry, AttributeModifierSlot.MAINHAND, UPGRADE_RANGED_DAMAGE_ID, totalMultiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    /**
     * Smithing upgrade for draw speed (haste). Base is 100; API
     * examples also use ADD_MULTIPLIED_BASE for haste.
     */
    public static void setOrReplaceRangedHasteUpgrade(ItemStack stack, float totalMultiplier) {
        addOrReplaceUpgradeModifier(stack, EntityAttributes_RangedWeapon.HASTE.entry, AttributeModifierSlot.MAINHAND, UPGRADE_RANGED_HASTE_ID, totalMultiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }
}
