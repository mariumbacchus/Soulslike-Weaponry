package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class BluemoonGreatsword extends MoonlightGreatsword implements IChargeNeeded {

    public BluemoonGreatsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, CommonConfig.BLUEMOON_GREATSWORD_DAMAGE.get(), CommonConfig.BLUEMOON_GREATSWORD_ATTACK_SPEED.get(), settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.NEED_CHARGE, WeaponUtil.TooltipAbilities.CHARGE);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return CommonConfig.DISABLE_USE_BLUEMOON_GREATSWORD.get();
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (this.isDisabled(itemStack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        if (itemStack.getDamage() < itemStack.getMaxDamage() - 1 && (this.isCharged(itemStack) ||
                user.isCreative() || user.hasStatusEffect(EffectRegistry.MOON_HERALD.get()))) {
            user.setCurrentHand(hand);
            return TypedActionResult.success(itemStack);
        }
        else {
            return TypedActionResult.fail(itemStack);
        }
    }

    @Override
    public int getMaxCharge() {
        return CommonConfig.BLUEMOON_GREATSWORD_CHARGE_NEEDED.get();
    }

    @Override
    public int getAddedCharge(ItemStack stack) {
        return CommonConfig.BLUEMOON_GREATSWORD_CHARGE_ADDED.get();
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }
}
