package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.noclip.DamagingWarmupEntityEvents;
import net.soulsweaponry.entity.projectile.noclip.FlamePillar;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;

public record ChaosStorm(
        float baseDamage, float bonusDamagePerLvl, int fireResistanceDuration,
        int pillarRange, int pillarAmount, int bonusPillarsPerLvl, float pillarSize,
        int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity player && !this.isCoolingDown(player, stack)) {
            if (ticksUsed >= 10) {
                stack.damage(1, player, WeaponUtil.getActiveHandSlot(player));
                this.summonFlamePillars(world, stack, user);
                this.applyItemCooldown(stack.getItem(), player, this.getScaledCooldown(stack));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, this.fireResistanceDuration, 0));
            }
        }
    }

    private int getScaledCooldown(ItemStack stack) {
        return Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * WeaponUtil.getUpgradeLevel(stack));
    }

    public void summonFlamePillars(World world, ItemStack stack, LivingEntity user) {
        if (!world.isClient) {
            int i = 0;
            List<BlockPos> list = new ArrayList<>();
            list.add(new BlockPos(0, 0, 0));
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            while (i < this.pillarAmount + this.bonusPillarsPerLvl * lvl) {
                int x = user.getBlockX() + user.getRandom().nextInt(this.pillarRange) - this.pillarRange / 2;
                int y = user.getBlockY();
                int z = user.getBlockZ() + user.getRandom().nextInt(this.pillarRange) - this.pillarAmount / 2;
                BlockPos pos = new BlockPos(x, y, z);
                for (BlockPos listPos : list) {
                    if (listPos != pos) {
                        FlamePillar pillar = new FlamePillar(world, user, this.pillarSize, i * 2, DamagingWarmupEntityEvents.SPAWN_FIRE);
                        pillar.setDamage(this.baseDamage + lvl * this.bonusDamagePerLvl);
                        pillar.setPos(x, y, z);
                        world.spawnEntity(pillar);
                        i++;
                    }
                }
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.chaos_storm").formatted(Formatting.RED),
                Text.translatable("tooltip.soulsweapons.chaos_storm.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_storm.2").formatted(Formatting.GRAY)
        );
    }
}
