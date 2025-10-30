package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;
import java.util.function.Predicate;

public record SonicBoom(
        float targetSearchRange, float bonusTargetSearchRangePerLvl,
        float maxTargetRange, float bonusMaxRangePerLvl,
        float damage, float bonusDamagePerLvl, float bonusEnchantDamageMod,
        float knockbackPowerMod, float bonusKnockbackModPerLvl,
        int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity player && !this.isCoolingDown(player, stack) && world instanceof ServerWorld serverWorld) {
            if (ticksUsed >= 10) {
                int lvl = WeaponUtil.getUpgradeLevel(stack);
                float searchRange = this.getSearchRange(lvl);
                float maxRange = this.getMaxRange(lvl);
                float queryRadius = Math.max(searchRange, maxRange);
                Box queryBox = user.getBoundingBox().expand(queryRadius);

                Predicate<LivingEntity> nonTeammate = e -> !e.isTeammate(user) && !(e instanceof ArmorStandEntity);
                TargetPredicate targetPredicate = TargetPredicate.createNonAttackable()
                        .setBaseMaxDistance(searchRange)
                        .ignoreVisibility()
                        .setPredicate(nonTeammate);

                LivingEntity current = user.getAttacking();
                boolean withinRange = user.distanceTo(current) <= maxRange;
                LivingEntity target;
                if (current != null && nonTeammate.test(current) && withinRange) {
                    target = current;
                } else {
                    target = world.getClosestEntity(LivingEntity.class, targetPredicate, user, user.getX(), user.getY(), user.getZ(), queryBox);
                }

                if (target != null) {
                    if (!withinRange) {
                        this.fail(world, player);
                        return;
                    }
                    float damage = this.damage + this.bonusDamagePerLvl * lvl + this.bonusEnchantDamageMod
                            * EnchantmentHelper.getDamage(serverWorld, stack, target, world.getDamageSources().playerAttack(player), 0);
                    float knockback = this.knockbackPowerMod + this.bonusKnockbackModPerLvl * lvl;
                    world.sendEntityStatus(user, EntityStatuses.SONIC_BOOM);
                    Vec3d vec3d = user.getPos().add(0.0, 1.6F, 0.0);
                    Vec3d vec3d2 = target.getEyePos().subtract(vec3d);
                    Vec3d vec3d3 = vec3d2.normalize();
                    for (int i = 1; i < MathHelper.floor(vec3d2.length()) + 7; i++) {
                        Vec3d vec3d4 = vec3d.add(vec3d3.multiply(i));
                        ((ServerWorld)world).spawnParticles(ParticleTypes.SONIC_BOOM, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 0.0);
                    }
                    world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 3.0F, 1.0F);
                    target.damage(world.getDamageSources().sonicBoom(user), damage);
                    user.onAttacking(target);
                    double d = 0.5 * (1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                    double e = 2.5 * (1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                    Vec3d vec = new Vec3d(vec3d3.getX() * e, vec3d3.getY() * d, vec3d3.getZ() * e).multiply(knockback);
                    target.addVelocity(vec);
                    stack.damage(2, player, WeaponUtil.getActiveHandSlot(player));
                    this.applyItemCooldown(stack, player, Math.max(this.minCooldown, this.cooldown - lvl * this.reducedCooldownPerLvl));
                } else {
                    this.fail(world, player);
                }
            }
        }
    }

    public float getMaxRange(int lvl) {
        return this.maxTargetRange + this.bonusMaxRangePerLvl * lvl;
    }

    public float getSearchRange(int lvl) {
        return this.targetSearchRange + this.bonusTargetSearchRangePerLvl * lvl;
    }

    private void fail(World world, PlayerEntity player) {
        world.playSound(null, player.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE.value(), SoundCategory.PLAYERS, 1f, 1f);
        this.notifyRange(player);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        return List.of(
                Text.translatable("tooltip.soulsweapons.sonic_boom").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.sonic_boom.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.sonic_boom.2", String.format("%.1f", this.getMaxRange(lvl))).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.sonic_boom.3", String.format("%.1f", this.getSearchRange(lvl))).formatted(Formatting.GRAY)
        );
    }
}
