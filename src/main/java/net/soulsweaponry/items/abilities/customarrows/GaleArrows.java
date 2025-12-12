package net.soulsweaponry.items.abilities.customarrows;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.arrow.ChargedArrow;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record GaleArrows(int speedDuration, float speedDurationPerLvl, int speedAmp, float speedAmpPerLvl) implements ICustomArrow {

    @Override
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        int lvl = WeaponUtil.getUpgradeLevel(bowStack);
        shooter.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, (int) (this.speedDuration + this.speedDurationPerLvl * lvl), (int) (this.speedAmp + this.speedAmpPerLvl * lvl)));
        return new ChargedArrow(world, shooter, arrowStack, bowStack, false);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.gale_arrows").formatted(Formatting.WHITE, Formatting.ITALIC),
                Text.translatable("tooltip.soulsweapons.gale_arrows.1").formatted(Formatting.GRAY)
        );
    }
}
