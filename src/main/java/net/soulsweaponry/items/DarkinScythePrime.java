package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.WeaponUtil;

public class DarkinScythePrime extends UmbralTrespassItem {

    public DarkinScythePrime(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, CommonConfig.DARKIN_SCYTHE_DAMAGE.get() + CommonConfig.DARKIN_SCYTHE_BONUS_DAMAGE.get(), CommonConfig.DARKIN_SCYTHE_PRIME_ATTACK_SPEED.get(), settings, CommonConfig.DARKIN_SCYTHE_PRIME_TICKS_BEFORE_DISMOUNT.get());
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.OMNIVAMP);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (attacker instanceof PlayerEntity player) {
            if (!player.getItemCooldownManager().isCoolingDown(stack.getItem()) && !(player.getHealth() >= player.getMaxHealth())) {
                if (!player.isCreative()) player.getItemCooldownManager().set(this, CommonConfig.LIFE_STEAL_COOLDOWN.get());
                float healing = CommonConfig.LIFE_STEAL_BASE_HEAL.get();
                if (CommonConfig.LIFE_STEAL_SCALES.get()) {
                    healing += MathHelper.ceil(((float) WeaponUtil.getEnchantDamageBonus(stack))/2);
                }
                attacker.heal(healing);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return CommonConfig.DISABLE_USE_DARKIN_SCYTHE_PRIME.get();
    }
}
