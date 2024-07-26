package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.WeaponUtil;

public class LichBane extends ModdedSword {

    public LichBane(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, CommonConfig.LICH_BANE_DAMAGE.get(), CommonConfig.LICH_BANE_ATTACK_SPEED.get(), settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.MAGIC_DAMAGE, WeaponUtil.TooltipAbilities.BLAZING_BLADE);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (target.getHealth() > target.getMaxHealth()/3 && target.getHealth() > this.getBonusMagicDamage(stack)) {
            target.damage(CustomDamageSource.TRUE_MAGIC, this.getBonusMagicDamage(stack));
        }
        target.setOnFireFor(4 + 3 * EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack));
        return super.postHit(stack, target, attacker);
    }

    public float getBonusMagicDamage(ItemStack stack) {
        return CommonConfig.LICH_BANE_BONUS_MAGIC_DAMAGE.get() + EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack);
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return CommonConfig.DISABLE_USE_LICH_BANE.get();
    }
}
