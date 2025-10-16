package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.MjolnirProjectile;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;

public record ThrowMjolnir(float baseVelocity, float bonusVelocityPerLvl) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player && remainingUseTicks >= 10) {
            MjolnirProjectile projectile = new MjolnirProjectile(world, player, stack);
            projectile.saveOnPlayer(player);
            projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, this.baseVelocity + WeaponUtil.getUpgradeLevel(stack) * this.bonusVelocityPerLvl, 1.0f);
            projectile.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            world.spawnEntity(projectile);
            world.playSoundFromEntity(null, projectile, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0f, 1.0f);
            if (!player.getAbilities().creativeMode) {
                player.getInventory().removeOne(stack);
            }
            stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        tooltip.add(Text.translatable("tooltip.soulsweapons.mjolnir_throw").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
        tooltip.add(Text.translatable("tooltip.soulsweapons.mjolnir_throw.1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.mjolnir_throw.2").formatted(Formatting.GRAY));
        ThrowLeviathanAxe.appendReturningTooltip(tooltip);
        return tooltip;
    }
}
