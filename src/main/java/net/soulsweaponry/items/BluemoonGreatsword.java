package net.soulsweaponry.items;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;

public class BluemoonGreatsword extends MoonlightGreatsword implements IChargeNeeded {
    
    public BluemoonGreatsword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.bluemoon_greatsword_damage, attackSpeed, settings);
    }

    @Override
    public boolean isDisabled() {
        return ConfigConstructor.disable_use_bluemoon_greatsword;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled()) {
            stack.damage(1, attacker, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            this.notifyDisabled(attacker);
            return true;
        }
        this.addCharge(stack, this.getAddedCharge(stack));
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this.isDisabled()) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        ItemStack itemStack = user.getStackInHand(hand);
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
}
