package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.noclip.HolyMoonlightPillar;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record Moonfall(
        int pillarAmount, float bonusPillarPerLvl, float pillarRadius,
        float damage, float bonusDamagePerLvl, float bonusDamageEnchantMod,
        float knockup, float bonusKnockupPerLvl,
        int minCooldown, int cooldown, int reducedCooldownPerLvl, int reducedCooldownPerEffectAmp
) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity player && world instanceof ServerWorld serverWorld) {
            if (ticksUsed >= 10) {
                int emp = player.hasStatusEffect(EffectRegistry.MOON_HERALD) ?
                        this.reducedCooldownPerEffectAmp * player.getStatusEffect(EffectRegistry.MOON_HERALD).getAmplifier() : 0;
                int lvl = WeaponUtil.getUpgradeLevel(stack);
                int ruptures = this.getRuptureAmount(lvl);
                Vec3d vecBlocksAway = player.getRotationVector().multiply(3).add(player.getPos());
                BlockPos targetArea = new BlockPos((int)vecBlocksAway.x, (int)user.getY(), (int)vecBlocksAway.z);
                float power = this.damage + this.bonusDamagePerLvl * lvl;
                float knockup = this.knockup + this.bonusKnockupPerLvl * lvl;
                for (Entity entity : world.getOtherEntities(player, new Box(targetArea).expand(3))) {
                    if (entity instanceof LivingEntity target) {
                        entity.damage(DamageSourceRegistry.create(world, DamageSourceRegistry.OBLITERATED, player),
                                power + this.bonusDamageEnchantMod * EnchantmentHelper.getDamage(serverWorld, stack, target, world.getDamageSources().playerAttack(player), 0));
                        entity.addVelocity(0, this.knockup, 0);
                    }
                }
                WeaponUtil.doConsumerOnLine(world, user.getYaw() + 90, user.getPos(), 4, ruptures, 1.75f,
                        (Vec3d position, Integer warmup, Float yaw) -> {
                            HolyMoonlightPillar pillar = new HolyMoonlightPillar(EntityRegistry.HOLY_MOONLIGHT_PILLAR, world);
                            pillar.setOwner(user);
                            pillar.setParticleAmountMod(1f);
                            pillar.setRadius(this.pillarRadius);
                            pillar.setDamage(power);
                            pillar.setKnockUp(knockup);
                            pillar.setWarmup(warmup);
                            pillar.setPos(position.getX(), position.getY(), position.getZ());
                            world.spawnEntity(pillar);
                        }
                );
                if (!player.isCreative()) {
                    this.applyItemCooldown(stack.getItem(), player,
                            Math.max(this.minCooldown, this.cooldown - lvl * this.reducedCooldownPerLvl - emp));
                    stack.damage(5, player, WeaponUtil.getActiveHandSlot(player));
                }
                world.playSound(player, targetArea, SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                world.playSound(player, targetArea, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1f, 1f);
                ParticleHandler.particleOutburstMap(player.getWorld(), 150, vecBlocksAway.getX(), user.getY(), vecBlocksAway.getZ(), ParticleEvents.MOONFALL_MAP, 1f);
            }
        }
    }

    private int getRuptureAmount(int lvl) {
        return (int) (this.pillarAmount + this.bonusPillarPerLvl * lvl);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.moonfall").formatted(Formatting.AQUA, Formatting.BOLD),
                Text.translatable("tooltip.soulsweapons.moonfall.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.moonfall.2", this.getRuptureAmount(WeaponUtil.getUpgradeLevel(stack))).formatted(Formatting.GRAY)
        );
    }
}
