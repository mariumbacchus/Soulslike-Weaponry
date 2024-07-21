package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.WeaponUtil;

public class DarkinScythePrime extends UmbralTrespassItem {

    public DarkinScythePrime(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.darkin_scythe_damage + ConfigConstructor.darkin_scythe_bonus_damage, ConfigConstructor.darkin_scythe_prime_attack_speed, settings, ConfigConstructor.darkin_scythe_prime_ticks_before_dismount);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.OMNIVAMP);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (attacker instanceof PlayerEntity player) {
            if (!player.getItemCooldownManager().isCoolingDown(stack.getItem()) && !(player.getHealth() >= player.getMaxHealth())) {
                if (!player.isCreative()) player.getItemCooldownManager().set(this, ConfigConstructor.lifesteal_item_cooldown);
                float healing = ConfigConstructor.lifesteal_item_base_healing;
                if (ConfigConstructor.lifesteal_item_heal_scales) {
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
        return ConfigConstructor.disable_use_darkin_scythe_prime;
    }
}