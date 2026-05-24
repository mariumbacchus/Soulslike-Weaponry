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
import net.soulsweaponry.entity.projectile.CometSpearEntity;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record ThrowCometSpear(float speed, int minCooldown, int cooldown, int reducedCooldownPerLvl) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity playerEntity) {
            if (ticksUsed >= 10) {
                int level = WeaponUtil.getUpgradeLevel(stack);
                stack.damage(2, playerEntity, WeaponUtil.getActiveHandSlot(playerEntity));
                this.applyItemCooldown(stack.getItem(), playerEntity,
                        Math.max(this.minCooldown, this.cooldown - level * this.reducedCooldownPerLvl));

                CometSpearEntity entity = new CometSpearEntity(world, playerEntity, stack);
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, this.speed, 1.0F);
                entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                world.spawnEntity(entity);
                world.playSoundFromEntity(null, entity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.throw_comet_spear").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.throw_comet_spear.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.throw_comet_spear.2").formatted(Formatting.GRAY)
        );
    }
}
