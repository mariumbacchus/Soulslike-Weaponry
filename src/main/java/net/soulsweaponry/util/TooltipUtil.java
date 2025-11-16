package net.soulsweaponry.util;

import net.minecraft.enchantment.Enchantments;
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
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.gun.GunItem;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

import java.util.List;

public class TooltipUtil {

    // TODO man i love technical debt its so fun yippie
    // TODO Wow this is getting long... gotta fix that...
    public static void addAbilityTooltip(TooltipAbilities ability, ItemStack stack, List<Text> tooltip) {
        switch (ability) {
            case TRICK_WEAPON -> {
                ItemStack mappedStack = TrickWeaponUtil.getMappedStack(stack);
                if (mappedStack != null) {
                    Item item = stack.getItem();
                    /*if (ITooltipInfo.shouldShowInfo()) { TODO
                        Text text = TrickWeaponUtil.getMappedItemName(stack);
                        tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon").formatted(Formatting.WHITE));
                        tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon_description_1", ITooltipInfo.formatKeybindText(KeyBindRegistry.switchWeapon.getBoundKeyLocalizedText())).formatted(Formatting.GRAY));
                        if (text != null) {
                            tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon_description_2").formatted(Formatting.DARK_GRAY)
                                    .append(text).formatted(Formatting.WHITE));
                        }
                    } else if (!(item instanceof ITooltipInfo)) {
                        ITooltipInfo.addShowInfoText(tooltip);
                    }*/
                }
            }
            case LUNAR_HERALD -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald_description_1").formatted(Formatting.GRAY)
                        .append(KeyBindRegistry.effectShootMoonlight.getBoundKeyLocalizedText()));
                tooltip.add(Text.translatable("tooltip.soulsweapons.lunar_herald_description_2").formatted(Formatting.GRAY));
            }
            case PARRY -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry_description_1", IHasAbilities.formatKeybindText(KeyBindRegistry.parry.getBoundKeyLocalizedText())).formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.parry_description_3").formatted(Formatting.GRAY));
            }
            case TRANSPARENT -> {
                tooltip.add(Text.translatable("tooltip.soulsweapons.transparent").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.transparent.1").formatted(Formatting.GRAY));
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
