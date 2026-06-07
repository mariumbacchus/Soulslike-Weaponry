package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.noclip.AirCombustion;
import net.soulsweaponry.entity.projectile.noclip.MoonveilWave;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record DragonwoundSlash(int maxAge, float waveVelocity, float waveBaseDamage, float waveBonusDamagePerLvl,
                               float explosionDamage, float explosionDamagePerLvl, float explosionRadius, float explosionRadiusPerLvl,
                               int explosionDelayTicks, int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            if (ticksUsed >= 10) {
                int lvl = WeaponUtil.getUpgradeLevel(stack);
                for (int t = -90; t <= 90; t++) {
                    double rad = Math.toRadians(t);
                    int out = 4;
                    float yaw = (float) Math.toRadians(user.getYaw() + 90);
                    double tilt = Math.toRadians(20);
                    double x1 = out * Math.cos(rad);
                    double y1 = out * Math.sin(rad) * Math.cos(tilt);
                    double z1 = out * Math.sin(rad) * Math.sin(tilt);
                    Vec3d vec1 = new Vec3d(x1, y1, z1).rotateY(-yaw).add(user.getEyePos());

                    if (world.isClient) {
                        int div = 75;
                        world.addParticle(ParticleRegistry.CRIMSON_PARTICLE, vec1.x, vec1.y, vec1.z, user.getRandom().nextGaussian()/div, user.getRandom().nextGaussian()/div, user.getRandom().nextGaussian()/div);
                    } else if (t % 30 == 0) {
                        AirCombustion airCombustion = new AirCombustion(world, user, this.explosionDamage + this.explosionDamagePerLvl * lvl, this.explosionRadius + this.explosionRadiusPerLvl * lvl, this.explosionDelayTicks, 0.4f);
                        airCombustion.setPos(vec1.x, vec1.y - airCombustion.getBodyY(0.5f), vec1.z);
                        airCombustion.setSpawnWarnParticles(false);
                        airCombustion.setExplosionParticles(ParticleTypes.LARGE_SMOKE, ParticleRegistry.DRAGONWOUND_FLAME);
                        airCombustion.setExplosionParticleAmount(40);
                        world.spawnEntity(airCombustion);
                    }
                }

                Vec3d spawnPos = user.getEyePos().add(0, -2, 0);
                MoonveilWave entity = new MoonveilWave(EntityRegistry.DRAGONWOUND_WAVE_VERTICAL, world, user, this.maxAge);
                entity.setAreaParticle(ParticleRegistry.CRIMSON_PARTICLE);
                entity.setAreaParticleCount((byte) 10);
                entity.setDespawnParticle(ParticleRegistry.DRAGONWOUND_FLAME);
                entity.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                entity.setDespawnParticleCount(60);
                entity.setModelRotationX(120);
                entity.setModelTranslationY(1f);
                entity.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, this.waveVelocity, 1.0F);
                entity.setDamage(this.waveBaseDamage + this.waveBonusDamagePerLvl * lvl);
                world.spawnEntity(entity);
                world.playSound(null, user.getBlockPos(), SoundRegistry.SHOOT_FIREWAVE, SoundCategory.PLAYERS, 1f, 1f);
                stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
                this.applyItemCooldown(stack.getItem(), player, Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * lvl));
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.dragonwound_slash").formatted(Formatting.LIGHT_PURPLE),
                Text.translatable("tooltip.soulsweapons.dragonwound_slash.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.dragonwound_slash.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.dragonwound_slash.3").formatted(Formatting.GRAY)
        );
    }
}
