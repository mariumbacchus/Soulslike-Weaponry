package net.soulsweaponry.items.abilities.customarrows;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.arrow.TrueDamageArrow;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;

public record ThirdShotTrue(
        float trueDamage, float bonusTrueDamagePerLvl, String useAction,
        int stacksAddedPerShot, float bonusStacksPerLvl, int maxStacksUntilTrigger
) implements ICustomArrow {

    @Override
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        Integer firedShots = bowStack.get(ComponentRegistry.KRAKEN_SLAYER_SHOTS_COUNTER);
        int lvl = WeaponUtil.getUpgradeLevel(bowStack);
        int stacksPerShot = this.getStacksAddedPerShot(lvl);
        if (firedShots != null) {
            if (firedShots >= this.maxStacksUntilTrigger) {
                TrueDamageArrow projectile = new TrueDamageArrow(world, shooter, arrowStack, bowStack);
                projectile.setTrueDamage(this.getTrueDamage(lvl));
                projectile.setDamage(originalArrow.getDamage());
                bowStack.set(ComponentRegistry.KRAKEN_SLAYER_SHOTS_COUNTER, stacksPerShot);
                return projectile;
            } else {
                bowStack.set(ComponentRegistry.KRAKEN_SLAYER_SHOTS_COUNTER, firedShots + stacksPerShot);
            }
        } else {
            bowStack.set(ComponentRegistry.KRAKEN_SLAYER_SHOTS_COUNTER, stacksPerShot);
        }
        return null;
    }

    public float getTrueDamage(int lvl) {
        return this.trueDamage + this.bonusTrueDamagePerLvl * lvl;
    }

    public int getStacksAddedPerShot(int lvl) {
        return (int) (this.stacksAddedPerShot + this.bonusStacksPerLvl * lvl);
    }

    @Override
    public UseAction getUseAction() {
        for (UseAction action : UseAction.values()) {
            if (action.toString().equals(this.useAction)) {
                return action;
            }
        }
        return UseAction.SPEAR;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        List<Text> tooltip = new ArrayList<>();
        tooltip.add(Text.translatable("tooltip.soulsweapons.third_shot").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.soulsweapons.third_shot.1", this.getStacksAddedPerShot(lvl),
                this.maxStacksUntilTrigger).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.third_shot.2",
                String.format("%.1f", this.getTrueDamage(lvl))).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.third_shot.3", String.format("%.0f",
                (1f - ConfigConstructor.kraken_slayer_player_true_damage_taken_modifier) * 100) + "%").formatted(Formatting.DARK_GRAY));
        return tooltip;
    }
}
