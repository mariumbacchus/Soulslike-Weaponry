package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DarkinScythePrime extends UmbralTrespassItem {

    public DarkinScythePrime(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.darkin_scythe_damage + ConfigConstructor.darkin_scythe_bonus_damage, attackSpeed, settings, ConfigConstructor.darkin_scythe_prime_ticks_before_dismount);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        if (this.isDisabled()) {
            this.notifyDisabled(attacker);
            return true;
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
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.OMNIVAMP, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.UMBRAL_TRESPASS, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
    }

    @Override
    public boolean isDisabled() {
        return ConfigConstructor.disable_use_darkin_scythe_prime;
    }
}