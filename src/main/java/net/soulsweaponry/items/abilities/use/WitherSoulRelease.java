package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.targetdeath.ISoulHarvest;
import net.soulsweaponry.registry.ComponentRegistry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WitherSoulRelease implements ISoulHarvest {

    private final float baseSkullExplosionPower;
    private final float criticalSkullExplosionPower;

    public WitherSoulRelease(float baseSkullExplosionPower, float criticalSkullExplosionPower) {
        this.baseSkullExplosionPower = baseSkullExplosionPower;
        this.criticalSkullExplosionPower = criticalSkullExplosionPower;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (!world.isClient) {
            this.detonatePrevEntity((ServerWorld) world, stack);
        }
        Integer kills = stack.get(ComponentRegistry.SOULS_HARVESTED);
        if (kills != null) {
            int power = this.getSouls(stack);
            if (power > 0 || user.isCreative()) {
                WitherSkullEntity entity = new WitherSkullEntity(EntityType.WITHER_SKULL, world);
                entity.setPos(user.getX(), user.getEyeY(), user.getZ());
                entity.setOwner(user);
                if (this.isCritical(stack)) {
                    entity.setCharged(true);
                    stack.set(ComponentRegistry.WITHER_SOUL_RELEASE_COUNTER, 1);
                } else {
                    stack.set(ComponentRegistry.WITHER_SOUL_RELEASE_COUNTER, 1 + Optional.ofNullable(stack.get(ComponentRegistry.WITHER_SOUL_RELEASE_COUNTER)).orElse(0));
                }
                entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 3f, 1.0F);
                world.spawnEntity(entity);
                this.setPrevUuid(stack, entity);
                if (!user.isCreative()) {
                    this.addAmount(stack, -1);
                }
                this.applyItemCooldown(stack.getItem(), user, 10);
                stack.damage(1, user, LivingEntity.getSlotForHand(hand));
                return TypedActionResult.success(stack, world.isClient());
            }
        }
        return TypedActionResult.fail(stack);
    }

    /**
     * Due to the skulls' drag at 0.95 (and 0.73 for the charged ones), the projectiles WILL get stuck in the air
     * at some point. Therefore, to avoid this, the previous projectile's UUID will be stored into NBT, then
     * checked in the world whether it exists or not, then removed accordingly.
     */
    private void detonatePrevEntity(ServerWorld world, ItemStack stack) {
        UUID prev = stack.get(ComponentRegistry.SAVED_UUID);
        if (prev != null) {
            Entity entity = world.getEntity(prev);
            if (entity instanceof WitherSkullEntity skull) {
                world.createExplosion(skull, skull.getX(), skull.getY(), skull.getZ(), skull.isCharged() ?
                        this.criticalSkullExplosionPower : this.baseSkullExplosionPower, false, World.ExplosionSourceType.MOB);
                skull.discard();
            }
        }
    }

    private void setPrevUuid(ItemStack stack, Entity entityToSet) {
        stack.set(ComponentRegistry.SAVED_UUID, entityToSet.getUuid());
    }

    private boolean isCritical(ItemStack stack) {
        return Optional.ofNullable(stack.get(ComponentRegistry.WITHER_SOUL_RELEASE_COUNTER)).orElseGet(() -> {
            stack.set(ComponentRegistry.WITHER_SOUL_RELEASE_COUNTER, 1);
            return 1;
        }) >= 3;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.soul_release_wither").formatted(Formatting.DARK_RED),
                Text.translatable("tooltip.soulsweapons.soul_release_wither_description_1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.soul_release_wither_description_2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.soul_release_wither_description_3").formatted(Formatting.GRAY)
        );
    }
}
