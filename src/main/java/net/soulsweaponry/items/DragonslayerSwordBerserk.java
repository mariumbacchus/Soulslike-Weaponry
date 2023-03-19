package net.soulsweaponry.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class DragonslayerSwordBerserk extends UltraHeavyWeapon {
    
    public DragonslayerSwordBerserk(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.heap_of_raw_iron_damage, attackSpeed, settings, true);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!user.getItemCooldownManager().isCoolingDown(this)) {
            stack.damage(1, user, (p_220045_0_) -> {
                p_220045_0_.sendToolBreakStatus(user.getActiveHand());
            });
            user.getItemCooldownManager().set(this, ConfigConstructor.heap_of_raw_iron_cooldown);
            int power = 1 + (int) Math.floor(EnchantmentHelper.getAttackDamage(stack, user.getGroup())/2);
            user.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY, 200, power));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 0));

            world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, .75f, 1f);

            return TypedActionResult.success(stack);
        } else {
            return TypedActionResult.fail(stack);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.RAGE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.HEAVY, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        
        super.appendTooltip(stack, world, tooltip, context);
    }
}
