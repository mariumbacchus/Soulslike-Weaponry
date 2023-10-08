package net.soulsweaponry.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.DarkinScythePre;
import net.soulsweaponry.items.ShadowAssassinScythe;
import net.soulsweaponry.items.Skofnung;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.registry.WeaponRegistry;

import java.util.ArrayList;
import java.util.List;

import static net.soulsweaponry.items.SoulHarvestingItem.KILLS;

public class WeaponUtil {
    
    public static final Enchantment[] DAMAGE_ENCHANTS = {Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS};
    public static final String PREV_TRICK_WEAPON = "trick_weapon_to_transform";
    public static final String CHARGE = "current_charge";

    /**
     * Define all trick weapons here, which is gathered by PacketsServer to switch to the given weapon's index
     */
    public static final TrickWeapon[] TRICK_WEAPONS = {
            WeaponRegistry.KIRKHAMMER,
            WeaponRegistry.SILVER_SWORD,
            WeaponRegistry.HOLY_GREATSWORD,
            WeaponRegistry.HOLY_MOONLIGHT_GREATSWORD,
            WeaponRegistry.HOLY_MOONLIGHT_SWORD,
    };

    /**
     * Returns level of the damage enchant, for example {@code 5} for Sharpness V or {@code 4} for Smite IV
     */
    public static int getEnchantDamageBonus(ItemStack stack) {
        for (Enchantment ench : DAMAGE_ENCHANTS) {
            if (EnchantmentHelper.getLevel(ench, stack) > 0) {
                return EnchantmentHelper.getLevel(ench, stack);
            }
        }
        return 0;
    }

    public static Text getSwitchWeaponName(ItemStack stack, TrickWeapon weapon) {
        TrickWeapon switchWeapon = TRICK_WEAPONS[weapon.getSwitchWeaponIndex()];
        if (stack.hasNbt() && stack.getNbt().contains(WeaponUtil.PREV_TRICK_WEAPON)) {
            switchWeapon = TRICK_WEAPONS[stack.getNbt().getInt(WeaponUtil.PREV_TRICK_WEAPON)];
        }
        return switchWeapon.getName();
    }

    public static void addCharge(ItemStack stack, int amount) {
        if (stack.hasNbt()) {
            if (stack.getNbt().contains(CHARGE)) {
                int add = stack.getNbt().getInt(CHARGE) >= ConfigConstructor.holy_moonlight_ability_charge_needed ?
                        ConfigConstructor.holy_moonlight_ability_charge_needed : stack.getNbt().getInt(CHARGE) + amount + WeaponUtil.getEnchantDamageBonus(stack);
                stack.getNbt().putInt(CHARGE, add);
            } else {
                stack.getNbt().putInt(CHARGE, 0);
            }
        }
    }

    public static boolean isCharged(ItemStack stack) {
        if (stack.hasNbt()) {
            if (stack.getNbt().contains(CHARGE)) {
                return stack.getNbt().getInt(CHARGE) >= ConfigConstructor.holy_moonlight_ability_charge_needed;
            } else {
                stack.getNbt().putInt(CHARGE, 0);
            }
        }
        return false;
    }

    public static int getCharge(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(CHARGE)) {
            return stack.getNbt().getInt(CHARGE);
        }
        return 0;
    }

    public static int getAddedCharge(ItemStack stack) {
        return (ConfigConstructor.holy_moonlight_ability_charge_added_post_hit + WeaponUtil.getEnchantDamageBonus(stack)) * (stack.isOf(WeaponRegistry.HOLY_MOONLIGHT_SWORD) ? 3 : 1);
    }

    public static List<Integer> arrayToList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int t : array) {
            list.add(t);
        }
        return list;
    }

    public static void addTooltip(List<Text> tooltip, String id, Formatting formatting, int lines) {
        tooltip.add(Text.translatable("tooltip.soulsweapons." + id).formatted(formatting));
        for (int i = 1; i <= lines; i++) {
            tooltip.add(Text.translatable("tooltip.soulsweapons." + id + "_description_" + i).formatted(Formatting.GRAY));
        }
    }

    public static void addAbilityTooltip(TooltipAbilities ability, ItemStack stack, List<Text> tooltip) {
        switch (ability) {
            case TRICK_WEAPON -> {
                if (stack.getItem() instanceof TrickWeapon weapon) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon").formatted(Formatting.WHITE));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon_description_1").formatted(Formatting.GRAY));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon_description_2").formatted(Formatting.DARK_GRAY)
                            .append(KeyBindRegistry.switchWeapon.getBoundKeyLocalizedText()));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon_description_3").formatted(Formatting.DARK_GRAY)
                            .append(getSwitchWeaponName(stack, weapon).copy().formatted(Formatting.WHITE)));
                }
            }
            case CHARGE -> {
                String current = MathHelper.floor((float) getCharge(stack) / (float) ConfigConstructor.holy_moonlight_ability_charge_needed * 100) + "%";
                tooltip.add(Text.translatable("tooltip.soulsweapons.charge").formatted(Formatting.DARK_AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.charge_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.charge_description_2").formatted(Formatting.DARK_GRAY).append(Text.literal(current + " | " + getAddedCharge(stack)).formatted(Formatting.AQUA)));
            }
            case NEED_CHARGE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.need_charge").formatted(Formatting.RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.need_charge_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.need_charge_description_2").formatted(Formatting.GRAY));
            }
            case RIGHTEOUS -> {
                int amount = MathHelper.floor(EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack) + ConfigConstructor.righteous_undead_bonus_damage);
                tooltip.add(Text.translatable("tooltip.soulsweapons.righteous").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.righteous_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.righteous_description_2").formatted(Formatting.DARK_GRAY).append(Text.literal(String.valueOf(amount))));
            }
            case MOONFALL -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonfall").formatted(Formatting.AQUA, Formatting.BOLD));
                for (int i = 1; i <= 3; i++) tooltip.add(Text.translatable("tooltip.soulsweapons.moonfall_description_" + i).formatted(Formatting.GRAY));
            }
            case HEAVY -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.heavy_weapon").formatted(Formatting.RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.heavy_weapon_description").formatted(Formatting.GRAY));
            }
            case LIFE_STEAL -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.life_steal").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.life_steal_description").formatted(Formatting.GRAY));
            }
            case OMNIVAMP -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.omnivamp").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.omnivamp_description").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.life_steal_description").formatted(Formatting.GRAY));
            }
            case OVERHEAL -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.overheal").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.overheal_description").formatted(Formatting.GRAY));
            }
            case SWORD_SLAM -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.sword_slam").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.sword_slam_description").formatted(Formatting.GRAY));
            }
            case SKYFALL -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.grand_skyfall").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.grand_skyfall_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.grand_skyfall_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.grand_skyfall_description_3").formatted(Formatting.GRAY));
            }
            case INFINITY -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.infinity").formatted(Formatting.WHITE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.infinity_description").formatted(Formatting.GRAY));
            }
            case CRIT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.crit").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.crit_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.crit_description_2").formatted(Formatting.GRAY));
            }
            case DOOM -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.doom").formatted(Formatting.RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.doom_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.doom_description_2").formatted(Formatting.GRAY));
            }
            case BLAZING_BLADE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.blazing_blade").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.blazing_blade_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.blazing_blade_description_2").formatted(Formatting.GRAY));
            }
            case TRANSFORMATION -> {
                if (stack.isOf(WeaponRegistry.DARKIN_SCYTHE_PRE)) {
                    DarkinScythePre scythe = (DarkinScythePre) stack.getItem();
                    tooltip.add(Text.translatable("tooltip.soulsweapons.transformation").formatted(Formatting.LIGHT_PURPLE));
                    for (int i = 1; i <= 8; i++) {
                        tooltip.add(Text.translatable("tooltip.soulsweapons.transformation_description_" + i).formatted(Formatting.GRAY));
                    }
                    tooltip.add(Text.literal(MathHelper.floor(((float)scythe.getSouls(stack)/ scythe.MAX_SOULS)*100) + "%").formatted(scythe.getDominantType(stack).equals(DarkinScythePre.SoulType.BLUE) ? Formatting.AQUA : Formatting.RED));
                }
            }
            case UMBRAL_TRESPASS -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.umbral_trespass").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.umbral_trespass_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.umbral_trespass_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.umbral_trespass_description_3").formatted(Formatting.GRAY));
            }
            case DAWNBREAKER -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.meridias_retribution").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.meridias_retribution_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.meridias_retribution_description_2").formatted(Formatting.GRAY));
            }
            case RAGE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.rage").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.rage_description").formatted(Formatting.GRAY));
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case LIGHTNING_CALL -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.lightning").formatted(Formatting.YELLOW));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lightning_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lightning_description_2").formatted(Formatting.GRAY));
            }
            case STORM_STOMP -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.storm_stomp").formatted(Formatting.WHITE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.storm_stomp_description").formatted(Formatting.GRAY));
            }
            case WEATHERBORN -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.weatherborn").formatted(Formatting.DARK_AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.weatherborn_description").formatted(Formatting.GRAY));
            }
            case DRAGON_STAFF -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.dragon_staff").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.dragon_staff_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.dragon_staff_description_2").formatted(Formatting.GRAY));
            }
            case VENGEFUL_FOG -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.vengeful_fog").formatted(Formatting.LIGHT_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.vengeful_fog_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.vengeful_fog_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.vengeful_fog_description_3").formatted(Formatting.GRAY));
            }
            case NIGHT_PROWLER -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.night_prowler").formatted(Formatting.DARK_AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.night_prowler_description").formatted(Formatting.GRAY));
            }
            case DETONATE_SPEARS -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.detonate_spears").formatted(Formatting.RED));
                for (int i = 1; i <= 5; i++) {
                    if (i == 3) tooltip.add(Text.translatable("tooltip.soulsweapons.detonate_spears_description_" + i).append(Text.literal(String.valueOf(
                            ConfigConstructor.draupnir_spear_detonate_power + ((float) EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack) / 2.5f)))
                            .formatted(Formatting.WHITE)).formatted(Formatting.GRAY));
                    else tooltip.add(Text.translatable("tooltip.soulsweapons.detonate_spears_description_" + i).formatted(Formatting.GRAY));
                }
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case FEATHERLIGHT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.featherlight").formatted(Formatting.LIGHT_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.featherlight_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.featherlight_description_2").formatted(Formatting.GRAY));
            }
            case SOUL_TRAP -> {
                String kills = "0";
                if (stack.hasNbt() && stack.getNbt().contains(KILLS)) {
                    kills = String.valueOf(stack.getNbt().getInt(KILLS));
                }
                tooltip.add(Text.translatable("tooltip.soulsweapons.soul_trap").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.soul_trap_description").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.soul_trap_kills").formatted(Formatting.DARK_AQUA).append(Text.literal(kills).formatted(Formatting.WHITE)));
            }
            case SOUL_RELEASE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.soul_release").formatted(Formatting.DARK_BLUE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.soul_release_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.soul_release_description_2").formatted(Formatting.GRAY));
            }
            case SOUL_RELEASE_WITHER -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.soul_release_wither").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.soul_release_wither_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.soul_release_wither_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.soul_release_wither_description_3").formatted(Formatting.GRAY));
            }
            case COLLECT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.collect_1").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
                tooltip.add(Text.translatable("tooltip.soulsweapons.collect_2").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
                tooltip.add(Text.translatable("tooltip.soulsweapons.collect_3").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
            }
            case SUMMON_WEAPON -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_4").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_5").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_1").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_2").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_3").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_4").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_5").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
            }
            case GALEFORCE -> {
                WeaponUtil.addTooltip(tooltip, "galeforce", Formatting.AQUA, 6);
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case FURY -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.fury").formatted(Formatting.RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.fury_description").formatted(Formatting.GRAY));
            }
            case HASTE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.haste").formatted(Formatting.YELLOW));
                tooltip.add(Text.translatable("tooltip.soulsweapons.haste_description").formatted(Formatting.GRAY));
            }
            case FLAME_ENRAGED -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.flame_enraged").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.flame_enraged_description").formatted(Formatting.GRAY));
            }
            case RETURNING -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.returning").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.returning_description").formatted(Formatting.GRAY));
            }
            case HEAVY_THROW -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.heavy_throw").formatted(Formatting.WHITE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.heavy_throw_description").formatted(Formatting.GRAY));
            }
            case PERMAFROST -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.permafrost").formatted(Formatting.DARK_AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.permafrost_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.permafrost_description_2").formatted(Formatting.GRAY));
            }
            case FREEZE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.freeze").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freeze_description").formatted(Formatting.GRAY));
            }
            case MAGIC_DAMAGE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.magic_damage").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.magic_damage_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.magic_damage_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.magic_damage_description_3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.magic_damage_description_4").formatted(Formatting.GRAY).append(Text.literal(
                        String.valueOf(ConfigConstructor.lich_bane_bonus_magic_damage + EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack))).formatted(Formatting.DARK_AQUA)));
            }
            case MJOLNIR_LIGHTNING -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.lightning").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.mjolnir_lightning_call_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.mjolnir_lightning_call_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.mjolnir_lightning_call_description_3").formatted(Formatting.GRAY));
            }
            case OFF_HAND_FLIGHT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.off_hand_flight").formatted(Formatting.WHITE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.off_hand_flight_description").formatted(Formatting.GRAY));
            }
            case THROW_LIGHTNING -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.mjolnir_throw").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.mjolnir_throw_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.mjolnir_throw_description_2").formatted(Formatting.GRAY));
            }
            case MOONLIGHT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight_description").formatted(Formatting.GRAY));
            }
            case MOONLIGHT_ATTACK -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight_attack_description").formatted(Formatting.GRAY));
            }
            case LUNAR_HERALD -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald_description_2").formatted(Formatting.GRAY));
            }
            case SUMMON_GHOST -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.ghost_summoner").formatted(Formatting.DARK_AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.ghost_summoner_description").formatted(Formatting.GRAY));
            }
            case SHIELD -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.shield").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.shield_description").formatted(Formatting.GRAY));
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case OBLITERATE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.obliterate").formatted(Formatting.DARK_BLUE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.obliterate_description").formatted(Formatting.GRAY));
            }
            case TRIPLE_MOONLIGHT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.pure_moonlight_description").formatted(Formatting.GRAY));
            }
            case SHADOW_STEP -> {
                String seconds = String.valueOf(MathHelper.floor((float) ShadowAssassinScythe.TICKS_FOR_BONUS / 20f));
                tooltip.add(Text.translatable("tooltip.soulsweapons.early_combat").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.early_combat_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.early_combat_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.early_combat_description_3").formatted(Formatting.DARK_GRAY, Formatting.ITALIC).append(Text.literal(seconds).formatted(Formatting.AQUA)));
            }
            case DISABLE_HEAL -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.disable_heal").formatted(Formatting.BOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.disable_heal_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.disable_heal_description_2").formatted(Formatting.GRAY));
            }
            case SHARPEN -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.sharpen").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.sharpen_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.sharpen_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.sharpen_description_3").formatted(Formatting.GRAY));
            }
            case IS_SHARPENED -> {
                if (Skofnung.isEmpowered(stack)) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.empowered").formatted(Formatting.AQUA));
                    tooltip.add(Text.literal(Skofnung.empAttacksLeft(stack) + "/8").formatted(Formatting.AQUA));
                }
            }
            case DISABLE_DEBUFS -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.skofnung_stone").formatted(Formatting.LIGHT_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.skofnung_stone_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.skofnung_stone_description_2").formatted(Formatting.GRAY));
            }
            case LUMINATE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.luminate").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.luminate_description").formatted(Formatting.GRAY));
            }
            case SPIDERS_BANE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.bane_of_arthropods").formatted(Formatting.WHITE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.bane_of_arthropods_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.bane_of_arthropods_description_2").formatted(Formatting.DARK_GRAY).append(Text.literal(String.valueOf(ConfigConstructor.sting_bonus_arthropod_damage)).formatted(Formatting.AQUA)));
            }
            case SAWBLADE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.sawblade").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.sawblade_description").formatted(Formatting.GRAY));
            }
            case WABBAJACK -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.wabbajack").formatted(Formatting.DARK_RED, Formatting.OBFUSCATED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.wabbajack_description").formatted(Formatting.GRAY));
            }
            case LUCK_BASED -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.lucky").formatted(Formatting.DARK_GREEN));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lucky_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lucky_description_2").formatted(Formatting.GRAY));
            }
            case PARRY -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry_description_3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry_description_4").formatted(Formatting.DARK_GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry_description_5").formatted(Formatting.DARK_GRAY));
            }
            case SKYWARD_STRIKES -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.skyward_strikes").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.skyward_strikes_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.skyward_strikes_description_2").formatted(Formatting.GRAY));
            }
            case KEYBIND_ABILITY -> tooltip.add(Text.translatable("tooltip.soulsweapons.keybind_ability").formatted(Formatting.DARK_GRAY)
                    .append(KeyBindRegistry.keybindAbility.getBoundKeyLocalizedText()));
            case NIGHTS_EDGE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.nights_edge").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.nights_edge_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.nights_edge_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.nights_edge_description_3").formatted(Formatting.GRAY));
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
        }
    }

    public enum TooltipAbilities {
        TRICK_WEAPON, CHARGE, NEED_CHARGE, RIGHTEOUS, MOONFALL, HEAVY, LIFE_STEAL, OMNIVAMP, OVERHEAL, SWORD_SLAM,
        SKYFALL, INFINITY, CRIT, DOOM, BLAZING_BLADE, TRANSFORMATION, UMBRAL_TRESPASS, DAWNBREAKER, RAGE, LIGHTNING_CALL,
        STORM_STOMP, WEATHERBORN, DRAGON_STAFF, VENGEFUL_FOG, NIGHT_PROWLER, DETONATE_SPEARS, FEATHERLIGHT, SOUL_TRAP,
        SOUL_RELEASE, SOUL_RELEASE_WITHER, COLLECT, SUMMON_WEAPON, GALEFORCE, FURY, HASTE, FLAME_ENRAGED, RETURNING,
        HEAVY_THROW, PERMAFROST, FREEZE, MAGIC_DAMAGE, MJOLNIR_LIGHTNING, OFF_HAND_FLIGHT, THROW_LIGHTNING, MOONLIGHT,
        MOONLIGHT_ATTACK, LUNAR_HERALD, SUMMON_GHOST, SHIELD, OBLITERATE, TRIPLE_MOONLIGHT, SHADOW_STEP, DISABLE_HEAL,
        SHARPEN, IS_SHARPENED, DISABLE_DEBUFS, LUMINATE, SPIDERS_BANE, SAWBLADE, WABBAJACK, LUCK_BASED, PARRY, SKYWARD_STRIKES,
        KEYBIND_ABILITY, NIGHTS_EDGE
    }
}
