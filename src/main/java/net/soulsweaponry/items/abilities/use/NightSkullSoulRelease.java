package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.NightSkull;
import net.soulsweaponry.items.abilities.stoppedusing.ShootMoonlight;
import net.soulsweaponry.items.abilities.targetdeath.ISoulHarvest;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;
import java.util.Optional;

public class NightSkullSoulRelease implements ISoulHarvest {

    private final float baseSkullExplosionPower;
    private final float chargedSkullExplosionPower;
    private final float baseDamage;
    private final float bonusDamagePerLvl;
    private final float chargedBaseDamage;
    private final float chargedBonusDamagePerLvl;
    private final float velocity;
    private final boolean destroyBlocks;
    private final int maxAge;

    public NightSkullSoulRelease(float baseSkullExplosionPower, float chargedSkullExplosionPower, float baseDamage, float bonusDamagePerLvl, float chargedBaseDamage, float chargedBonusDamagePerLvl, float velocity, boolean destroyBlocks, int maxAge) {
        this.baseSkullExplosionPower = baseSkullExplosionPower;
        this.chargedSkullExplosionPower = chargedSkullExplosionPower;
        this.baseDamage = baseDamage;
        this.bonusDamagePerLvl = bonusDamagePerLvl;
        this.chargedBaseDamage = chargedBaseDamage;
        this.chargedBonusDamagePerLvl = chargedBonusDamagePerLvl;
        this.velocity = velocity;
        this.destroyBlocks = destroyBlocks;
        this.maxAge = maxAge;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        int power = this.getSouls(stack);
        if (power > 0 || user.isCreative()) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            boolean chargeMiddle = false;
            if (this.isCritical(stack)) {
                chargeMiddle = true;
                stack.set(ComponentRegistry.WITHER_SOUL_RELEASE_COUNTER, 1);
            } else {
                stack.set(ComponentRegistry.WITHER_SOUL_RELEASE_COUNTER, 1 + stack.getOrDefault(ComponentRegistry.WITHER_SOUL_RELEASE_COUNTER, 0));
            }
            for (int i = 0; i < 3; i++) {
                NightSkull entity = this.createNightSkull(world, user, stack, i, lvl, chargeMiddle && i == 0);
                world.spawnEntity(entity);
            }
            if (!user.isCreative()) {
                this.addAmount(stack, -1);
            }
            this.applyItemCooldown(stack.getItem(), user, 10);
            stack.damage(1, user, LivingEntity.getSlotForHand(hand));
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_WITHER_SHOOT, user.getSoundCategory(), 0.7f, 1f);
            return TypedActionResult.success(stack, world.isClient());
        }
        return TypedActionResult.fail(stack);
    }

    private NightSkull createNightSkull(World world, LivingEntity user, ItemStack stack, int projectileNr, int lvl, boolean charged) {
        float stepDegrees = 10f;
        float damage = charged ? this.chargedBaseDamage + this.chargedBonusDamagePerLvl * lvl : this.baseDamage + this.bonusDamagePerLvl * lvl;
        NightSkull entity = new NightSkull(world, user, damage, charged, this.destroyBlocks, charged ? this.chargedSkullExplosionPower : this.baseSkullExplosionPower, this.maxAge);
        entity.setPos(user.getX(), user.getEyeY() - 0.5f, user.getZ());
        float yawOffset = ShootMoonlight.getYawOffsetForIndex(projectileNr, stepDegrees);
        entity.setVelocity(user, user.getPitch(), user.getYaw() + yawOffset, 0.0F, this.velocity, 1.0F);
        return entity;
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
                Text.translatable("tooltip.soulsweapons.soul_release_wither.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.soul_release_wither.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.soul_release_wither.3").formatted(Formatting.GRAY)
        );
    }

    /*
     * Due to the skulls' drag at 0.95 (and 0.73 for the charged ones), the projectiles WILL get stuck in the air
     * at some point. Therefore, to avoid this, the previous projectile's UUID will be stored into NBT, then
     * checked in the world whether it exists or not, then removed accordingly.
     */
    /*
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
    */
}
