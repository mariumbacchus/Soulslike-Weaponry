package net.soulsweaponry.items.abilities.customarrows;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.arrow.MoonlightArrow;
import net.soulsweaponry.mixin.PersistentProjectileEntityInvoker;

import java.util.List;

public class MoonlightArrowAbility implements ICustomArrow {

    @Override
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        MoonlightArrow projectile = new net.soulsweaponry.entity.projectile.arrow.MoonlightArrow(world, shooter, arrowStack, bowStack);
        ((PersistentProjectileEntityInvoker)projectile).invokeSetPierceLevel((byte) 4);
        projectile.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return projectile;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.moonlight_arrow").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.moonlight_arrow.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.moonlight_arrow.2").formatted(Formatting.GRAY)
        );
    }
}
