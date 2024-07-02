package net.soulsweaponry.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BluemoonGreatsword extends MoonlightGreatsword implements IChargeNeeded {

    public BluemoonGreatsword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.bluemoon_greatsword_damage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (ConfigConstructor.disable_use_bluemoon_greatsword) {
            if (ConfigConstructor.inform_player_about_disabled_use){
                attacker.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.useDisabled","This item is disabled"));
            }
            return super.postHit(stack, target, attacker);
        }
        this.addCharge(stack, this.getAddedCharge(stack));
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (ConfigConstructor.disable_use_bluemoon_greatsword) {
            if (ConfigConstructor.inform_player_about_disabled_use){
                user.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.useDisabled","This item is disabled"));
            }
            return TypedActionResult.fail(itemStack);
        }
        if (itemStack.getDamage() < itemStack.getMaxDamage() - 1 && (this.isCharged(itemStack) ||
                user.isCreative() || user.hasStatusEffect(EffectRegistry.MOON_HERALD))) {
            user.setCurrentHand(hand);
            return TypedActionResult.success(itemStack);
        }
        else {
            return TypedActionResult.fail(itemStack);
        }
    }

    @Override
    public int getMaxCharge() {
        return ConfigConstructor.bluemoon_greatsword_charge_needed;
    }

    @Override
    public int getAddedCharge(ItemStack stack) {
        return ConfigConstructor.bluemoon_greatsword_charge_added_post_hit;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (ConfigConstructor.disable_use_bluemoon_greatsword) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.disabled"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}