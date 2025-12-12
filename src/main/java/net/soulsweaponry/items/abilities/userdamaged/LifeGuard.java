package net.soulsweaponry.items.abilities.userdamaged;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record LifeGuard(
        double reducedDamagePercent, double bonusReducedDmgPercentPerLvl,
        double saveLifeChancePercent, double bonusSaveChancePerLvl,
        float lifeSaveExplosionDamage, float lifeSaveExplosionBonusDmgPerLvl,
        float lifeSaveExplosionKnockback, float lifeSaveExplosionBonusKnockbackPerLvl,
        float lifeSaveExplosionRange, float lifeSaveBonusExplosionRangePerLvl, // 2.5D, ?
        int lifeSaveStackDamage, int lifeSaveReducedStackDmgPerLvl
) implements IAbility {

    @Override
    public float modifyUserDamageTaken(LivingEntity user, float damageTaken, DamageSource source, ItemStack stack, Hand hand) {
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        float damage = Math.max(0, (float) (damageTaken * (1D - this.getReducedDamagePercent(lvl))));
        int rounded = Math.round(damageTaken);
        damageTaken = damage;
        int j = Math.min(rounded, 10);
        for (int i = 0; i < j; i++) {
            ParticleHandler.singleParticle(user.getWorld(), ParticleTypes.SOUL, user.getParticleX(1f), user.getRandomBodyY(), user.getParticleZ(1f), 0, 0, 0);
        }
        user.getWorld().playSound(null, user.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE.value(), SoundCategory.PLAYERS, 1f, 1f);
        // Chance to save the player
        double savePercent = this.getLifeSaveChance(lvl);
        float explosionRange = this.lifeSaveExplosionRange + this.lifeSaveBonusExplosionRangePerLvl * lvl;
        if (user.getHealth() - damageTaken < 0 && !user.getWorld().isClient && savePercent < user.getRandom().nextDouble()) {
            ParticleHandler.particleSphereList(user.getWorld(), 500, user.getX(), user.getY(), user.getZ(), 0.4f, ParticleTypes.SCULK_SOUL, ParticleTypes.SMOKE);
            user.getWorld().playSound(null, user.getBlockPos(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 1f, 1f);
            float explosionDamage = this.lifeSaveExplosionDamage + this.lifeSaveExplosionBonusDmgPerLvl * lvl;
            float explosionKnockback = this.lifeSaveExplosionKnockback + this.lifeSaveExplosionBonusKnockbackPerLvl * lvl;
            for (Entity entity1 : user.getWorld().getOtherEntities(user, user.getBoundingBox().expand(explosionRange))) {
                if (entity1 instanceof LivingEntity living) {
                    living.damage(user.getDamageSources().explosion(user, user), explosionDamage);
                    double x = user.getX() - living.getX();
                    double z = user.getZ() - living.getZ();
                    living.takeKnockback(explosionKnockback, x, z);
                }
            }
            damageTaken = 0f;
            rounded += this.lifeSaveStackDamage();
        }
        if (rounded > 0) {
            stack.damage(rounded, user, LivingEntity.getSlotForHand(hand));
        }
        return damageTaken;
    }

    public double getReducedDamagePercent(int itemLvl) {
        return this.reducedDamagePercent + this.bonusReducedDmgPercentPerLvl * itemLvl;
    }

    public double getLifeSaveChance(int itemLvl) {
        return this.saveLifeChancePercent + this.bonusSaveChancePerLvl * itemLvl;
    }

    public int getLifeSaveStackDamage(int itemLvl) {
        return this.lifeSaveStackDamage - this.lifeSaveReducedStackDmgPerLvl * itemLvl;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        return List.of(
                Text.translatable("tooltip.soulsweapons.life_guard").formatted(Formatting.RED),
                Text.translatable("tooltip.soulsweapons.life_guard.1", String.format("%.1f", this.getReducedDamagePercent(lvl) * 100) + "%").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.life_guard.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.life_guard.3").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.life_guard.4", String.format("%.1f", this.getLifeSaveChance(lvl) * 100) + "%").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.life_guard.5").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.life_guard.6", this.getLifeSaveStackDamage(lvl)).formatted(Formatting.DARK_GRAY)
        );
    }
}
