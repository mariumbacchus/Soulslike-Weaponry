package net.soulsweaponry.items.abilities.attackclick;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record ShootSmallMoonlight(
        float projectileSpeed, float damage, float bonusDamagePerLvl, float bonusDamagePerEffectAmp,
        int minCooldown, int cooldown, float reducedCooldownPerLvl, int cooldownWithEffect
) implements IAbility {

    @Override
    public void onAttackClickServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!this.isCoolingDown(player, stack)) {
            //Damaging the itemstack messes with Better Combat, therefore postHit damages weapon twice instead
            boolean hasEffect = player.hasStatusEffect(EffectRegistry.MOON_HERALD);
            int amp = hasEffect ? player.getStatusEffect(EffectRegistry.MOON_HERALD).getAmplifier() + 1 : 0;
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            this.shootMoonlight(world, stack, player, amp, lvl);
            world.playSound(null, player.getBlockPos(), SoundRegistry.MOONLIGHT_SMALL_EVENT, SoundCategory.PLAYERS, 1f, 1f);
            player.swingHand(Hand.MAIN_HAND, true);
            int cooldown = hasEffect ? this.cooldownWithEffect : (int) Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * lvl);
            this.applyItemCooldownNoCheck(stack, player, player.isCreative() ? 5 : cooldown);
        }
    }

    public void shootMoonlight(ServerWorld world, ItemStack stack, PlayerEntity player, int amp, int lvl) {
        float damage = this.damage + this.bonusDamagePerLvl * lvl;
        damage += this.bonusDamagePerEffectAmp * amp;
        MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.MOONLIGHT_ENTITY_TYPE, world, player, stack);
        projectile.setAgeAndPoints(15, 30, (byte) 1);
        projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, this.projectileSpeed, 0f);
        projectile.setDamage(damage);
        world.spawnEntity(projectile);
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, WeaponUtil.getActiveHandSlot(attacker));
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.moonlight").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.moonlight_attack.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.moonlight_attack.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.moonlight_attack.3").formatted(Formatting.GRAY)
        );
    }
}
