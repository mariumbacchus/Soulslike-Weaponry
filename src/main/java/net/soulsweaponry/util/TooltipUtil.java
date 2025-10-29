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
import net.soulsweaponry.items.sword.BluemoonGreatsword;
import net.soulsweaponry.items.sword.Skofnung;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

import java.util.Arrays;
import java.util.List;

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
            case RAGE -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.rage").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.rage_description").formatted(Formatting.GRAY));
                //addAbilityTooltip(TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
            }
            case NIGHT_PROWLER -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.night_prowler").formatted(Formatting.DARK_AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.night_prowler_description").formatted(Formatting.GRAY));
            }
            case FEATHERLIGHT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.featherlight").formatted(Formatting.LIGHT_PURPLE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.featherlight_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.featherlight_description_2").formatted(Formatting.GRAY));
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
            case LUNAR_HERALD -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald_description_1").formatted(Formatting.GRAY)
                        .append(KeyBindRegistry.effectShootMoonlight.getBoundKeyLocalizedText()));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald_description_2").formatted(Formatting.GRAY));
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
        }
    }
}
