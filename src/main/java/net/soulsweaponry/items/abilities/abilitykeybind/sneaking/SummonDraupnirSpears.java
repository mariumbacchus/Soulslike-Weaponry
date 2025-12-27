package net.soulsweaponry.items.abilities.abilitykeybind.sneaking;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.entity.projectile.DraupnirSpearEntity;
import net.soulsweaponry.items.abilities.abilitykeybind.ExplodeSavedEntities;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record SummonDraupnirSpears(float rangeOut, int spearAmount, int minCooldown, int cooldown, int reducedCooldownPerLvl) implements ISneakKeybindAbility {

    @Override
    public void sneakingUseKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (this.hasCooldownEffect(player)) {
            this.notifyCooldown(player);
            return;
        }
        int thetaAdded = 360 / this.spearAmount;
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.PLAYERS, 1f, 1f);
        for (int theta = 0; theta < 360; theta += thetaAdded) {
            double x0 = player.getX();
            double z0 = player.getZ();
            double x = x0 + this.rangeOut * Math.cos(theta * Math.PI / 180);
            double z = z0 + this.rangeOut * Math.sin(theta * Math.PI / 180);
            double x1 = Math.cos(theta * Math.PI / 180);
            double z1 = Math.sin(theta * Math.PI / 180);

            DraupnirSpearEntity entity = new DraupnirSpearEntity(world, player, stack);
            entity.setPos(x, player.getY() + 5, z);
            entity.setVelocity(x1, -3, z1);
            entity.setPitch(-90);
            entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            world.spawnEntity(entity);
            ExplodeSavedEntities.saveEntityOnItem(stack, entity);
            ParticleHandler.particleOutburst(world, 10, x, player.getY() + 5, z, ParticleTypes.CLOUD, new Vec3d(4, 4, 4), 0.5f);
        }
        if (!player.isCreative()) {
            this.applyEffectCooldown(player, this.getScaledCooldownSummon(stack));
        }
    }

    private int getScaledCooldownSummon(ItemStack stack) {
        return Math.max(this.minCooldown, this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.draupnirs_call").formatted(Formatting.LIGHT_PURPLE),
                Text.translatable("tooltip.soulsweapons.draupnirs_call.1", this.spearAmount).formatted(Formatting.GRAY)
        );
    }
}
