package net.soulsweaponry.items.scythe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.UmbralTrespassItem;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

public class ShadowAssassinScythe extends UmbralTrespassItem {

    public static final int TICKS_FOR_BONUS = (int) ConfigConstructor.shadow_assassin_scythe_shadow_step_ticks;

    public ShadowAssassinScythe(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) (ConfigConstructor.darkin_scythe_damage + ConfigConstructor.darkin_scythe_bonus_damage), ConfigConstructor.shadow_assassin_scythe_attack_speed, settings, (int) ConfigConstructor.shadow_assassin_scythe_ticks_before_dismount);
        this.addTooltipAbility(TooltipAbilities.SHADOW_STEP);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (attacker instanceof PlayerEntity player) {
            var cooldownManager = player.getItemCooldownManager();
            if (!cooldownManager.isCoolingDown(this)) {
                attacker.addStatusEffect(new StatusEffectInstance(EffectRegistry.SHADOW_STEP, TICKS_FOR_BONUS,
                        MathHelper.floor(WeaponUtil.getEnchantDamageBonus(stack)/2f)));
                cooldownManager.set(this, (int) ConfigConstructor.shadow_assassin_scythe_shadow_step_cooldown);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (this.isDisabled(stack)) return;
        boolean canGetBonus = entity instanceof LivingEntity living && living.hasStatusEffect(EffectRegistry.SHADOW_STEP);
        float bonus = canGetBonus ? this.getAttackDamage() + ConfigConstructor.shadow_assassin_scythe_shadow_step_bonus_damage : this.getAttackDamage();
        WeaponUtil.modifyStackAttributes(stack, bonus, this.getAttackSpeed());
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_shadow_assassin_scythe;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_shadow_assassin_scythe;
    }

    @Override
    public float getAbilityDamage() {
        return ConfigConstructor.shadow_assassin_scythe_ability_damage;
    }

    @Override
    public int getAbilityCooldown(ItemStack stack) {
        return (int) Math.max(ConfigConstructor.shadow_assassin_scythe_ability_min_cooldown, ConfigConstructor.shadow_assassin_scythe_ability_cooldown
                - this.getReduceCooldownEnchantLevel(stack) * 25);
    }

    @Override
    public boolean shouldAbilityHeal() {
        return false;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.shadow_assassin_scythe_ability_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.shadow_assassin_scythe_ability_enchant_reduces_cooldown_ids;
    }
}