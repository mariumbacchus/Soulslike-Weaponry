package net.soulsweaponry.util;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.api.trickweapon.TrickWeaponUtil;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.armor.SetBonusArmor;
import net.soulsweaponry.items.gun.GunItem;
import net.soulsweaponry.items.scythe.DarkinScythePre;
import net.soulsweaponry.items.scythe.ShadowAssassinScythe;
import net.soulsweaponry.items.spear.GlaiveOfHodir;
import net.soulsweaponry.items.sword.BluemoonGreatsword;
import net.soulsweaponry.items.sword.Skofnung;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TooltipUtil {

    public static void addTooltip(List<Text> tooltip, String id, Formatting formatting, int lines) {
        tooltip.add(Text.translatable("tooltip.soulsweapons." + id).formatted(formatting));
        for (int i = 1; i <= lines; i++) {
            tooltip.add(Text.translatable("tooltip.soulsweapons." + id + "_description_" + i).formatted(Formatting.GRAY));
        }
    }

    // TODO man i love technical debt its so fun yippie
    // TODO Wow this is getting long... gotta fix that...
    public static void addAbilityTooltip(TooltipAbilities ability, ItemStack stack, List<Text> tooltip) {
        switch (ability) {
            case LIGHTBRINGER -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.lightbringer").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lightbringer.1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lightbringer.2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lightbringer.3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lightbringer.4").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lightbringer.5").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
            }
            case CHAIN_LIGHTNING -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.chain_lightning").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chain_lightning.1").formatted(Formatting.GRAY));
            }
            case SET_BONUS -> {
                if (stack.getItem() instanceof SetBonusArmor armor) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.armor.set_bonus").formatted(Formatting.AQUA));
                    for (StatusEffectInstance effect : armor.getFullSetEffects()) {
                        tooltip.add(Text.translatable("tooltip.soulsweapons.armor.set_bonus.gain_effects").append(effect.getEffectType().value().getName()).formatted(Formatting.GRAY));
                    }
                    tooltip.addAll(Arrays.asList(armor.getFullSetAbilities()));
                }
            }
            case EXALT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.exalt").formatted(Formatting.RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.exalt.1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.exalt.2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.exalt.3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.exalt.4").formatted(Formatting.DARK_GRAY));
            }
            case UNBURNABLE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.fire_immune").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.fire_immune.1").formatted(Formatting.GRAY));
            }
            case INFECTIOUS -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.infectious").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.infectious.1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.infectious.2").formatted(Formatting.GRAY));
                if (stack.isOf(ArmorRegistry.ENHANCED_WITHERED_CHEST)) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.infectious.3").formatted(Formatting.GRAY));
                }
            }
            case UNCEASING -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.unceasing").formatted(Formatting.DARK_PURPLE));
                for (int i = 1; i <= 4; i++) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.unceasing." + i).formatted(Formatting.GRAY));
                }
                //addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case CORRUPT_GROUND -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_robes").formatted(Formatting.WHITE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_robes_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_robes_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_robes_description_3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_robes_description_4").formatted(Formatting.GRAY));
            }
            case MIRROR -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.mirror").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.mirror.1").formatted(Formatting.GRAY).append(Text.literal((int)(ConfigConstructor.arkenplate_mirror_trigger_percent * 100f) + "%").formatted(Formatting.RED)));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.mirror.2").formatted(Formatting.GRAY));
            }
            case AFTERSHOCK -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.aftershock").formatted(Formatting.DARK_GREEN));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.aftershock.1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.aftershock.2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.aftershock.3").formatted(Formatting.GRAY));
                if (stack.isOf(ArmorRegistry.ENHANCED_ARKENPLATE)) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.aftershock.4").formatted(Formatting.GRAY));
                }
            }
            case UNBREAKABLE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate_description_2").formatted(Formatting.GRAY));
            }
            case EMPEROR -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_crown").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_crown_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_crown_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_crown_description_3", ConfigConstructor.chaos_crown_luck_given).formatted(Formatting.GRAY));
            }
            case EFFECT_REVERSAL -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.reversal").formatted(Formatting.DARK_AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.reversal_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.reversal_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.reversal_3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.reversal_4").formatted(Formatting.DARK_GRAY));
            }
            case TRICK_WEAPON -> {
                ItemStack mappedStack = TrickWeaponUtil.getMappedStack(stack);
                if (mappedStack != null) {
                    Item item = stack.getItem();
                    if (ITooltipInfo.shouldShowInfo()) {
                        Text text = TrickWeaponUtil.getMappedItemName(stack);
                        tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon").formatted(Formatting.WHITE));
                        tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon_description_1", ITooltipInfo.formatKeybindText(KeyBindRegistry.switchWeapon.getBoundKeyLocalizedText())).formatted(Formatting.GRAY));
                        if (text != null) {
                            tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon_description_2").formatted(Formatting.DARK_GRAY)
                                    .append(text).formatted(Formatting.WHITE));
                        }
                    } else if (!(item instanceof ITooltipInfo)) {
                        ITooltipInfo.addShowInfoText(tooltip);
                    }
                }
            }
            case CHARGE -> {
                IChargeNeeded item = (IChargeNeeded)stack.getItem();
                String current = MathHelper.floor((float) item.getCharge(stack) / (float) item.getMaxCharge() * 100) + "%";
                tooltip.add(Text.translatable("tooltip.soulsweapons.charge").formatted(Formatting.DARK_AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.charge_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.charge_description_2").formatted(Formatting.DARK_GRAY).append(Text.literal(current + " | " + item.getAddedCharge(stack)).formatted(Formatting.AQUA)));
            }
            case CHARGE_BONUS_DAMAGE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.charge_bonus_damage").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.charge_bonus_damage_1").formatted(Formatting.GRAY));
            }
            case NEED_CHARGE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.need_charge").formatted(Formatting.RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.need_charge_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.need_charge_description_2").formatted(Formatting.GRAY));
            }
            case LUNAR_HERALD_NO_CHARGE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald_no_charge_1").formatted(Formatting.DARK_GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald_no_charge_2").formatted(Formatting.DARK_GRAY));
            }
            case MOONFALL -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonfall").formatted(Formatting.AQUA, Formatting.BOLD));
                for (int i = 1; i <= 3; i++) tooltip.add(Text.translatable("tooltip.soulsweapons.moonfall_description_" + i).formatted(Formatting.GRAY));
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
            case DOOM -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.doom").formatted(Formatting.RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.doom_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.doom_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.doom_description_3").formatted(Formatting.GRAY));
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
                    for (int i = 1; i <= 7; i++) {
                        tooltip.add(Text.translatable("tooltip.soulsweapons.transformation_description_" + i).formatted(Formatting.GRAY));
                    }
                    tooltip.add(Text.translatable("tooltip.soulsweapons.transformation_description_8").formatted(Formatting.GRAY)
                            .append(Text.literal(MathHelper.floor(((float)scythe.getSouls(stack)/ scythe.MAX_SOULS)*100) + "%")
                                    .formatted(scythe.getDominantType(stack).equals(DarkinScythePre.SoulType.BLUE) ? Formatting.AQUA : Formatting.RED)));
                }
            }
            case UMBRAL_TRESPASS -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.umbral_trespass").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.umbral_trespass_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.umbral_trespass_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.umbral_trespass_description_3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.umbral_trespass_description_4").formatted(Formatting.DARK_GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.umbral_trespass_description_5").formatted(Formatting.DARK_GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.umbral_trespass_description_6").formatted(Formatting.DARK_GRAY));
            }
            case DAWNBREAKER -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.meridias_retribution").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.meridias_retribution_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.meridias_retribution_description_2").formatted(Formatting.GRAY));
            }
            case RAGE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.rage").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.rage_description").formatted(Formatting.GRAY));
                //addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case LIGHTNING_CALL -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.lightning_call").formatted(Formatting.YELLOW));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lightning_call.description.1").formatted(Formatting.GRAY));
            }
            case STORM_STOMP -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.storm_stomp").formatted(Formatting.WHITE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.storm_stomp_description").formatted(Formatting.GRAY));
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
                                    ConfigConstructor.draupnir_spear_detonate_power + ((float) WeaponUtil.getLevel(stack, Enchantments.SHARPNESS) / 2.5f)))
                            .formatted(Formatting.WHITE)).formatted(Formatting.GRAY));
                    else tooltip.add(Text.translatable("tooltip.soulsweapons.detonate_spears_description_" + i).formatted(Formatting.GRAY));
                }
                //addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
                tooltip.add(Text.translatable("tooltip.soulsweapons.detonate_spears_description_6").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.detonate_spears_description_7").formatted(Formatting.GRAY));
            }
            case FEATHERLIGHT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.featherlight").formatted(Formatting.LIGHT_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.featherlight_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.featherlight_description_2").formatted(Formatting.GRAY));
            }
            case SOUL_TRAP -> {
                String kills = String.valueOf(Optional.ofNullable(stack.get(ComponentRegistry.KILLS)).orElse(0));
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
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_2", ITooltipInfo.formatKeybindText(KeyBindRegistry.returnFreyrSword.getBoundKeyLocalizedText())).formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_4", ITooltipInfo.formatKeybindText(KeyBindRegistry.stationaryFreyrSword.getBoundKeyLocalizedText())).formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_1").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_2").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
                tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_3").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
            }
            case GALEFORCE -> {
                addTooltip(tooltip, "galeforce", Formatting.AQUA, 6);
                //addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
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
            case MAGIC_DAMAGE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.magic_damage").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.magic_damage_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.magic_damage_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.magic_damage_description_3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.magic_damage_description_4").formatted(Formatting.GRAY).append(Text.literal(
                        String.valueOf(ConfigConstructor.lich_bane_bonus_magic_damage + WeaponUtil.getLevel(stack, Enchantments.FIRE_ASPECT))).formatted(Formatting.DARK_AQUA)));
            }
            case MOONLIGHT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight_description").formatted(Formatting.GRAY));
                if (stack.getItem() instanceof BluemoonGreatsword) {
                    addAbilityTooltip(TooltipAbilities.LUNAR_HERALD_NO_CHARGE, stack, tooltip);
                }
            }
            case MOONLIGHT_ATTACK -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight_attack_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight_attack_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight_attack_description_3").formatted(Formatting.GRAY));
            }
            case LUNAR_HERALD -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald_description_1").formatted(Formatting.GRAY)
                        .append(KeyBindRegistry.effectShootMoonlight.getBoundKeyLocalizedText()));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald_description_2").formatted(Formatting.GRAY));
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
                    tooltip.add(Text.translatable("tooltip.soulsweapons.empowered").formatted(Formatting.AQUA).append(Text.literal(Skofnung.empAttacksLeft(stack) + "/8").formatted(Formatting.AQUA)));
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
                tooltip.add(Text.translatable("tooltip.soulsweapons.sawblade_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.sawblade_description_2").formatted(Formatting.GRAY));
            }
            case WABBAJACK -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.wabbajack").formatted(Formatting.DARK_RED, Formatting.OBFUSCATED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.wabbajack_description").formatted(Formatting.GRAY));
            }
            case LUCK_BASED -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.lucky").formatted(Formatting.DARK_GREEN));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lucky_description_1").formatted(Formatting.GRAY));
            }
            case PARRY -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry_description_1", ITooltipInfo.formatKeybindText(KeyBindRegistry.parry.getBoundKeyLocalizedText())).formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry_description_3").formatted(Formatting.GRAY));
            }
            case SKYWARD_STRIKES -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.skyward_strikes").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.skyward_strikes_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.skyward_strikes_description_2").formatted(Formatting.GRAY));
            }
            case NIGHTS_EDGE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.nights_edge").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.nights_edge_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.nights_edge_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.nights_edge_description_3").formatted(Formatting.GRAY));
                //addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case CHAOS_STORM -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_storm").formatted(Formatting.RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_storm_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_storm_description_2").formatted(Formatting.GRAY));
            }
            case VEIL_OF_FIRE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.veil_of_fire").formatted(Formatting.GOLD).formatted(Formatting.BOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.veil_of_fire_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.veil_of_fire_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.veil_of_fire_description_3").formatted(Formatting.GRAY));
                //addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case BLIGHT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.blight").formatted(Formatting.LIGHT_PURPLE).formatted(Formatting.BOLD));
                for (int i = 1; i <= 5; i++) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.blight_description_" + i).formatted(Formatting.GRAY));
                }
                tooltip.add(Text.translatable("tooltip.soulsweapons.blight_description_6").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
            }
            case FAST_PULL -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.fast_pull").formatted(Formatting.WHITE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.fast_pull_1").formatted(Formatting.GRAY));
            }
            case SLOW_PULL -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.slow_pull").formatted(Formatting.RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.slow_pull_1").formatted(Formatting.GRAY));
            }
            case THIRD_SHOT -> {
                float bonus = 0f;
                if (stack.isOf(WeaponRegistry.KRAKEN_SLAYER)) {
                    bonus = WeaponUtil.getLevel(stack, Enchantments.POWER);
                }
                tooltip.add(Text.translatable("tooltip.soulsweapons.third_shot").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.third_shot_1").formatted(Formatting.GRAY));
                tooltip.add(Text.literal(String.valueOf(ConfigConstructor.kraken_slayer_bonus_true_damage + bonus))
                        .formatted(Formatting.WHITE)
                        .append(Text.translatable("tooltip.soulsweapons.third_shot_2").formatted(Formatting.GRAY)));
                tooltip.add(Text.translatable("tooltip.soulsweapons.third_shot_3").formatted(Formatting.GRAY)
                        .append(Text.literal(MathHelper.floor((1f - ConfigConstructor.kraken_slayer_player_true_damage_taken_modifier) * 100) + "%"))
                        .formatted(Formatting.DARK_GRAY));
                if (stack.isOf(WeaponRegistry.KRAKEN_SLAYER_CROSSBOW)) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.third_shot_4").formatted(Formatting.GRAY));
                }
            }
            case MOONLIGHT_ARROW -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight_arrow").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight_arrow_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight_arrow_2").formatted(Formatting.GRAY));
            }
            case ARROW_STORM -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.arrow_storm").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arrow_storm_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arrow_storm_2").formatted(Formatting.GRAY));
                //addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case TRANSPARENT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.transparent").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.transparent.1").formatted(Formatting.GRAY));
            }
            case CHUNGUS_INFUSED -> {
                int id = new Random().nextInt(0, 16);
                tooltip.add(Text.translatable("tooltip.soulsweapons.chungus_infused").formatted(Formatting.byColorIndex(id)));
                for (int i = 1; i <= 7; i++) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.chungus_infused." + i).formatted(Formatting.GRAY));
                }
                tooltip.add(Text.translatable("tooltip.soulsweapons.chungus_infused.8", ConfigConstructor.chungus_staff_ticks_before_explosion).formatted(Formatting.GRAY));
                //addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
                tooltip.add(Text.translatable("tooltip.soulsweapons.chungus_infused.9").formatted(Formatting.DARK_GRAY));
            }
            case FROST_MOON -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.frost_moon").formatted(Formatting.WHITE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.frost_moon.1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.frost_moon.2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.frost_moon.3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.frost_moon.4").formatted(Formatting.GRAY));
                //addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case GLAIVE_DANCE -> {
                int maxStacks = stack.getItem() instanceof GlaiveOfHodir item ? item.getMaxStacks() : 5;
                int cooldown = stack.getItem() instanceof GlaiveOfHodir item ? item.getMaxStacksCooldown() : 200;
                tooltip.add(Text.translatable("tooltip.soulsweapons.glaive_dance").formatted(Formatting.GOLD));
                for (int i = 1; i <= 4; i++) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.glaive_dance." + i).formatted(Formatting.GRAY));
                }
                tooltip.add(Text.translatable("tooltip.soulsweapons.glaive_dance.5", maxStacks).formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.glaive_dance.6", MathHelper.floor(cooldown / 20f)).formatted(Formatting.GRAY));
                if (!WeaponUtil.isFightModLoaded()) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.glaive_dance.7").formatted(Formatting.GRAY));
                }
            }
            case GHOST_GLAIVE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.ghost_glaive").formatted(Formatting.YELLOW));
                tooltip.add(Text.translatable("tooltip.soulsweapons.ghost_glaive.1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.ghost_glaive.2", ConfigConstructor.glaive_of_hodir_projectile_posture_loss).formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.ghost_glaive.3").formatted(Formatting.GRAY));
            }
            case SONIC_BOOM -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.sonic_boom").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.sonic_boom.1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.sonic_boom.2", String.format("%.1f", ConfigConstructor.excalibur_sonic_boom_max_range)).formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.sonic_boom.3", String.format("%.1f", ConfigConstructor.excalibur_sonic_boom_target_search_range)).formatted(Formatting.GRAY));
            }
            case LIFE_GUARD -> {
                ILifeGuard item = (ILifeGuard) stack.getItem();
                tooltip.add(Text.translatable("tooltip.soulsweapons.life_guard").formatted(Formatting.RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.life_guard.1", String.format("%.1f", item.getLifeGuardPercent(stack) * 100) + "%").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.life_guard.2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.life_guard.3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.life_guard.4", String.format("%.1f", item.getLifeSaveChance(stack) * 100) + "%").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.life_guard.5").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.life_guard.6", item.getLifeSaveStackDamage(stack)).formatted(Formatting.DARK_GRAY));
            }
            case TRANSIENT_MOONLIGHT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.transient_moonlight").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.transient_moonlight.1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.transient_moonlight.2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.transient_moonlight.3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.transient_moonlight.4").formatted(Formatting.GRAY));
            }
            case GUN_ITEM -> {
                if (stack.getItem() instanceof GunItem gun) {
                    float bonus = stack.getOrDefault(ComponentRegistry.GUN_BONUS_DAMAGE, 0f);
                    MutableText damage = Text.literal(String.format("%.1f", (gun.getBulletDamage(stack) + bonus)));
                    if (bonus > 0) {
                        damage.formatted(Formatting.BLUE);
                    }
                    tooltip.add(Text.translatable("tooltip.soulsweapons.gun_posture_loss").append(Text.literal(String.valueOf(gun.getPostureLoss(stack)))).formatted(Formatting.GRAY));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.gun_posture_loss_on_players", MathHelper.floor(ConfigConstructor.silver_bullet_posture_loss_on_player_modifier * 100f) + "%").formatted(Formatting.DARK_GRAY));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.gun_damage").formatted(Formatting.GRAY).append(damage));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.gun_cooldown").append(Text.literal(String.valueOf(gun.getCooldown(stack)))).formatted(Formatting.GRAY));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.gun_bullets_used").append(Text.literal(String.valueOf(gun.getBulletsNeeded(stack)))).formatted(Formatting.GRAY));
                    if (gun.getMaxUseTime(stack, null) != 0) {
                        tooltip.add(Text.translatable("tooltip.soulsweapons.gun_max_use_time").append(Text.literal(String.valueOf(gun.getMaxUseTime(stack, null)))).formatted(Formatting.GRAY));
                    }
                }
            }
            case PROJECTILE_POSTURE_LOSS -> {
                if (stack.getItem() instanceof IPostureLossItem postureLoss) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.projectile_posture_loss").formatted(Formatting.LIGHT_PURPLE));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.projectile_posture_loss.1", postureLoss.getPostureLoss()).formatted(Formatting.GRAY));
                }
            }
            case DRAGONS_SCOURGE -> {
                if (stack.getItem() instanceof IDragonBonus bonus) {
                    float amount = bonus.getTotalDragonBonus(stack);
                    tooltip.add(Text.translatable("tooltip.soulsweapons.dragons_bane").formatted(Formatting.DARK_PURPLE));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.dragons_bane.1", String.format("%.1f", amount)).formatted(Formatting.GRAY));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.dragons_bane.2").formatted(Formatting.DARK_GRAY));
                }
            }
        }
    }
}
