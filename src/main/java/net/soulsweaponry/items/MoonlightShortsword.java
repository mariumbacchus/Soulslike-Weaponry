package net.soulsweaponry.items;

import java.util.List;

import javax.annotation.Nullable;

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
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class MoonlightShortsword extends SwordItem {
    
    public MoonlightShortsword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.moonlight_shortsword_damage, attackSpeed, settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (ConfigConstructor.moonlight_shortsword_enable_right_click) {
            MoonlightShortsword.summonSmallProjectile(world, user);
            return TypedActionResult.success(itemStack, world.isClient());
        } else {
            return TypedActionResult.fail(itemStack);
        }
		
	}

    public static void summonSmallProjectile(World world, PlayerEntity user) {
        for (Hand hand : Hand.values()) {
            ItemStack itemStack = user.getStackInHand(hand);
            boolean moonHeraldEffect = user.hasStatusEffect(EffectRegistry.MOON_HERALD)
                && user.getStatusEffect(EffectRegistry.MOON_HERALD).getDuration() % (ConfigConstructor.moonlight_shortsword_projectile_cooldown/2) == 0;
            if (!user.getItemCooldownManager().isCoolingDown(WeaponRegistry.MOONLIGHT_SHORTSWORD) && itemStack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD) || moonHeraldEffect) {

                float damage = ConfigConstructor.moonlight_shortsword_projectile_damage;
                MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.MOONLIGHT_ENTITY_TYPE, world, user);
                if (user.hasStatusEffect(EffectRegistry.MOON_HERALD) && !itemStack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD)) {
                    damage += user.getStatusEffect(EffectRegistry.MOON_HERALD).getAmplifier()*1.5f;
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
