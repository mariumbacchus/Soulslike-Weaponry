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
import net.soulsweaponry.entity.projectile.DraupnirSpearEntity;
import net.soulsweaponry.items.abilities.abilitykeybind.ExplodeSavedEntities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record ThrowDraupnirSpear(float projectileSpeed, int minCooldown, int cooldown, int reducedCooldownPerLvl) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity && !this.isCoolingDown(playerEntity, stack) && remainingUseTicks >= 10) {
            DraupnirSpearEntity entity = new DraupnirSpearEntity(world, playerEntity, stack);
            entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, this.projectileSpeed, 1.0F);
            entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            world.spawnEntity(entity);
            world.playSoundFromEntity(null, entity, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
            ExplodeSavedEntities.saveEntityOnItem(stack, entity);
            this.applyItemCooldown(stack.getItem(), playerEntity, this.getScaledCooldownThrow(stack));
            stack.damage(1, playerEntity, WeaponUtil.getActiveHandSlot(playerEntity));
        }
    }

    private int getScaledCooldownThrow(ItemStack stack) {
        return Math.max(this.minCooldown, this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.spear_arsenal").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.spear_arsenal.1").formatted(Formatting.GRAY)
        );
    }
}
