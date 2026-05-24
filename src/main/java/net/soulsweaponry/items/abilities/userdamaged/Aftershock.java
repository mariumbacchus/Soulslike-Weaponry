package net.soulsweaponry.items.abilities.userdamaged;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;

public record Aftershock(
        float percentHealthThreshold, float bonusThresholdPerLvl,
        float knockback, float knockbackPerLvl, float damage, float bonusDamagePerLvl, float bonusEnchantDamageMod,
        float expansion, int minCooldown, int cooldown, int reducedCooldownPerLvl, List<StatusEffectInstance> targetHitEffects
) implements IAbility {

    @Override
    public boolean onUserDamaged(DamageSource source, float amount, ItemStack stack, LivingEntity user) {
        World world = user.getWorld();
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        if (user.getHealth() <= user.getMaxHealth() * this.getThreshold(lvl) && !world.isClient && user instanceof PlayerEntity player && !this.isCoolingDown(player, stack)) {
            float knocback = this.knockback + this.knockbackPerLvl * lvl;
            float damage = this.damage + this.bonusDamagePerLvl * lvl;
            ParticleHandler.singleParticle(world, ParticleTypes.EXPLOSION_EMITTER, player.getX(), player.getBodyY(0.5D), player.getZ(), 0, 0, 0);
            for (Entity entity : world.getOtherEntities(player, player.getBoundingBox().expand(this.expansion))) {
                if (entity instanceof LivingEntity target && !target.isTeammate(player)) {
                    this.damageTarget(target, stack, player, knocback, damage);
                }
            }
            world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
            if (!player.isCreative()) {
                this.applyItemCooldown(stack, player, Math.max(this.minCooldown, this.cooldown - lvl * this.reducedCooldownPerLvl));
            }
        }
        return true;
    }

    public void damageTarget(LivingEntity target, ItemStack stack, PlayerEntity player, float knocback, float damage) {
        if (player.getWorld() instanceof ServerWorld) {
            damage += EnchantmentHelper.getAttackDamage(stack, target.getGroup()) * this.bonusEnchantDamageMod;
        }
        for (StatusEffectInstance instance : this.targetHitEffects) {
            target.addStatusEffect(new StatusEffectInstance(instance));
        }
        target.damage(player.getDamageSources().mobAttack(player), damage);
        double x = player.getX() - target.getX();
        double z = player.getZ() - target.getZ();
        target.takeKnockback(knocback, x, z);
    }

    public float getThreshold(int lvl) {
        return this.percentHealthThreshold + this.bonusThresholdPerLvl * lvl;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        MutableText health = Text.of(String.format("%.0f", this.getThreshold(WeaponUtil.getUpgradeLevel(stack)) * 100) + "%").copy();
        tooltip.add(Text.translatable("tooltip.soulsweapons.aftershock").formatted(Formatting.DARK_GREEN));
        tooltip.add(Text.translatable("tooltip.soulsweapons.aftershock.1", health.formatted(Formatting.RED)).formatted(Formatting.GRAY));
        if (!this.targetHitEffects.isEmpty()) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.aftershock.2", this.getLocalizedEffectInstanceNames(this.targetHitEffects)).formatted(Formatting.GRAY));
        }
        return tooltip;
    }
}
