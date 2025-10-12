package net.soulsweaponry.items.abilities.statboost;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

/**
 * Post hit will give the player Shadow Step effect, granting movement speed and boosting the damage to the item
 * that has this ability.
 * @param baseBonusDamage
 * @param bonusDamagePerAmp bonus damage per Shadow Step amplifier
 * @param shadowStepTicks
 * @param shadowStepBaseAmp
 * @param bonusAmpPerLvl
 * @param minCooldown
 * @param cooldown
 * @param reducedCooldownPerLvl
 */
public record ShadowStep(float baseBonusDamage, float bonusDamagePerAmp, int shadowStepTicks, int shadowStepBaseAmp, float bonusAmpPerLvl, int minCooldown, int cooldown, int reducedCooldownPerLvl) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && !this.isCoolingDown(player, stack)) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            attacker.addStatusEffect(new StatusEffectInstance(EffectRegistry.SHADOW_STEP, this.shadowStepTicks,
                    (int) (this.shadowStepBaseAmp + this.bonusAmpPerLvl * lvl)));
            this.applyItemCooldown(stack.getItem(), player,
                    Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * lvl));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        float damage = WeaponUtil.getBaseAttackDamage(stack);
        float attackSpeed = WeaponUtil.getBaseAttackSpeed(stack);
        if (entity instanceof LivingEntity living && living.hasStatusEffect(EffectRegistry.SHADOW_STEP)) {
            int amp = living.getStatusEffect(EffectRegistry.SHADOW_STEP).getAmplifier();
            damage += this.baseBonusDamage + this.bonusDamagePerAmp * amp;
        }
        WeaponUtil.modifyStackAttributes(stack, damage, attackSpeed);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        String seconds = String.valueOf(MathHelper.floor((float) this.shadowStepTicks * 0.05f));
        return List.of(
                Text.translatable("tooltip.soulsweapons.early_combat").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.early_combat_description_1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.early_combat_description_2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.early_combat_description_3")
                        .formatted(Formatting.DARK_GRAY, Formatting.ITALIC).append(Text.literal(seconds).formatted(Formatting.AQUA))
        );
    }
}
