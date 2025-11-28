package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.noclip.MoonveilWave;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.stoppedusing.sneaking.MoonveilVertical;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;

public record MoonveilHorizontal(int maxAge, float baseDamage, float bonusDamagePerLvl, int minCooldown, int cooldown, int reducedCooldownPerLvl) implements IAbility {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(stack.getItem()) && !world.isClient) {
            if (ticksUsed >= 10) {
                int lvl = WeaponUtil.getUpgradeLevel(stack);
                MoonveilWave entity = new MoonveilWave(world, user, this.maxAge);
                entity.setAreaParticle(ParticleRegistry.MOONVEIL_PARTICLE);
                entity.setAreaParticleCount((byte) 15);
                entity.setDespawnParticleCount(40);
                entity.setDespawnParticle(ParticleRegistry.BLUE_FLAME);
                entity.setPos(player.getX(), player.getEyeY() - 0.3f, player.getZ());
                entity.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1.0f, 1.0F);
                entity.setDamage(this.baseDamage + this.bonusDamagePerLvl * lvl);
                world.spawnEntity(entity);
                world.playSound(null, user.getBlockPos(), SoundRegistry.MOONVEIL_HORIZONTAL, SoundCategory.PLAYERS, 1f, 1f);
                stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
                this.applyItemCooldown(stack.getItem(), player, Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * lvl));
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        if (IHasAbilities.getAbility(stack, MoonveilVertical.class).isEmpty()) {
            MoonveilVertical.applyMoonveilWaveTooltip(tooltip);
        }
        return tooltip;
    }
}
