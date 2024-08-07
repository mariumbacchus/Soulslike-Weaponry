package net.soulsweaponry.items;

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
import net.soulsweaponry.util.WeaponUtil;

public class BluemoonGreatsword extends MoonlightGreatsword implements IChargeNeeded {

    public BluemoonGreatsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.bluemoon_greatsword_damage, ConfigConstructor.bluemoon_greatsword_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.NEED_CHARGE, WeaponUtil.TooltipAbilities.CHARGE);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        this.addCharge(stack, this.getAddedCharge(stack));
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_bluemoon_greatsword;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (this.isDisabled(itemStack)) {
            this.notifyDisabled(user);
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
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_bluemoon_greatsword;
    }
}