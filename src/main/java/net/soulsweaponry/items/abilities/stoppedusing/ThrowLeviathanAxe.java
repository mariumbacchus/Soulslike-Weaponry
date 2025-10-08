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
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.entity.projectile.LeviathanAxeEntity;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;

public record ThrowLeviathanAxe(float baseSpeed, float speedPerLvl) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            if (remainingUseTicks >= 10) {
                stack.damage(3, playerEntity, WeaponUtil.getActiveHandSlot(playerEntity));
                LeviathanAxeEntity entity = new LeviathanAxeEntity(world, user, stack);
                entity.saveOnPlayer(playerEntity);
                float speed = WeaponUtil.getUpgradeLevel(stack) * this.speedPerLvl;
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, this.baseSpeed + speed, 1.0F);
                entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                world.spawnEntity(entity);
                world.playSound(playerEntity, playerEntity.getBlockPos(), SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1f, .5f);
                if (!playerEntity.getAbilities().creativeMode) {
                    playerEntity.getInventory().removeOne(stack);
                }
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        tooltip.add(Text.translatable("tooltip.soulsweapons.heavy_throw").formatted(Formatting.WHITE));
        tooltip.add(Text.translatable("tooltip.soulsweapons.heavy_throw_description").formatted(Formatting.GRAY));
        appendReturningTooltip(tooltip);
        return tooltip;
    }

    public static void appendReturningTooltip(List<Text> tooltip) {
        tooltip.add(Text.translatable("tooltip.soulsweapons.returning").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("tooltip.soulsweapons.returning_description_1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.returning_description_2").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.returning_description_3").append(KeyBindRegistry.returnThrownWeapon.getBoundKeyLocalizedText()).formatted(Formatting.DARK_GRAY));
    }
}
