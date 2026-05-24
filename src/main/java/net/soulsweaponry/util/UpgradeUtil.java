package net.soulsweaponry.util;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeManager;
import net.projectile_damage.api.EntityAttributes_ProjectileDamage;
import net.soulsweaponry.items.gun.GunItem;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpgradeUtil {

    private static final Map<Item, ItemUpgradeRecipe> ITEM_CACHE = new HashMap<>();

    public static final UUID UPGRADE_DAMAGE = UUID.fromString("4a1c33a1-7a43-4670-8b0e-6567e4492309");
    public static final UUID UPGRADE_ATTACK_SPEED = UUID.fromString("1fb53593-9302-4753-a56f-ec9cad97e1e7");
    public static final UUID UPGRADE_RANGED_DAMAGE = UUID.fromString("a80078e9-593d-4344-beb5-5b0dd98c05cf");
    public static final UUID UPGRADE_RANGED_HASTE = UUID.fromString("fc081fc5-6d03-4fa8-8d4e-76bdf9260af1");
    public static final Map<EquipmentSlot, UUID> UPGRADE_ARMOR_UUIDS = Map.of(
            EquipmentSlot.HEAD, UUID.fromString("a1c5ebbd-d2b3-47ff-86f6-bd05d011042f"),
            EquipmentSlot.CHEST, UUID.fromString("9c8f2dc1-568d-4f0e-add9-529bbc16d9fd"),
            EquipmentSlot.LEGS, UUID.fromString("6794c6ea-7bf8-4392-ad70-b96e4541c0a5"),
            EquipmentSlot.FEET, UUID.fromString("b17ac08c-d8da-46f4-9445-31399a237df9")
    );
    public static final Map<EquipmentSlot, UUID> UPGRADE_ARMOR_TOUGHNESS_UUIDS = Map.of(
            EquipmentSlot.HEAD, UUID.fromString("b294a88e-d01a-454e-95ca-d4c248ca3452"),
            EquipmentSlot.CHEST, UUID.fromString("1ed4b145-41d1-4d02-a1c9-a231fe2a18bd"),
            EquipmentSlot.LEGS, UUID.fromString("c3d61498-2282-483a-b8d8-19b9e0d35957"),
            EquipmentSlot.FEET, UUID.fromString("b679e338-fe2b-4d55-8b67-60882a228530")
    );

    public static Multimap<EntityAttribute, EntityAttributeModifier> applyUpgradeModifiers(Multimap<EntityAttribute, EntityAttributeModifier> vanilla, ItemStack stack, EquipmentSlot slot) {
        int level = NbtHelper.getInt(stack, NbtIds.ITEM_UPGRADE_LEVEL, 0);
        if (level <= 0) {
            return vanilla;
        }
        float primaryPerLevel = NbtHelper.getFloat(stack, NbtIds.UPGRADE_PRIMARY, 0f);
        float secondaryPerLevel = NbtHelper.getFloat(stack, NbtIds.UPGRADE_SECONDARY, 0f);
        float primary = primaryPerLevel * level;
        float secondary = secondaryPerLevel * level;

        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(vanilla);
        Item item = stack.getItem();
        if (item instanceof ArmorItem) {
            applyArmorUpgrade(builder, stack, slot, primary, secondary);
        } else if (item instanceof RangedWeaponItem) {
            if (item instanceof GunItem) {
                NbtHelper.putFloat(stack, NbtIds.GUN_BONUS_DAMAGE, primary);
            } else {
                applyRangedUpgrade(builder, slot, primary, secondary);
            }
        } else if (item instanceof MiningToolItem) {
            applyMiningUpgrade(builder, stack, slot, primary, secondary);
        } else {
            applyWeaponUpgrade(builder, slot, primary, secondary);
        }
        return builder.build();
    }

    private static void applyWeaponUpgrade(
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder,
            EquipmentSlot slot,
            float damageBonus,
            float speedBonus
    ) {
        if (slot != EquipmentSlot.MAINHAND) {
            return;
        }
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(UPGRADE_DAMAGE, "upgrade.damage", damageBonus, EntityAttributeModifier.Operation.ADDITION));
        if (speedBonus > 0) {
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(UPGRADE_ATTACK_SPEED, "upgrade.attack_speed", speedBonus, EntityAttributeModifier.Operation.ADDITION));
        }
    }

    private static void applyArmorUpgrade(
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder,
            ItemStack stack,
            EquipmentSlot slot,
            float armorBonus,
            float toughnessBonus
    ) {
        if (!(stack.getItem() instanceof ArmorItem armorItem)) {
            return;
        }
        if (armorItem.getSlotType() != slot) {
            return;
        }
        builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(UPGRADE_ARMOR_UUIDS.get(slot), "upgrade.armor", armorBonus, EntityAttributeModifier.Operation.ADDITION));
        if (toughnessBonus > 0) {
            builder.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(UPGRADE_ARMOR_TOUGHNESS_UUIDS.get(slot), "upgrade.armor_toughness", toughnessBonus, EntityAttributeModifier.Operation.ADDITION));
        }
    }

    private static void applyMiningUpgrade(
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder,
            ItemStack stack,
            EquipmentSlot slot,
            float damageBonus,
            float efficiencyBonus
    ) {
        if (slot != EquipmentSlot.MAINHAND) {
            return;
        }
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(UPGRADE_DAMAGE, "upgrade.damage", damageBonus, EntityAttributeModifier.Operation.ADDITION));
        if (efficiencyBonus > 0) {
            NbtHelper.putFloat(stack, NbtIds.UPGRADE_MINING_EFFICIENCY, efficiencyBonus);
        }
    }

    private static void applyRangedUpgrade(
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder,
            EquipmentSlot slot,
            float damageBonus,
            float hasteBonus
    ) {
        if (slot != EquipmentSlot.MAINHAND) {
            return;
        }

        builder.put(EntityAttributes_ProjectileDamage.GENERIC_PROJECTILE_DAMAGE, new EntityAttributeModifier(UPGRADE_RANGED_DAMAGE, "upgrade.ranged_damage", damageBonus, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        //TODO there is no ranged weapon api version for forge 1.20.1, lowkey gotta make one myself then
        //builder.put(EntityAttributes_RangedWeapon.DAMAGE.attribute, new EntityAttributeModifier(UPGRADE_RANGED_DAMAGE, "upgrade.ranged_damage", damageBonus, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        //if (hasteBonus > 0) {
        //    builder.put(EntityAttributes_RangedWeapon.HASTE.attribute, new EntityAttributeModifier(UPGRADE_RANGED_HASTE, "upgrade.ranged_haste", hasteBonus, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        //}
    }

    public static void rebuildRecipeCache(RecipeManager manager) {
        ITEM_CACHE.clear();
        for (var recipe : manager.values()) {
            if (!(recipe instanceof ItemUpgradeRecipe upgrade)) {
                continue;
            }
            ItemStack[] stacks = upgrade.base().getMatchingStacks();
            for (ItemStack stack : stacks) {
                Item item = stack.getItem();
                if (!upgrade.fallback() || !ITEM_CACHE.containsKey(item)) {
                    ITEM_CACHE.put(item, upgrade);
                }
            }
        }
    }

    @Nullable
    public static ItemUpgradeRecipe findItemUpgradeRecipeForBase(ItemStack stack) {
        return ITEM_CACHE.get(stack.getItem());
    }
}