package net.soulsweaponry.items.abilities.usagetick;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record Sawblade(
        float range, float boxExpansion, float damage, float bonusDamagePerLvl, float bonusEnchantDamageMod, float knockback,
        int bleed, float bleedPerLvl, int bleedDuration, int bleedDurationPerLvl,
        int bleedAmp, float bleedAmpPerLvl, int maxUseTime, int bonusMaxUseTimePerLvl,
        int minCooldown, int cooldown, int reducedCooldownPerLvl

) implements IChargeUsageTicks {

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks > 0) {
            Vec3d vecBlocksAway = user.getRotationVector().multiply(this.range).add((user).getPos());
            Box chunkBox = new Box(user.getX(), user.getY(), user.getZ(), vecBlocksAway.x, vecBlocksAway.y + 1, vecBlocksAway.z).expand(this.boxExpansion);
            List<Entity> nearbyEntities = world.getOtherEntities(user, chunkBox);
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            float damage = this.damage + bonusDamagePerLvl * lvl;
            int duration = this.bleedDuration + this.bleedDurationPerLvl * lvl;
            int amp = (int) (this.bleedAmp + this.bleedAmpPerLvl * lvl);
            if (world instanceof ServerWorld serverWorld) {
                for (Entity nearbyEntity : nearbyEntities) {
                    if (nearbyEntity instanceof LivingEntity target) {
                        if (target.damage(serverWorld, world.getDamageSources().mobAttack(user), damage
                                + EnchantmentHelper.getDamage(serverWorld, stack, target, world.getDamageSources().mobAttack(user), 0) * this.bonusEnchantDamageMod)) {
                            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1f, 1f);
                            target.takeKnockback(this.knockback, 0, 0);
                            BleedData.addBleed(target, (int) (this.bleed + this.bleedPerLvl * lvl));
                            target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEED, duration, amp));
                        }
                    }
                }
            }
            world.addParticle(ParticleTypes.SWEEP_ATTACK, vecBlocksAway.getX(), vecBlocksAway.getY() + 1F, vecBlocksAway.getZ(), user.getRandom().nextInt(10) - 5, user.getRandom().nextInt(10) - 5, user.getRandom().nextInt(10) - 5);
        } else {
            user.stopUsingItem();
        }
    }

    @Override
    public int getCooldown(ItemStack stack, int ticksUsed) {
        return Math.max(this.minCooldown, this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl - ticksUsed);
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
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.sawblade").formatted(Formatting.DARK_RED),
                Text.translatable("tooltip.soulsweapons.sawblade.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.sawblade.2").formatted(Formatting.GRAY)
        );
    }
}
