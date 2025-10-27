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
import net.soulsweaponry.entity.projectile.DragonslayerSwordspearEntity;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record ThrowDragonslayerSwordspear(float speed, int minCooldown, int cooldown, int reducedCooldownPerLvl, float rainingCooldownMod) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity playerEntity) {
            if (ticksUsed >= 10) {
                stack.damage(1, playerEntity, WeaponUtil.getActiveHandSlot(playerEntity));
                DragonslayerSwordspearEntity entity = new DragonslayerSwordspearEntity(world, playerEntity, stack);
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, this.speed, 1.0F);
                entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                world.spawnEntity(entity);
                world.playSoundFromEntity(null, entity, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                this.applyItemCooldown(stack.getItem(), playerEntity, this.getScaledCooldownThrow(world, stack));
            }
        }
    }

    private int getScaledCooldownThrow(World world, ItemStack stack) {
        return (int) Math.max(this.minCooldown, (this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl) * (world.isRaining() ? this.rainingCooldownMod : 1f));
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.throw_dragonslayer_swordspear").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.throw_dragonslayer_swordspear.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.throw_dragonslayer_swordspear.2").formatted(Formatting.GRAY)
        );
    }
}
