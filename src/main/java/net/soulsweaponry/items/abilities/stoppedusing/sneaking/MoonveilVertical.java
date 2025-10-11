package net.soulsweaponry.items.abilities.stoppedusing.sneaking;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.noclip.MoonveilWave;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;

public record MoonveilVertical(float baseDamage, float bonusDamagePerLvl, int minCooldown, int cooldown, int reducedCooldownPerLvl) implements ISneakChargeToUse {

    @Override
    public void sneakingOnStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(stack.getItem()) && !world.isClient) {
            if (remainingUseTicks >= 10) {
                int lvl = WeaponUtil.getUpgradeLevel(stack);
                MoonveilWave entity = new MoonveilWave(EntityRegistry.MOONVEIL_VERTICAL, world, user, 15);
                entity.setAreaParticle(ParticleRegistry.MOONVEIL_PARTICLE);
                entity.setAreaParticleCount((byte) 10);
                entity.setDespawnParticle(ParticleRegistry.BLUE_FLAME);
                entity.setPos(player.getX(), player.getEyeY() - 1f, player.getZ());
                entity.setDespawnParticleCount(40);
                entity.setModelRotationX(90);
                entity.setModelTranslationY(1f);
                entity.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1f, 1.0F);
                entity.setDamage(this.baseDamage + this.bonusDamagePerLvl * lvl);
                world.spawnEntity(entity);
                world.playSound(null, user.getBlockPos(), SoundRegistry.MOONVEIL_VERTICAL, SoundCategory.PLAYERS, 1f, 1f);
                stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
                this.applyItemCooldown(stack.getItem(), player, Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * lvl));
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        applyMoonveilWaveTooltip(tooltip);
        return tooltip;
    }

    public static void applyMoonveilWaveTooltip(List<Text> tooltip) {
        tooltip.add(Text.translatable("tooltip.soulsweapons.transient_moonlight").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("tooltip.soulsweapons.transient_moonlight.1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.transient_moonlight.2").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.transient_moonlight.3").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.transient_moonlight.4").formatted(Formatting.GRAY));
    }
}
