package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.noclip.GhostGlaiveEntity;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record ThrowGhostGlaive(float projectileDamage, float bonusDamagePerLvl,
                               float projectileSpeed, int postureLoss, int maxProjectileAge,
                               int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity playerEntity) {
            if (ticksUsed >= 10) {
                stack.damage(3, playerEntity, WeaponUtil.getActiveHandSlot(playerEntity));
                GhostGlaiveEntity entity = new GhostGlaiveEntity(world, playerEntity, this.maxProjectileAge);
                entity.setDamage(this.projectileDamage + WeaponUtil.getUpgradeLevel(stack) * this.bonusDamagePerLvl);
                entity.setPos(playerEntity.getX(), playerEntity.getEyeY() - 0.3f, playerEntity.getZ());
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, this.projectileSpeed, 1.0F);
                entity.setPostureLoss(this.postureLoss);
                world.spawnEntity(entity);
                world.playSound(playerEntity, playerEntity.getBlockPos(), SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1f, .5f);
                this.applyItemCooldown(stack.getItem(), playerEntity, Math.max(this.minCooldown, this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl));
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.ghost_glaive").formatted(Formatting.YELLOW),
                Text.translatable("tooltip.soulsweapons.ghost_glaive.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.ghost_glaive.2", this.postureLoss).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.ghost_glaive.3").formatted(Formatting.GRAY)
        );
    }
}
