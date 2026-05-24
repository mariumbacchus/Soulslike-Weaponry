package net.soulsweaponry.items.abilities.statboost;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.items.abilities.ISharpened;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

/**
 * Grants stat boost and bleed post hit if the weapon is sharpened, meaning the
 * {@link net.soulsweaponry.util.NbtIds#SHARPENED_STRIKES} is bigger than 0.
 */
public record Sharpened(
        float bonusDamage, float bonusDamagePerLvl,
        float bonusAttackSpeed, float bonusAttackSpeedPerLvl,
        int bleedPostHit, float bonusBleedPerLvl,
        int bleedDuration, int bleedDurationPerLvl,
        int bleedAmp, float bleedAmpPerLvl,
        int maxEmpoweredStrikes
) implements ISharpened {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (ISharpened.isEmpowered(stack)) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            int duration = this.bleedDuration + this.bleedDurationPerLvl * lvl;
            int amp = (int) (this.bleedAmp + this.bleedAmpPerLvl * lvl);
            BleedData.addBleed(target, (int) (this.bleedPostHit + this.bonusBleedPerLvl * lvl));
            target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEED.get(), duration, amp));
            if (attacker instanceof PlayerEntity player) {
                if (!this.isCoolingDown(player, stack)) {
                    this.reduceEmpowered(stack, player.getWorld(), attacker);
                    this.applyItemCooldown(stack, player, 5);
                }
            } else {
                this.reduceEmpowered(stack, attacker.getWorld(), attacker);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        double damage = WeaponUtil.getBaseItemAttackDamage(stack);
        double attackSpeed = WeaponUtil.getBaseItemAttackSpeed(stack);
        if (ISharpened.isEmpowered(stack)) {
            damage += this.bonusDamage + this.bonusDamagePerLvl * lvl;
            attackSpeed += this.bonusAttackSpeed + this.bonusAttackSpeedPerLvl * lvl;
        }
        WeaponUtil.modifyStackAttributes(stack, damage, attackSpeed);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.sharpen").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.sharpen.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.sharpen.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.sharpen.3", ISharpened.getEmpoweredAttacks(stack)).formatted(Formatting.DARK_GRAY)
        );
    }

    @Override
    public int getMaxEmpoweredStrikes(ItemStack stack) {
        return this.maxEmpoweredStrikes;
    }
}
