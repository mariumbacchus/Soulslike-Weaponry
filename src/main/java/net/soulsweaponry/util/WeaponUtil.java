package net.soulsweaponry.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.*;
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
                int currentCharge = stack.getNbt().contains(CHARGE) ? stack.getNbt().getInt(CHARGE) : 0;
                int newCharge = currentCharge + amount + WeaponUtil.getEnchantDamageBonus(stack);
                int maxCharge = ConfigConstructor.holy_moonlight_ability_charge_needed;
                stack.getNbt().putInt(CHARGE, Math.min(newCharge, maxCharge));
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
        boolean sword = stack.isOf(WeaponRegistry.HOLY_MOONLIGHT_SWORD);
        int base = sword ? ConfigConstructor.holy_moonlight_sword_charge_added_post_hit : ConfigConstructor.holy_moonlight_greatsword_charge_added_post_hit;
        return (base + WeaponUtil.getEnchantDamageBonus(stack)) * (sword ? 1 : 2);
    }

    public static List<Integer> arrayToList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int t : array) {
            list.add(t);
        }
        return list;
    }

    public static void addTooltip(List<Text> tooltip, String id, Formatting formatting, int lines) {
        tooltip.add(new TranslatableText("tooltip.soulsweapons." + id).formatted(formatting));
        for (int i = 1; i <= lines; i++) {
            tooltip.add(new TranslatableText("tooltip.soulsweapons." + id + "_description_" + i).formatted(Formatting.GRAY));
        }
    }

    public static void addAbilityTooltip(TooltipAbilities ability, ItemStack stack, List<Text> tooltip) {
        switch (ability) {
            case TRICK_WEAPON -> {
                if (stack.getItem() instanceof TrickWeapon weapon) {
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.trick_weapon").formatted(Formatting.WHITE));
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.trick_weapon_description_1").formatted(Formatting.GRAY));
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.trick_weapon_description_2").formatted(Formatting.DARK_GRAY)
                            .append(KeyBindRegistry.switchWeapon.getBoundKeyLocalizedText()));
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.trick_weapon_description_3").formatted(Formatting.DARK_GRAY)
                            .append(getSwitchWeaponName(stack, weapon).copy().formatted(Formatting.WHITE)));
                }
            }
            case CHARGE -> {
                String current = MathHelper.floor((float) getCharge(stack) / (float) ConfigConstructor.holy_moonlight_ability_charge_needed * 100) + "%";
                tooltip.add(new TranslatableText("tooltip.soulsweapons.charge").formatted(Formatting.DARK_AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.charge_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.charge_description_2").formatted(Formatting.DARK_GRAY).append(new LiteralText(current + " | " + getAddedCharge(stack)).formatted(Formatting.AQUA)));
            }
            case CHARGE_BONUS_DAMAGE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.charge_bonus_damage").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.charge_bonus_damage_1").formatted(Formatting.GRAY));
            }
            case NEED_CHARGE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.need_charge").formatted(Formatting.RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.need_charge_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.need_charge_description_2").formatted(Formatting.GRAY));
            }
            case LUNAR_HERALD_NO_CHARGE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.lunar_herald_no_charge_1").formatted(Formatting.DARK_GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.lunar_herald_no_charge_2").formatted(Formatting.DARK_GRAY));
            }
            case RIGHTEOUS -> {
                int amount = MathHelper.floor(EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack) + ConfigConstructor.righteous_undead_bonus_damage);
                tooltip.add(new TranslatableText("tooltip.soulsweapons.righteous").formatted(Formatting.GOLD));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.righteous_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.righteous_description_2").formatted(Formatting.DARK_GRAY).append(new LiteralText(String.valueOf(amount))));
            }
            case MOONFALL -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.moonfall").formatted(Formatting.AQUA, Formatting.BOLD));
                for (int i = 1; i <= 3; i++) tooltip.add(new TranslatableText("tooltip.soulsweapons.moonfall_description_" + i).formatted(Formatting.GRAY));
            }
            case HEAVY -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.heavy_weapon").formatted(Formatting.RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.heavy_weapon_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.heavy_weapon_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.heavy_weapon_description_3").formatted(Formatting.GRAY));
            }
            case LIFE_STEAL -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.life_steal").formatted(Formatting.DARK_RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.life_steal_description").formatted(Formatting.GRAY));
            }
            case OMNIVAMP -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.omnivamp").formatted(Formatting.DARK_RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.omnivamp_description").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.life_steal_description").formatted(Formatting.GRAY));
            }
            case OVERHEAL -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.overheal").formatted(Formatting.GOLD));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.overheal_description").formatted(Formatting.GRAY));
            }
            case SWORD_SLAM -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.sword_slam").formatted(Formatting.DARK_PURPLE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.sword_slam_description").formatted(Formatting.GRAY));
            }
            case SKYFALL -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.grand_skyfall").formatted(Formatting.GOLD));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.grand_skyfall_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.grand_skyfall_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.grand_skyfall_description_3").formatted(Formatting.GRAY));
            }
            case INFINITY -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.infinity").formatted(Formatting.WHITE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.infinity_description").formatted(Formatting.GRAY));
            }
            case CRIT -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.crit").formatted(Formatting.DARK_RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.crit_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.crit_description_2").formatted(Formatting.GRAY));
            }
            case DOOM -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.doom").formatted(Formatting.RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.doom_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.doom_description_2").formatted(Formatting.GRAY));
            }
            case BLAZING_BLADE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.blazing_blade").formatted(Formatting.GOLD));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.blazing_blade_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.blazing_blade_description_2").formatted(Formatting.GRAY));
            }
            case TRANSFORMATION -> {
                if (stack.isOf(WeaponRegistry.DARKIN_SCYTHE_PRE)) {
                    DarkinScythePre scythe = (DarkinScythePre) stack.getItem();
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.transformation").formatted(Formatting.LIGHT_PURPLE));
                    for (int i = 1; i <= 8; i++) {
                        tooltip.add(new TranslatableText("tooltip.soulsweapons.transformation_description_" + i).formatted(Formatting.GRAY));
                    }
                    tooltip.add(new LiteralText(MathHelper.floor(((float)scythe.getSouls(stack)/ scythe.MAX_SOULS)*100) + "%").formatted(scythe.getDominantType(stack).equals(DarkinScythePre.SoulType.BLUE) ? Formatting.AQUA : Formatting.RED));
                }
            }
            case UMBRAL_TRESPASS -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.umbral_trespass").formatted(Formatting.DARK_RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.umbral_trespass_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.umbral_trespass_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.umbral_trespass_description_3").formatted(Formatting.GRAY));
            }
            case DAWNBREAKER -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.meridias_retribution").formatted(Formatting.DARK_PURPLE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.meridias_retribution_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.meridias_retribution_description_2").formatted(Formatting.GRAY));
            }
            case RAGE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.rage").formatted(Formatting.DARK_RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.rage_description").formatted(Formatting.GRAY));
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case LIGHTNING_CALL -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.lightning").formatted(Formatting.YELLOW));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.lightning_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.lightning_description_2").formatted(Formatting.GRAY));
            }
            case STORM_STOMP -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.storm_stomp").formatted(Formatting.WHITE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.storm_stomp_description").formatted(Formatting.GRAY));
            }
            case WEATHERBORN -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.weatherborn").formatted(Formatting.DARK_AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.weatherborn_description").formatted(Formatting.GRAY));
            }
            case DRAGON_STAFF -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.dragon_staff").formatted(Formatting.DARK_PURPLE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.dragon_staff_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.dragon_staff_description_2").formatted(Formatting.GRAY));
            }
            case VENGEFUL_FOG -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.vengeful_fog").formatted(Formatting.LIGHT_PURPLE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.vengeful_fog_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.vengeful_fog_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.vengeful_fog_description_3").formatted(Formatting.GRAY));
            }
            case NIGHT_PROWLER -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.night_prowler").formatted(Formatting.DARK_AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.night_prowler_description").formatted(Formatting.GRAY));
            }
            case DETONATE_SPEARS -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.detonate_spears").formatted(Formatting.RED));
                for (int i = 1; i <= 5; i++) {
                    if (i == 3) tooltip.add(new TranslatableText("tooltip.soulsweapons.detonate_spears_description_" + i).append(new LiteralText(String.valueOf(
                            ConfigConstructor.draupnir_spear_detonate_power + ((float) EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack) / 2.5f)))
                            .formatted(Formatting.WHITE)).formatted(Formatting.GRAY));
                    else tooltip.add(new TranslatableText("tooltip.soulsweapons.detonate_spears_description_" + i).formatted(Formatting.GRAY));
                }
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case FEATHERLIGHT -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.featherlight").formatted(Formatting.LIGHT_PURPLE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.featherlight_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.featherlight_description_2").formatted(Formatting.GRAY));
            }
            case SOUL_TRAP -> {
                String kills = "0";
                if (stack.hasNbt() && stack.getNbt().contains(KILLS)) {
                    kills = String.valueOf(stack.getNbt().getInt(KILLS));
                }
                tooltip.add(new TranslatableText("tooltip.soulsweapons.soul_trap").formatted(Formatting.DARK_PURPLE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.soul_trap_description").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.soul_trap_kills").formatted(Formatting.DARK_AQUA).append(new LiteralText(kills).formatted(Formatting.WHITE)));
            }
            case SOUL_RELEASE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.soul_release").formatted(Formatting.DARK_BLUE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.soul_release_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.soul_release_description_2").formatted(Formatting.GRAY));
            }
            case SOUL_RELEASE_WITHER -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.soul_release_wither").formatted(Formatting.DARK_RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.soul_release_wither_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.soul_release_wither_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.soul_release_wither_description_3").formatted(Formatting.GRAY));
            }
            case COLLECT -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.collect_1").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.collect_2").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.collect_3").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
            }
            case SUMMON_WEAPON -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freyr_sword").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freyr_sword_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freyr_sword_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freyr_sword_description_3").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freyr_sword_description_4").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freyr_sword_description_5").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freyr_sword_note_1").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freyr_sword_note_2").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freyr_sword_note_3").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freyr_sword_note_4").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freyr_sword_note_5").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
            }
            case GALEFORCE -> {
                WeaponUtil.addTooltip(tooltip, "galeforce", Formatting.AQUA, 6);
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case FURY -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.fury").formatted(Formatting.RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.fury_description").formatted(Formatting.GRAY));
            }
            case HASTE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.haste").formatted(Formatting.YELLOW));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.haste_description").formatted(Formatting.GRAY));
            }
            case FLAME_ENRAGED -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.flame_enraged").formatted(Formatting.GOLD));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.flame_enraged_description").formatted(Formatting.GRAY));
            }
            case RETURNING -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.returning").formatted(Formatting.DARK_PURPLE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.returning_description").formatted(Formatting.GRAY));
            }
            case HEAVY_THROW -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.heavy_throw").formatted(Formatting.WHITE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.heavy_throw_description").formatted(Formatting.GRAY));
            }
            case PERMAFROST -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.permafrost").formatted(Formatting.DARK_AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.permafrost_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.permafrost_description_2").formatted(Formatting.GRAY));
            }
            case FREEZE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freeze").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.freeze_description").formatted(Formatting.GRAY));
            }
            case MAGIC_DAMAGE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.magic_damage").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.magic_damage_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.magic_damage_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.magic_damage_description_3").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.magic_damage_description_4").formatted(Formatting.GRAY).append(new LiteralText(
                        String.valueOf(ConfigConstructor.lich_bane_bonus_magic_damage + EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack))).formatted(Formatting.DARK_AQUA)));
            }
            case MJOLNIR_LIGHTNING -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.lightning").formatted(Formatting.GOLD));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.mjolnir_lightning_call_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.mjolnir_lightning_call_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.mjolnir_lightning_call_description_3").formatted(Formatting.GRAY));
            }
            case OFF_HAND_FLIGHT -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.off_hand_flight").formatted(Formatting.WHITE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.off_hand_flight_description").formatted(Formatting.GRAY));
            }
            case THROW_LIGHTNING -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.mjolnir_throw").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.mjolnir_throw_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.mjolnir_throw_description_2").formatted(Formatting.GRAY));
            }
            case MOONLIGHT -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.moonlight").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.moonlight_description").formatted(Formatting.GRAY));
            }
            case MOONLIGHT_ATTACK -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.moonlight").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.moonlight_attack_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.moonlight_attack_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.moonlight_attack_description_3").formatted(Formatting.GRAY));
            }
            case LUNAR_HERALD -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.lunar_herald").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.lunar_herald_description_1").formatted(Formatting.GRAY)
                        .append(KeyBindRegistry.effectShootMoonlight.getBoundKeyLocalizedText()));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.lunar_herald_description_2").formatted(Formatting.GRAY));
            }
            case SUMMON_GHOST -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.ghost_summoner").formatted(Formatting.DARK_AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.ghost_summoner_description").formatted(Formatting.GRAY));
            }
            case SHIELD -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.shield").formatted(Formatting.DARK_PURPLE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.shield_description").formatted(Formatting.GRAY));
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case OBLITERATE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.obliterate").formatted(Formatting.DARK_BLUE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.obliterate_description").formatted(Formatting.GRAY));
            }
            case TRIPLE_MOONLIGHT -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.moonlight").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.pure_moonlight_description").formatted(Formatting.GRAY));
            }
            case SHADOW_STEP -> {
                String seconds = String.valueOf(MathHelper.floor((float) ShadowAssassinScythe.TICKS_FOR_BONUS / 20f));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.early_combat").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.early_combat_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.early_combat_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.early_combat_description_3").formatted(Formatting.DARK_GRAY, Formatting.ITALIC).append(new LiteralText(seconds).formatted(Formatting.AQUA)));
            }
            case DISABLE_HEAL -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.disable_heal").formatted(Formatting.BOLD));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.disable_heal_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.disable_heal_description_2").formatted(Formatting.GRAY));
            }
            case SHARPEN -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.sharpen").formatted(Formatting.GOLD));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.sharpen_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.sharpen_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.sharpen_description_3").formatted(Formatting.GRAY));
            }
            case IS_SHARPENED -> {
                if (Skofnung.isEmpowered(stack)) {
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.empowered").formatted(Formatting.AQUA).append(new LiteralText(Skofnung.empAttacksLeft(stack) + "/8").formatted(Formatting.AQUA)));
                }
            }
            case DISABLE_DEBUFS -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.skofnung_stone").formatted(Formatting.LIGHT_PURPLE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.skofnung_stone_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.skofnung_stone_description_2").formatted(Formatting.GRAY));
            }
            case LUMINATE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.luminate").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.luminate_description").formatted(Formatting.GRAY));
            }
            case SPIDERS_BANE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.bane_of_arthropods").formatted(Formatting.WHITE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.bane_of_arthropods_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.bane_of_arthropods_description_2").formatted(Formatting.DARK_GRAY).append(new LiteralText(String.valueOf(ConfigConstructor.sting_bonus_arthropod_damage)).formatted(Formatting.AQUA)));
            }
            case SAWBLADE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.sawblade").formatted(Formatting.DARK_RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.sawblade_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.sawblade_description_2").formatted(Formatting.GRAY));
            }
            case WABBAJACK -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.wabbajack").formatted(Formatting.DARK_RED, Formatting.OBFUSCATED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.wabbajack_description").formatted(Formatting.GRAY));
            }
            case LUCK_BASED -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.lucky").formatted(Formatting.DARK_GREEN));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.lucky_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.lucky_description_2").formatted(Formatting.GRAY));
            }
            case PARRY -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.parry").formatted(Formatting.GOLD));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.parry_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.parry_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.parry_description_3").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.parry_description_4").formatted(Formatting.DARK_GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.parry_description_5").formatted(Formatting.DARK_GRAY));
            }
            case SKYWARD_STRIKES -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.skyward_strikes").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.skyward_strikes_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.skyward_strikes_description_2").formatted(Formatting.GRAY));
            }
            case KEYBIND_ABILITY -> tooltip.add(new TranslatableText("tooltip.soulsweapons.keybind_ability").formatted(Formatting.DARK_GRAY)
                    .append(KeyBindRegistry.keybindAbility.getBoundKeyLocalizedText()));
            case NIGHTS_EDGE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.nights_edge").formatted(Formatting.DARK_PURPLE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.nights_edge_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.nights_edge_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.nights_edge_description_3").formatted(Formatting.GRAY));
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case CHAOS_STORM -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_storm").formatted(Formatting.RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_storm_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_storm_description_2").formatted(Formatting.GRAY));
            }
            case VEIL_OF_FIRE -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.veil_of_fire").formatted(Formatting.GOLD).formatted(Formatting.BOLD));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.veil_of_fire_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.veil_of_fire_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.veil_of_fire_description_3").formatted(Formatting.GRAY));
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case BLIGHT -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.blight").formatted(Formatting.LIGHT_PURPLE).formatted(Formatting.BOLD));
                for (int i = 1; i <= 5; i++) {
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.blight_description_" + i).formatted(Formatting.GRAY));
                }
                tooltip.add(new TranslatableText("tooltip.soulsweapons.blight_description_6").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
            }
            case FAST_PULL -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.fast_pull").formatted(Formatting.WHITE));
                if (stack.getItem() instanceof ModdedBow bow) {
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.fast_pull_2").formatted(Formatting.GRAY).append(new LiteralText(String.valueOf(bow.getReducedPullTime()))));
                } else {
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.fast_pull_1").formatted(Formatting.GRAY));
                }
            }
            case SLOW_PULL -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.slow_pull").formatted(Formatting.RED));
                if (stack.getItem() instanceof ModdedBow bow) {
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.slow_pull_2").formatted(Formatting.GRAY).append(new LiteralText(String.valueOf(bow.getReducedPullTime()))));
                } else {
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.slow_pull_1").formatted(Formatting.GRAY));
                }
            }
            case THIRD_SHOT -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.third_shot").formatted(Formatting.GOLD));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.third_shot_1").formatted(Formatting.GRAY));
                tooltip.add(new LiteralText(String.valueOf(ConfigConstructor.kraken_slayer_bonus_true_damage + EnchantmentHelper.getLevel(Enchantments.POWER, stack)))
                        .formatted(Formatting.WHITE)
                        .append(new TranslatableText("tooltip.soulsweapons.third_shot_2").formatted(Formatting.GRAY)));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.third_shot_3").formatted(Formatting.GRAY)
                        .append(new LiteralText(MathHelper.floor((1f - ConfigConstructor.kraken_slayer_player_true_damage_taken_modifier) * 100) + "%"))
                        .formatted(Formatting.DARK_GRAY));
            }
            case MOONLIGHT_ARROW -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.moonlight_arrow").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.moonlight_arrow_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.moonlight_arrow_2").formatted(Formatting.GRAY));
            }
            case ARROW_STORM -> {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.arrow_storm").formatted(Formatting.DARK_PURPLE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.arrow_storm_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.arrow_storm_2").formatted(Formatting.GRAY));
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
        }
    }

    public enum TooltipAbilities {
        TRICK_WEAPON, CHARGE, CHARGE_BONUS_DAMAGE, NEED_CHARGE, LUNAR_HERALD_NO_CHARGE, RIGHTEOUS, MOONFALL, HEAVY, LIFE_STEAL, OMNIVAMP, OVERHEAL, SWORD_SLAM,
        SKYFALL, INFINITY, CRIT, DOOM, BLAZING_BLADE, TRANSFORMATION, UMBRAL_TRESPASS, DAWNBREAKER, RAGE, LIGHTNING_CALL,
        STORM_STOMP, WEATHERBORN, DRAGON_STAFF, VENGEFUL_FOG, NIGHT_PROWLER, DETONATE_SPEARS, FEATHERLIGHT, SOUL_TRAP,
        SOUL_RELEASE, SOUL_RELEASE_WITHER, COLLECT, SUMMON_WEAPON, GALEFORCE, FURY, HASTE, FLAME_ENRAGED, RETURNING,
        HEAVY_THROW, PERMAFROST, FREEZE, MAGIC_DAMAGE, MJOLNIR_LIGHTNING, OFF_HAND_FLIGHT, THROW_LIGHTNING, MOONLIGHT,
        MOONLIGHT_ATTACK, LUNAR_HERALD, SUMMON_GHOST, SHIELD, OBLITERATE, TRIPLE_MOONLIGHT, SHADOW_STEP, DISABLE_HEAL,
        SHARPEN, IS_SHARPENED, DISABLE_DEBUFS, LUMINATE, SPIDERS_BANE, SAWBLADE, WABBAJACK, LUCK_BASED, PARRY, SKYWARD_STRIKES,
        KEYBIND_ABILITY, NIGHTS_EDGE, CHAOS_STORM, VEIL_OF_FIRE, BLIGHT, FAST_PULL, THIRD_SHOT, SLOW_PULL, MOONLIGHT_ARROW,
        ARROW_STORM
    }
}
