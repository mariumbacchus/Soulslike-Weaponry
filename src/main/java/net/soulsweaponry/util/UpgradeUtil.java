package net.soulsweaponry.util;

import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.gun.GunItem;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UpgradeUtil {

    private static final Identifier UPGRADE_DAMAGE_ID = Identifier.of(SoulsWeaponry.ModId, "upgrade.damage");
    private static final Identifier UPGRADE_ATTACK_SPEED_ID = Identifier.of(SoulsWeaponry.ModId, "upgrade.attack_speed");
    private static final Identifier UPGRADE_RANGED_DAMAGE_ID = Identifier.of(SoulsWeaponry.ModId, "upgrade.ranged.damage");
    private static final Identifier UPGRADE_RANGED_HASTE_ID  = Identifier.of(SoulsWeaponry.ModId, "upgrade.ranged.haste");
    private static final Identifier UPGRADE_MINING_EFFICIENCY_ID  = Identifier.of(SoulsWeaponry.ModId, "upgrade.mining.efficiency");

    public static final String GUN_BONUS_DAMAGE_KEY = "GunBonusDamage";

    private static Identifier armorUpgradeId(String slotName) {
        return Identifier.of(SoulsWeaponry.ModId, "upgrade.armor." + slotName);
    }

    private static Identifier armorToughnessUpgradeId(String slotName) {
        return Identifier.of(SoulsWeaponry.ModId, "upgrade.armor_toughness." + slotName);
    }

    private static EquipmentSlot getArmorSlot(ArmorItem armor) {
        return armor.getSlotType();
    }

    public static NbtList getOrCreateAttributeModifiersList(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.contains("AttributeModifiers", NbtElement.LIST_TYPE)) {
            nbt.put("AttributeModifiers", new NbtList());
        }
        return nbt.getList("AttributeModifiers", NbtElement.COMPOUND_TYPE);
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

        if (stack.getItem() instanceof ArmorItem armor) {
            UpgradeUtil.setOrReplaceArmorUpgrade(stack, armor, primary);
            UpgradeUtil.setOrReplaceArmorToughnessUpgrade(stack, armor, secondary);
        } else if (stack.getItem() instanceof RangedWeaponItem ranged) {
            if (ranged instanceof GunItem) {
                stack.getOrCreateNbt().putFloat(GUN_BONUS_DAMAGE_KEY, primary);
            } else {
                UpgradeUtil.setOrReplaceRangedDamageUpgrade(stack, primary);
                UpgradeUtil.setOrReplaceRangedHasteUpgrade(stack, secondary);
            }
        } else if (stack.getItem() instanceof MiningToolItem) {
            UpgradeUtil.setOrReplaceDamageUpgrade(stack, primary);
            UpgradeUtil.setOrReplaceMiningEfficiencyUpgrade(stack, secondary);
        } else {
            UpgradeUtil.setOrReplaceDamageUpgrade(stack, primary);
            if (secondary > 0) {
                UpgradeUtil.setOrReplaceAttackSpeedUpgrade(stack, secondary);
            }
        }
    }

    /**
     * Removes all SoulsWeaponry upgrade modifiers (upgrade.*)
     * but leaves vanilla + other mods' modifiers alone.
     */
    public static void clearAllUpgradeModifiers(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.contains("AttributeModifiers", NbtElement.LIST_TYPE)) {
            return;
        }
        NbtList list = nbt.getList("AttributeModifiers", NbtElement.COMPOUND_TYPE);
        if (list.isEmpty()) {
            return;
        }

        NbtList rebuilt = new NbtList();
        for (int i = 0; i < list.size(); i++) {
            NbtCompound e = list.getCompound(i);
            String name = e.getString("Name");
            if (name == null || name.isEmpty()) {
                rebuilt.add(e);
                continue;
            }
            if (!name.startsWith(SoulsWeaponry.ModId + ":upgrade.")) {
                rebuilt.add(e);
            }
        }
        nbt.put("AttributeModifiers", rebuilt);
    }

    /**
     * Adds/replaces a single upgrade modifier while preserving all other modifiers.
     * This adds a "blue" addition modifier instead of changing the main value unlike other methods in WeaponUtil.
     */
    private static void addOrReplaceUpgradeModifier(ItemStack stack, EntityAttribute attribute, EquipmentSlot slot, Identifier id, float amount, EntityAttributeModifier.Operation operation) {
        NbtCompound nbt = stack.getOrCreateNbt();
        NbtList list = getOrCreateAttributeModifiersList(stack);

        UUID uuid = uuidFor(id);
        int[] targetUuid = uuidToIntArray(uuid);

        NbtList rebuilt = new NbtList();
        for (int i = 0; i < list.size(); i++) {
            NbtCompound e = list.getCompound(i);
            if (matchesUuid(e, targetUuid)) {
                continue;
            }
            rebuilt.add(e);
        }

        Identifier attrId = Registries.ATTRIBUTE.getId(attribute);
        if (attrId == null) {
            nbt.put("AttributeModifiers", rebuilt);
            return;
        }

        NbtCompound mod = new NbtCompound();
        mod.putString("AttributeName", attrId.toString());
        mod.putString("Name", id.toString());
        mod.putDouble("Amount", amount);
        mod.putInt("Operation", operation.getId());
        mod.putIntArray("UUID", targetUuid);
        mod.putString("Slot", slotToString(slot));
        rebuilt.add(mod);

        nbt.put("AttributeModifiers", rebuilt);
    }

    private static void addOrReplaceUpgradeModifier(ItemStack stack, EntityAttribute attribute, EquipmentSlot slot, Identifier id, float amount) {
        addOrReplaceUpgradeModifier(stack, attribute, slot, id, amount, EntityAttributeModifier.Operation.ADDITION);
    }

    /**
     * Tool/weapon path: cumulative +ATTACK_DAMAGE (MAINHAND).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceDamageUpgrade(ItemStack stack, float total) {
        addOrReplaceUpgradeModifier(stack, EntityAttributes.GENERIC_ATTACK_DAMAGE, EquipmentSlot.MAINHAND, UPGRADE_DAMAGE_ID, total);
    }

    /**
     * Tool/weapon path: cumulative +MINING_EFFICIENCY (MAINHAND).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceMiningEfficiencyUpgrade(ItemStack stack, float total) {//TODO apply nbt and call from mixin
        addOrReplaceUpgradeModifier(stack, EntityAttributes.PLAYER_MINING_EFFICIENCY, EquipmentSlot.MAINHAND, UPGRADE_MINING_EFFICIENCY_ID, total);
    }

    /**
     * Tool/weapon path: cumulative +ATTACK_SPEED (MAINHAND).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceAttackSpeedUpgrade(ItemStack stack, float total) {
        addOrReplaceUpgradeModifier(stack, EntityAttributes.GENERIC_ATTACK_SPEED, EquipmentSlot.MAINHAND, UPGRADE_ATTACK_SPEED_ID, total);
    }

    /**
     * Armor path: cumulative +ARMOR (Armor slot).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceArmorUpgrade(ItemStack stack, ArmorItem armor, float total) {
        EquipmentSlot slot = getArmorSlot(armor);
        addOrReplaceUpgradeModifier(stack, EntityAttributes.GENERIC_ARMOR, slot, armorUpgradeId(armor.getSlotType().getName()), total);
    }

    /**
     * Armor path: cumulative +ARMOR_TOUGHNESS (Armor slot).
     * 'total' should already be (perLevelBonus * level).
     */
    public static void setOrReplaceArmorToughnessUpgrade(ItemStack stack, ArmorItem armor, float total) {
        EquipmentSlot slot = getArmorSlot(armor);
        addOrReplaceUpgradeModifier(stack, EntityAttributes.GENERIC_ARMOR_TOUGHNESS, slot, armorToughnessUpgradeId(armor.getSlotType().getName()), total);
    }

    /**
     * Smithing upgrade for ranged damage: +total as a MULTIPLIER to base.
     * total = 0.1f -> +10%; 0.3f -> +30%, etc.
     */
    public static void setOrReplaceRangedDamageUpgrade(ItemStack stack, float totalMultiplier) {
        addOrReplaceUpgradeModifier(stack, EntityAttributes_RangedWeapon.DAMAGE.attribute, EquipmentSlot.MAINHAND, UPGRADE_RANGED_DAMAGE_ID, totalMultiplier, EntityAttributeModifier.Operation.MULTIPLY_BASE);
    }

    /**
     * Smithing upgrade for draw speed (haste). Base is 100; API
     * examples also use ADD_MULTIPLIED_BASE for haste.
     */
    public static void setOrReplaceRangedHasteUpgrade(ItemStack stack, float totalMultiplier) {
        addOrReplaceUpgradeModifier(stack, EntityAttributes_RangedWeapon.HASTE.attribute, EquipmentSlot.MAINHAND, UPGRADE_RANGED_HASTE_ID, totalMultiplier, EntityAttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Nullable
    public static ItemUpgradeRecipe findItemUpgradeRecipeForBase(World world, ItemStack baseStack) {
        RecipeManager manager = world.getRecipeManager();
        for (Recipe<?> recipe : manager.values()) {
            if (recipe instanceof ItemUpgradeRecipe itemUpgradeRecipe) {
                if (itemUpgradeRecipe.testBase(baseStack)) {
                    return itemUpgradeRecipe;
                }
            }
        }
        return null;
    }

    private static UUID uuidFor(Identifier id) {
        return UUID.nameUUIDFromBytes(id.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static int[] uuidToIntArray(UUID uuid) {
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();
        return new int[] {
                (int)(most >> 32), (int)most,
                (int)(least >> 32), (int)least
        };
    }

    private static boolean matchesUuid(NbtCompound e, int[] targetUuid) {
        if (!e.contains("UUID", NbtElement.INT_ARRAY_TYPE)) {
            return false;
        }
        int[] u = e.getIntArray("UUID");
        if (u.length != 4) return false;
        return u[0] == targetUuid[0] && u[1] == targetUuid[1] && u[2] == targetUuid[2] && u[3] == targetUuid[3];
    }

    private static String slotToString(EquipmentSlot slot) {
        return switch (slot) {
            case MAINHAND -> "mainhand";
            case OFFHAND -> "offhand";
            case FEET -> "feet";
            case LEGS -> "legs";
            case CHEST -> "chest";
            case HEAD -> "head";
        };
    }
}
