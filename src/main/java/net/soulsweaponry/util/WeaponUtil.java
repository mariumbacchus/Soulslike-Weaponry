package net.soulsweaponry.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.soulsweaponry.config.CommonConfig;

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
//    public static final TrickWeapon[] TRICK_WEAPONS = {TODO add trick weapons
//            WeaponRegistry.KIRKHAMMER,
//            WeaponRegistry.SILVER_SWORD,
//            WeaponRegistry.HOLY_GREATSWORD,
//            WeaponRegistry.HOLY_MOONLIGHT_GREATSWORD,
//            WeaponRegistry.HOLY_MOONLIGHT_SWORD,
//    };

    /**
     * Returns level of the damage enchant, for example {@code 5} for Sharpness V or {@code 4} for Smite IV
     */
    public static int getEnchantDamageBonus(ItemStack stack) {
        for (Enchantment ench : DAMAGE_ENCHANTS) {
            if (EnchantmentHelper.getItemEnchantmentLevel(ench, stack) > 0) {
                return EnchantmentHelper.getItemEnchantmentLevel(ench, stack);
            }
        }
        return 0;
    }

//    public static Component getSwitchWeaponName(ItemStack stack, TrickWeapon weapon) {TODO trickweapon
//        TrickWeapon switchWeapon = TRICK_WEAPONS[weapon.getSwitchWeaponIndex()];
//        if (stack.hasTag() && stack.getTag().contains(WeaponUtil.PREV_TRICK_WEAPON)) {
//            switchWeapon = TRICK_WEAPONS[stack.getTag().getInt(WeaponUtil.PREV_TRICK_WEAPON)];
//        }
//        return switchWeapon.getName();
//    }

    public static void addCharge(ItemStack stack, int amount) {
        if (stack.hasTag()) {
            if (stack.getTag().contains(CHARGE)) {
                int add = stack.getTag().getInt(CHARGE) >= CommonConfig.HOLY_MOON_ABILITY_CHARGE_NEED.get() ?
                        CommonConfig.HOLY_MOON_ABILITY_CHARGE_NEED.get() : stack.getTag().getInt(CHARGE) + amount + WeaponUtil.getEnchantDamageBonus(stack);
                stack.getTag().putInt(CHARGE, add);
            } else {
                stack.getTag().putInt(CHARGE, 0);
            }
        }
    }

    public static boolean isCharged(ItemStack stack) {
        if (stack.hasTag()) {
            if (stack.getTag().contains(CHARGE)) {
                return stack.getTag().getInt(CHARGE) >= CommonConfig.HOLY_MOON_ABILITY_CHARGE_NEED.get();
            } else {
                stack.getTag().putInt(CHARGE, 0);
            }
        }
        return false;
    }

    public static int getCharge(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(CHARGE)) {
            return stack.getTag().getInt(CHARGE);
        }
        return 0;
    }

//    public static int getAddedCharge(ItemStack stack) {
//        return (CommonConfig.HOLY_MOON_ABILITY_CHARGE_ADDED.get() + WeaponUtil.getEnchantDamageBonus(stack)) * (stack.is(WeaponRegistry.HOLY_MOONLIGHT_SWORD) ? 3 : 1);
//    }TODO holy moonlight sword

    public static List<Integer> arrayToList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int t : array) {
            list.add(t);
        }
        return list;
    }

    public static void addTooltip(List<Component> tooltip, String id, ChatFormatting formatting, int lines) {
        tooltip.add(new TranslatableComponent("tooltip.soulsweapons." + id).withStyle(formatting));
        for (int i = 1; i <= lines; i++) {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons." + id + "_description_" + i).withStyle(ChatFormatting.GRAY));
        }
    }

    public static boolean isUndead(LivingEntity entity) {
        return entity.getMobType() == MobType.UNDEAD;
    }

    public static void addAbilityTooltip(TooltipAbilities ability, ItemStack stack, List<Component> tooltip) {
        switch (ability) {
            case TRICK_WEAPON -> {
//                if (stack.getItem() instanceof TrickWeapon weapon) {
//                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.trick_weapon").withStyle(ChatFormatting.WHITE));
//                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.trick_weapon_description_1").withStyle(ChatFormatting.GRAY));
//                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.trick_weapon_description_2").withStyle(ChatFormatting.DARK_GRAY)
//                            .append(KeyBindRegistry.switchWeapon.getBoundKeyLocalizedText()));TODO KeyBindRegistry
//                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.trick_weapon_description_3").withStyle(ChatFormatting.DARK_GRAY)
//                            .append(getSwitchWeaponName(stack, weapon).copy().withStyle(ChatFormatting.WHITE)));
//                }
            }
            case CHARGE -> {
                String current = Mth.floor((float) getCharge(stack) / (float) CommonConfig.HOLY_MOON_ABILITY_CHARGE_NEED.get() * 100) + "%";
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.charge").withStyle(ChatFormatting.DARK_AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.charge_description_1").withStyle(ChatFormatting.GRAY));
                //tooltip.add(new TranslatableComponent("tooltip.soulsweapons.charge_description_2").withStyle(ChatFormatting.DARK_GRAY).append(new TextComponent(current + " | " + getAddedCharge(stack)).withStyle(ChatFormatting.AQUA)));TODO holy moonlight sword
            }
            case NEED_CHARGE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.need_charge").withStyle(ChatFormatting.RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.need_charge_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.need_charge_description_2").withStyle(ChatFormatting.GRAY));
            }
            case RIGHTEOUS -> {
                int amount = Mth.floor(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack) + CommonConfig.RIGHTEOUS_UNDEAD_BONUS_DAMAGE.get());
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.righteous").withStyle(ChatFormatting.GOLD));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.righteous_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.righteous_description_2").withStyle(ChatFormatting.DARK_GRAY).append(new TextComponent(String.valueOf(amount))));
            }
            case MOONFALL -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.moonfall").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
                for (int i = 1; i <= 3; i++) tooltip.add(new TranslatableComponent("tooltip.soulsweapons.moonfall_description_" + i).withStyle(ChatFormatting.GRAY));
            }
            case HEAVY -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.heavy_weapon").withStyle(ChatFormatting.RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.heavy_weapon_description").withStyle(ChatFormatting.GRAY));
            }
            case LIFE_STEAL -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.life_steal").withStyle(ChatFormatting.DARK_RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.life_steal_description").withStyle(ChatFormatting.GRAY));
            }
            case OMNIVAMP -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.omnivamp").withStyle(ChatFormatting.DARK_RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.omnivamp_description").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.life_steal_description").withStyle(ChatFormatting.GRAY));
            }
            case OVERHEAL -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.overheal").withStyle(ChatFormatting.GOLD));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.overheal_description").withStyle(ChatFormatting.GRAY));
            }
            case SWORD_SLAM -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.sword_slam").withStyle(ChatFormatting.DARK_PURPLE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.sword_slam_description").withStyle(ChatFormatting.GRAY));
            }
            case SKYFALL -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.grand_skyfall").withStyle(ChatFormatting.GOLD));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.grand_skyfall_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.grand_skyfall_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.grand_skyfall_description_3").withStyle(ChatFormatting.GRAY));
            }
            case INFINITY -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.infinity").withStyle(ChatFormatting.WHITE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.infinity_description").withStyle(ChatFormatting.GRAY));
            }
            case CRIT -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.crit").withStyle(ChatFormatting.DARK_RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.crit_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.crit_description_2").withStyle(ChatFormatting.GRAY));
            }
            case DOOM -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.doom").withStyle(ChatFormatting.RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.doom_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.doom_description_2").withStyle(ChatFormatting.GRAY));
            }
            case BLAZING_BLADE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.blazing_blade").withStyle(ChatFormatting.GOLD));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.blazing_blade_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.blazing_blade_description_2").withStyle(ChatFormatting.GRAY));
            }
            case TRANSFORMATION -> {
//                if (stack.is(WeaponRegistry.DARKIN_SCYTHE_PRE)) {
//                    DarkinScythePre scythe = (DarkinScythePre) stack.getItem();
//                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.transformation").withStyle(ChatFormatting.LIGHT_PURPLE));
//                    for (int i = 1; i <= 8; i++) {
//                        tooltip.add(new TranslatableComponent("tooltip.soulsweapons.transformation_description_" + i).withStyle(ChatFormatting.GRAY));
//                    }
//                    tooltip.add(new TextComponent(Mth.floor(((float)scythe.getSouls(stack)/ scythe.MAX_SOULS)*100) + "%").withStyle(scythe.getDominantType(stack).equals(DarkinScythePre.SoulType.BLUE) ? ChatFormatting.AQUA : ChatFormatting.RED));
//                }TODO darkin scythe pre
            }
            case UMBRAL_TRESPASS -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.umbral_trespass").withStyle(ChatFormatting.DARK_RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.umbral_trespass_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.umbral_trespass_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.umbral_trespass_description_3").withStyle(ChatFormatting.GRAY));
            }
            case DAWNBREAKER -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.meridias_retribution").withStyle(ChatFormatting.DARK_PURPLE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.meridias_retribution_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.meridias_retribution_description_2").withStyle(ChatFormatting.GRAY));
            }
            case RAGE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.rage").withStyle(ChatFormatting.DARK_RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.rage_description").withStyle(ChatFormatting.GRAY));
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case LIGHTNING_CALL -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.lightning").withStyle(ChatFormatting.YELLOW));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.lightning_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.lightning_description_2").withStyle(ChatFormatting.GRAY));
            }
            case STORM_STOMP -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.storm_stomp").withStyle(ChatFormatting.WHITE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.storm_stomp_description").withStyle(ChatFormatting.GRAY));
            }
            case WEATHERBORN -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.weatherborn").withStyle(ChatFormatting.DARK_AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.weatherborn_description").withStyle(ChatFormatting.GRAY));
            }
            case DRAGON_STAFF -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.dragon_staff").withStyle(ChatFormatting.DARK_PURPLE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.dragon_staff_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.dragon_staff_description_2").withStyle(ChatFormatting.GRAY));
            }
            case VENGEFUL_FOG -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.vengeful_fog").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.vengeful_fog_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.vengeful_fog_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.vengeful_fog_description_3").withStyle(ChatFormatting.GRAY));
            }
            case NIGHT_PROWLER -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.night_prowler").withStyle(ChatFormatting.DARK_AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.night_prowler_description").withStyle(ChatFormatting.GRAY));
            }
            case DETONATE_SPEARS -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.detonate_spears").withStyle(ChatFormatting.RED));
                for (int i = 1; i <= 5; i++) {
                    if (i == 3) tooltip.add(new TranslatableComponent("tooltip.soulsweapons.detonate_spears_description_" + i).append(new TextComponent(String.valueOf(
                            CommonConfig.DRAUPNIR_DETONATE_POWER.get() + ((float) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, stack) / 2.5f)))
                            .withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY));
                    else tooltip.add(new TranslatableComponent("tooltip.soulsweapons.detonate_spears_description_" + i).withStyle(ChatFormatting.GRAY));
                }
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case FEATHERLIGHT -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.featherlight").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.featherlight_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.featherlight_description_2").withStyle(ChatFormatting.GRAY));
            }
            case SOUL_TRAP -> {
                String kills = "0";
                if (stack.hasTag() && stack.getTag().contains(KILLS)) {
                    kills = String.valueOf(stack.getTag().getInt(KILLS));
                }
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.soul_trap").withStyle(ChatFormatting.DARK_PURPLE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.soul_trap_description").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.soul_trap_kills").withStyle(ChatFormatting.DARK_AQUA).append(new TextComponent(kills).withStyle(ChatFormatting.WHITE)));
            }
            case SOUL_RELEASE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.soul_release").withStyle(ChatFormatting.DARK_BLUE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.soul_release_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.soul_release_description_2").withStyle(ChatFormatting.GRAY));
            }
            case SOUL_RELEASE_WITHER -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.soul_release_wither").withStyle(ChatFormatting.DARK_RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.soul_release_wither_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.soul_release_wither_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.soul_release_wither_description_3").withStyle(ChatFormatting.GRAY));
            }
            case COLLECT -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.collect_1").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.collect_2").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.collect_3").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
            }
            case SUMMON_WEAPON -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freyr_sword").withStyle(ChatFormatting.AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freyr_sword_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freyr_sword_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freyr_sword_description_3").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freyr_sword_description_4").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freyr_sword_description_5").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freyr_sword_note_1").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freyr_sword_note_2").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freyr_sword_note_3").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freyr_sword_note_4").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freyr_sword_note_5").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            }
            case GALEFORCE -> {
                WeaponUtil.addTooltip(tooltip, "galeforce", ChatFormatting.AQUA, 6);
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case FURY -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.fury").withStyle(ChatFormatting.RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.fury_description").withStyle(ChatFormatting.GRAY));
            }
            case HASTE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.haste").withStyle(ChatFormatting.YELLOW));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.haste_description").withStyle(ChatFormatting.GRAY));
            }
            case FLAME_ENRAGED -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.flame_enraged").withStyle(ChatFormatting.GOLD));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.flame_enraged_description").withStyle(ChatFormatting.GRAY));
            }
            case RETURNING -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.returning").withStyle(ChatFormatting.DARK_PURPLE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.returning_description").withStyle(ChatFormatting.GRAY));
            }
            case HEAVY_THROW -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.heavy_throw").withStyle(ChatFormatting.WHITE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.heavy_throw_description").withStyle(ChatFormatting.GRAY));
            }
            case PERMAFROST -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.permafrost").withStyle(ChatFormatting.DARK_AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.permafrost_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.permafrost_description_2").withStyle(ChatFormatting.GRAY));
            }
            case FREEZE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freeze").withStyle(ChatFormatting.AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.freeze_description").withStyle(ChatFormatting.GRAY));
            }
            case MAGIC_DAMAGE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.magic_damage").withStyle(ChatFormatting.AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.magic_damage_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.magic_damage_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.magic_damage_description_3").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.magic_damage_description_4").withStyle(ChatFormatting.GRAY).append(new TextComponent(
                        String.valueOf(CommonConfig.LICH_BANE_BONUS_MAGIC_DAMAGE.get() + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack))).withStyle(ChatFormatting.DARK_AQUA)));
            }
            case MJOLNIR_LIGHTNING -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.lightning").withStyle(ChatFormatting.GOLD));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.mjolnir_lightning_call_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.mjolnir_lightning_call_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.mjolnir_lightning_call_description_3").withStyle(ChatFormatting.GRAY));
            }
            case OFF_HAND_FLIGHT -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.off_hand_flight").withStyle(ChatFormatting.WHITE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.off_hand_flight_description").withStyle(ChatFormatting.GRAY));
            }
            case THROW_LIGHTNING -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.mjolnir_throw").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.mjolnir_throw_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.mjolnir_throw_description_2").withStyle(ChatFormatting.GRAY));
            }
            case MOONLIGHT -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.moonlight").withStyle(ChatFormatting.AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.moonlight_description").withStyle(ChatFormatting.GRAY));
            }
            case MOONLIGHT_ATTACK -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.moonlight").withStyle(ChatFormatting.AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.moonlight_attack_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.moonlight_attack_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.moonlight_attack_description_3").withStyle(ChatFormatting.GRAY));
            }
            case LUNAR_HERALD -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.lunar_herald").withStyle(ChatFormatting.AQUA));
//                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.lunar_herald_description_1").withStyle(ChatFormatting.GRAY)
//                        .append(KeyBindRegistry.effectShootMoonlight.getBoundKeyLocalizedText()));TODO keybindregistry
//                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.lunar_herald_description_2").withStyle(ChatFormatting.GRAY));
            }
            case SUMMON_GHOST -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.ghost_summoner").withStyle(ChatFormatting.DARK_AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.ghost_summoner_description").withStyle(ChatFormatting.GRAY));
            }
            case SHIELD -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shield").withStyle(ChatFormatting.DARK_PURPLE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shield_description").withStyle(ChatFormatting.GRAY));
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case OBLITERATE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.obliterate").withStyle(ChatFormatting.DARK_BLUE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.obliterate_description").withStyle(ChatFormatting.GRAY));
            }
            case TRIPLE_MOONLIGHT -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.moonlight").withStyle(ChatFormatting.AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.pure_moonlight_description").withStyle(ChatFormatting.GRAY));
            }
            case SHADOW_STEP -> {
//                String seconds = String.valueOf(Mth.floor((float) ShadowAssassinScythe.TICKS_FOR_BONUS / 20f));TODO add shadow assasin scythe
//                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.early_combat").withStyle(ChatFormatting.AQUA));
//                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.early_combat_description_1").withStyle(ChatFormatting.GRAY));
//                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.early_combat_description_2").withStyle(ChatFormatting.GRAY));
//                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.early_combat_description_3").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC).append(new TextComponent(seconds).withStyle(ChatFormatting.AQUA)));
            }
            case DISABLE_HEAL -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.disable_heal").withStyle(ChatFormatting.BOLD));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.disable_heal_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.disable_heal_description_2").withStyle(ChatFormatting.GRAY));
            }
            case SHARPEN -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.sharpen").withStyle(ChatFormatting.GOLD));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.sharpen_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.sharpen_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.sharpen_description_3").withStyle(ChatFormatting.GRAY));
            }
            case IS_SHARPENED -> {
//                if (Skofnung.isEmpowered(stack)) {
//                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.empowered").withStyle(ChatFormatting.AQUA).append(new TextComponent(Skofnung.empAttacksLeft(stack) + "/8").withStyle(ChatFormatting.AQUA)));
//                }TODO add skofnung
            }
            case DISABLE_DEBUFS -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.skofnung_stone").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.skofnung_stone_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.skofnung_stone_description_2").withStyle(ChatFormatting.GRAY));
            }
            case LUMINATE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.luminate").withStyle(ChatFormatting.AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.luminate_description").withStyle(ChatFormatting.GRAY));
            }
            case SPIDERS_BANE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.bane_of_arthropods").withStyle(ChatFormatting.WHITE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.bane_of_arthropods_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.bane_of_arthropods_description_2").withStyle(ChatFormatting.DARK_GRAY).append(new TextComponent(String.valueOf(CommonConfig.STING_BONUS_ARTHROPOD_DAMAGE.get())).withStyle(ChatFormatting.AQUA)));
            }
            case SAWBLADE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.sawblade").withStyle(ChatFormatting.DARK_RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.sawblade_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.sawblade_description_2").withStyle(ChatFormatting.GRAY));
            }
            case WABBAJACK -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.wabbajack").withStyle(ChatFormatting.DARK_RED, ChatFormatting.OBFUSCATED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.wabbajack_description").withStyle(ChatFormatting.GRAY));
            }
            case LUCK_BASED -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.lucky").withStyle(ChatFormatting.DARK_GREEN));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.lucky_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.lucky_description_2").withStyle(ChatFormatting.GRAY));
            }
            case PARRY -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.parry").withStyle(ChatFormatting.GOLD));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.parry_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.parry_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.parry_description_3").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.parry_description_4").withStyle(ChatFormatting.DARK_GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.parry_description_5").withStyle(ChatFormatting.DARK_GRAY));
            }
            case SKYWARD_STRIKES -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.skyward_strikes").withStyle(ChatFormatting.AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.skyward_strikes_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.skyward_strikes_description_2").withStyle(ChatFormatting.GRAY));
            }
            case KEYBIND_ABILITY -> tooltip.add(new TranslatableComponent("tooltip.soulsweapons.keybind_ability").withStyle(ChatFormatting.DARK_GRAY));
                    //.append(KeyBindRegistry.keybindAbility.getBoundKeyLocalizedText()));TODO Keybindregistry
            case NIGHTS_EDGE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.nights_edge").withStyle(ChatFormatting.DARK_PURPLE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.nights_edge_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.nights_edge_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.nights_edge_description_3").withStyle(ChatFormatting.GRAY));
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case CHAOS_STORM -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_storm").withStyle(ChatFormatting.RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_storm_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_storm_description_2").withStyle(ChatFormatting.GRAY));
            }
            case VEIL_OF_FIRE -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.veil_of_fire").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.veil_of_fire_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.veil_of_fire_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.veil_of_fire_description_3").withStyle(ChatFormatting.GRAY));
                WeaponUtil.addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case BLIGHT -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.blight").withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.BOLD));
                for (int i = 1; i <= 5; i++) {
                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.blight_description_" + i).withStyle(ChatFormatting.GRAY));
                }
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.blight_description_6").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
            }
            case FAST_PULL -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.fast_pull").withStyle(ChatFormatting.WHITE));
//                if (stack.getItem() instanceof ModdedBow bow) {TODO modded bow
//                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.fast_pull_2").withStyle(ChatFormatting.GRAY).append(new TextComponent(String.valueOf(bow.getReducedPullTime()))));
//                } else {
//                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.fast_pull_1").withStyle(ChatFormatting.GRAY));
//                }
            }
            case SLOW_PULL -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.slow_pull").withStyle(ChatFormatting.RED));
//                if (stack.getItem() instanceof ModdedBow bow) {TODO add ModdedBow
//                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.slow_pull_2").withStyle(ChatFormatting.GRAY).append(new TextComponent(String.valueOf(bow.getReducedPullTime()))));
//                } else {
//                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.slow_pull_1").withStyle(ChatFormatting.GRAY));
//                }
            }
            case THIRD_SHOT -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.third_shot").withStyle(ChatFormatting.GOLD));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.third_shot_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TextComponent(String.valueOf(CommonConfig.KRAKEN_SLAYER_BONUS_DAMAGE.get() + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack)))
                        .withStyle(ChatFormatting.WHITE)
                        .append(new TranslatableComponent("tooltip.soulsweapons.third_shot_2").withStyle(ChatFormatting.GRAY)));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.third_shot_3").withStyle(ChatFormatting.GRAY)
                        .append(new TextComponent(Mth.floor((1f - CommonConfig.KRAKEN_SLAYER_PLAYER_DAMAGE_MOD.get()) * 100) + "%"))
                        .withStyle(ChatFormatting.DARK_GRAY));
            }
            case MOONLIGHT_ARROW -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.moonlight_arrow").withStyle(ChatFormatting.AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.moonlight_arrow_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.moonlight_arrow_2").withStyle(ChatFormatting.GRAY));
            }
            case ARROW_STORM -> {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.arrow_storm").withStyle(ChatFormatting.DARK_PURPLE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.arrow_storm_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.arrow_storm_2").withStyle(ChatFormatting.GRAY));
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
        KEYBIND_ABILITY, NIGHTS_EDGE, CHAOS_STORM, VEIL_OF_FIRE, BLIGHT, FAST_PULL, THIRD_SHOT, SLOW_PULL, MOONLIGHT_ARROW,
        ARROW_STORM
    }
}
