package net.soulsweaponry.items.abilities.customarrows;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.arrow.SilverArrow;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record SilverArrows(int postureLoss, float postureLossPerLvl, float undeadBonus, float undeadBonusPerLvl) implements ICustomArrow {

    @Override
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        SilverArrow arrow = new SilverArrow(shooter, world, arrowStack, bowStack);
        arrow.setBonusUndeadDamage(this.getUndeadBonus(bowStack));
        arrow.setPostureLoss(this.getPostureLoss(bowStack));
        return arrow;
    }

    public int getPostureLoss(ItemStack stack) {
        return (int) (this.postureLoss + this.postureLossPerLvl * WeaponUtil.getUpgradeLevel(stack));
    }

    public float getUndeadBonus(ItemStack stack) {
        return this.undeadBonus + this.undeadBonusPerLvl * WeaponUtil.getUpgradeLevel(stack);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.silver_arrows").formatted(Formatting.LIGHT_PURPLE),
                Text.translatable("tooltip.soulsweapons.silver_arrows.1", this.getPostureLoss(stack)).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.silver_arrows.2", String.format("%.1f", this.getUndeadBonus(stack))).formatted(Formatting.GRAY)
        );
    }
}
