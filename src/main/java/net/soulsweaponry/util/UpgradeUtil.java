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
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.gun.GunItem;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;
import net.soulsweaponry.registry.ComponentRegistry;
import org.jetbrains.annotations.Nullable;

public class UpgradeUtil {

    private static final Identifier UPGRADE_DAMAGE_ID = Identifier.of(SoulsWeaponry.ModId, "upgrade.damage");
    private static final Identifier UPGRADE_ATTACK_SPEED_ID = Identifier.of(SoulsWeaponry.ModId, "upgrade.attack_speed");
    private static final Identifier UPGRADE_RANGED_DAMAGE_ID = Identifier.of(SoulsWeaponry.ModId, "upgrade.ranged.damage");
    private static final Identifier UPGRADE_RANGED_HASTE_ID  = Identifier.of(SoulsWeaponry.ModId, "upgrade.ranged.haste");
    private static final Identifier UPGRADE_MINING_EFFICIENCY_ID  = Identifier.of(SoulsWeaponry.ModId, "upgrade.mining.efficiency");

    private static Identifier armorUpgradeId(String slotName) {
        return Identifier.of(SoulsWeaponry.ModId, "upgrade.armor." + slotName);
    }

    private static Identifier armorToughnessUpgradeId(String slotName) {
        return Identifier.of(SoulsWeaponry.ModId, "upgrade.armor_toughness." + slotName);
    }

    private static AttributeModifierSlot getArmorSlot(ItemStack stack) {
        var equippable = stack.get(DataComponentTypes.EQUIPPABLE);
        if (equippable != null) {
            return AttributeModifierSlot.forEquipmentSlot(equippable.slot());
        }
        // Fallback (should basically never happen for real armor)
        return AttributeModifierSlot.HEAD;
    }

    public static AttributeModifiersComponent getEffectiveAttrComponent(ItemStack stack) {
        AttributeModifiersComponent fromStack = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        return fromStack != null && !fromStack.modifiers().isEmpty() ? fromStack : null;
    }

    /**
     * Rebuilds all "upgrade.*" modifiers based on:
     *  <li> current item type (armor / ranged / gun / melee) </li>
     *  <li> given upgrade level </li>
     *  <li> primary/secondary per-level bonuses </li>
     *  <p>
     *  Mainly called in {@link net.soulsweaponry.recipe.ItemUpgradeRecipe#applyUpgrades(ItemStack, int)}
     *  and when switching items in {@link net.soulsweaponry.api.trickweapon.TrickWeaponUtil}.
     */
    public static void rebuildUpgradeAttributesForCurrentForm(ItemStack stack, int level, float primaryPerLevel, float secondaryPerLevel) {
        if (level <= 0) {
            UpgradeUtil.clearAllUpgradeModifiers(stack);
            return;
        }
        UpgradeUtil.clearAllUpgradeModifiers(stack);

        float primary = primaryPerLevel * level;
        float secondary = secondaryPerLevel * level;
        switch (stack.getItem()) {
            case ArmorItem armor -> {
                UpgradeUtil.setOrReplaceArmorUpgrade(stack, primary);
                UpgradeUtil.setOrReplaceArmorToughnessUpgrade(stack, secondary);
            }
            case RangedWeaponItem ranged -> {
                if (ranged instanceof GunItem) {
                    stack.set(ComponentRegistry.GUN_BONUS_DAMAGE, primary); // Calculated inside the weapon instead of attribute
                } else {
                    UpgradeUtil.setOrReplaceRangedDamageUpgrade(stack, primary); // +% projectile damage
                    UpgradeUtil.setOrReplaceRangedHasteUpgrade(stack, secondary); // +% draw speed
                }
            }
            case MiningToolItem miningToolItem -> {
                UpgradeUtil.setOrReplaceDamageUpgrade(stack, primary);
                UpgradeUtil.setOrReplaceMiningEfficiencyUpgrade(stack, secondary);
            }
            default -> {
                UpgradeUtil.setOrReplaceDamageUpgrade(stack, primary);
                if (secondary > 0) {
                    UpgradeUtil.setOrReplaceAttackSpeedUpgrade(stack, secondary);
                }
            }
        }
    }

    /**
     * Removes all SoulsWeaponry upgrade modifiers (upgrade.*)
     * but leaves vanilla + other mods' modifiers alone.
     */
    public static void clearAllUpgradeModifiers(ItemStack stack) {
        AttributeModifiersComponent current = getEffectiveAttrComponent(stack);
        if (current == null || current.modifiers().isEmpty()) {
            return;
        }
        AttributeModifiersComponent.Builder b = AttributeModifiersComponent.builder();
        for (AttributeModifiersComponent.Entry e : current.modifiers()) {
            EntityAttributeModifier mod = e.modifier();
            Identifier id = mod.id();
            // Keep any modifier that isn't of "soulsweapons:upgrade.*"
            if (id == null
                    || !id.getNamespace().equals(SoulsWeaponry.ModId)
                    || !id.getPath().startsWith("upgrade.")) {
                b.add(e.attribute(), e.modifier(), e.slot());
            }
        }
        stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, b.build());
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
        addOrReplaceUpgradeModifier(stack, EntityAttributes.ATTACK_DAMAGE, AttributeModifierSlot.MAINHAND, UPGRADE_DAMAGE_ID, total);
    }

    /**
     * Tool/weapon path: cumulative +MINING_EFFICIENCY (MAINHAND).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceMiningEfficiencyUpgrade(ItemStack stack, float total) {
        addOrReplaceUpgradeModifier(stack, EntityAttributes.MINING_EFFICIENCY, AttributeModifierSlot.MAINHAND, UPGRADE_MINING_EFFICIENCY_ID, total);
    }

    /**
     * Tool/weapon path: cumulative +ATTACK_SPEED (MAINHAND).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceAttackSpeedUpgrade(ItemStack stack, float total) {
        addOrReplaceUpgradeModifier(stack, EntityAttributes.ATTACK_SPEED, AttributeModifierSlot.MAINHAND, UPGRADE_ATTACK_SPEED_ID, total);
    }

    /**
     * Armor path: cumulative +ARMOR (Armor slot).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceArmorUpgrade(ItemStack stack, float total) {
        AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(stack.get(DataComponentTypes.EQUIPPABLE).slot());
        addOrReplaceUpgradeModifier(stack, EntityAttributes.ARMOR, slot, armorUpgradeId(stack.get(DataComponentTypes.EQUIPPABLE).slot().getName()), total);
    }

    /**
     * Armor path: cumulative +ARMOR_TOUGHNESS (Armor slot).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceArmorToughnessUpgrade(ItemStack stack, float total) {
        AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(stack.get(DataComponentTypes.EQUIPPABLE).slot());
        addOrReplaceUpgradeModifier(stack, EntityAttributes.ARMOR_TOUGHNESS, slot, armorToughnessUpgradeId(stack.get(DataComponentTypes.EQUIPPABLE).slot().getName()), total);
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

    @Nullable
    public static ItemUpgradeRecipe findItemUpgradeRecipeForBase(World world, ItemStack baseStack) {
        if (world.getServer() == null) {
            return null;
        }
        var manager = world.getServer().getRecipeManager();
        if (!(manager instanceof ServerRecipeManager serverManager)) {
            return null;
        }
        for (RecipeEntry<?> entry : serverManager.values()) {
            if (entry.value() instanceof ItemUpgradeRecipe recipe) {
                if (recipe.base().isPresent() && recipe.base().get().test(baseStack)) {
                    return recipe;
                }
            }
        }
        return null;
    }
}
