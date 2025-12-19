package net.soulsweaponry.items.abilities.usagetick;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record DragonMist(boolean healMobsOwnedByOthers, float baseDamageOrHeal, float bonusDamagePerLvl,
                         int mistEffectDuration, int mistEffectAmp, int maxUseTime, int bonusMaxUseTimePerLvl,
                         int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IChargeUsageTicks {

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks > 0) {
            Vec3d pov = user.getRotationVector();
            Vec3d area = pov.multiply(10).add(user.getPos());
            Vec3i on = new Vec3i((int) area.getX(), (int) area.getY(), (int) area.getZ());
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            float healOrDamage = this.baseDamageOrHeal + this.bonusDamagePerLvl * lvl;
            for (Entity entity : world.getOtherEntities(user, new Box(user.getPos().add(0, 2, 0), new BlockPos(on).toCenterPos()))) {
                if (entity instanceof LivingEntity living) {
                    // Heal tamed entities with an owner, including those not owned by the user if config is true
                    if (entity instanceof Tameable tameable && tameable.getOwnerUuid() != null && (tameable.getOwnerUuid().equals(user.getUuid()) || this.healMobsOwnedByOthers)) {
                        living.heal(healOrDamage);
                        if (world.isClient) {
                            world.addParticle(ParticleTypes.HEART, living.getParticleX(0.5),//TODO test
                                    living.getRandomBodyY(), living.getParticleZ(0.5), 0, 0, 0);
                        }
                    } else if (world instanceof ServerWorld serverWorld) {
                        living.damage(serverWorld, DamageSourceRegistry.create(world, DamageSourceRegistry.DRAGON_MIST, user), healOrDamage);
                    }
                    living.addStatusEffect(new StatusEffectInstance(EffectRegistry.HALLOWED_DRAGON_MIST, this.mistEffectDuration, this.mistEffectAmp));
                }
            }
            if (world.isClient) {
                float height = user.isSneaking() ? 1.5f : 1.75f;
                Vec3d origin = user.getPos().add(pov.multiply(1.0)).add(0, height, 0);
                for (int k = 0; k < 8; k++) {
                    double rx = user.getRandom().nextDouble() - 0.5;
                    double ry = user.getRandom().nextDouble() - 0.5;
                    double rz = user.getRandom().nextDouble() - 0.5;
                    double spread = 0.5; // bigger = wider cone
                    Vec3d jitter = new Vec3d(rx, ry, rz).multiply(spread);
                    Vec3d vel = pov.normalize().multiply(1.0).add(jitter);

                    world.addParticle(ParticleRegistry.PURPLE_FLAME, true, origin.x, origin.y, origin.z,
                            vel.x, vel.y, vel.z);
                    world.addParticle(ParticleTypes.DRAGON_BREATH, true, origin.x, origin.y, origin.z,
                            vel.x, vel.y, vel.z);
                }
            }
        } else {
            user.stopUsingItem();
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return this.maxUseTime + WeaponUtil.getUpgradeLevel(stack) * this.bonusMaxUseTimePerLvl;
    }

    @Override
    public UseAction getUseAction() {
        return UseAction.BOW;
    }

    @Override
    public int getCooldown(ItemStack stack, int ticksUsed) {
        return Math.max(this.minCooldown, this.cooldown
                - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl
                - ticksUsed);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.vengeful_fog").formatted(Formatting.LIGHT_PURPLE),
                Text.translatable("tooltip.soulsweapons.vengeful_fog.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.vengeful_fog.2").formatted(Formatting.GRAY)
        );
    }
}
