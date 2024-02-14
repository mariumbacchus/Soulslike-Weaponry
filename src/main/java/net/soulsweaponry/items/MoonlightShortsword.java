package net.soulsweaponry.items;

import java.util.List;

import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

public class MoonlightShortsword extends SwordItem {
    
    public MoonlightShortsword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.moonlight_shortsword_damage, attackSpeed, settings);
    }

    public MoonlightShortsword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public static void summonSmallProjectile(World world, PlayerEntity user) {
        for (Hand hand : Hand.values()) {
            ItemStack itemStack = user.getStackInHand(hand);
            if (!user.getItemCooldownManager().isCoolingDown(WeaponRegistry.MOONLIGHT_SHORTSWORD) && itemStack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD)
                    || user.hasStatusEffect(EffectRegistry.MOON_HERALD)
                    || (itemStack.isOf(WeaponRegistry.BLUEMOON_SHORTSWORD) && !user.getItemCooldownManager().isCoolingDown(WeaponRegistry.BLUEMOON_SHORTSWORD))) {
                boolean bl = itemStack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD) || itemStack.isOf(WeaponRegistry.BLUEMOON_SHORTSWORD);
                if (user.hasStatusEffect(EffectRegistry.MOON_HERALD) && bl && user.getStatusEffect(EffectRegistry.MOON_HERALD).getDuration() % 4 != 0) {
                    return;
                }
                float damage = ConfigConstructor.moonlight_shortsword_projectile_damage;
                MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.MOONLIGHT_ENTITY_TYPE, world, user);
                if (user.hasStatusEffect(EffectRegistry.MOON_HERALD) && !itemStack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD)) {
                    damage += user.getStatusEffect(EffectRegistry.MOON_HERALD).getAmplifier() * 2f;
                }
                projectile.setAgeAndPoints(15, 30, 1);
                projectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 0f);
                projectile.setDamage(damage);
                if (itemStack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD)) projectile.setItemStack(itemStack);
                world.spawnEntity(projectile);
    
                //Damaging the itemstack messes with Better Combat, therefore postHit damages weapon twice instead
                /* itemStack.damage(1, user, (p_220045_0_) -> {
                    p_220045_0_.sendToolBreakStatus(hand);
                }); */
                if (itemStack.getItem() instanceof BluemoonShortsword) {
                    user.getItemCooldownManager().set(WeaponRegistry.BLUEMOON_SHORTSWORD, ConfigConstructor.bluemoon_shortsword_projectile_cooldown);
                }

                world.playSound(null, user.getBlockPos(), SoundRegistry.MOONLIGHT_SMALL_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                user.getItemCooldownManager().set(WeaponRegistry.MOONLIGHT_SHORTSWORD, ConfigConstructor.moonlight_shortsword_projectile_cooldown);
                user.swingHand(Hand.MAIN_HAND, true);
            }
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.MOONLIGHT_ATTACK, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
