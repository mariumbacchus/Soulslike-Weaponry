package net.soulsweaponry.items.abilities.supernova;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.noclip.DamagingWarmupEntityEvents;
import net.soulsweaponry.entity.projectile.noclip.FlamePillar;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record Flameburst(float moltenMetalBaseRadius, float moltenMetalRadiusPerLvl,
                         float moltenMetalBaseDamage, float moltenMetalDamagePerLvl,
                         int flamePillarAmount,
                         float flamePillarBaseRadius, float flamePillarRadiusPerLvl,
                         float flamePillarBaseDamage, float flamePillarDamagePerLvl,
                         int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IAbility {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            if (remainingUseTicks >= 10) {
                if (!world.isClient) {
                    int level = WeaponUtil.getUpgradeLevel(stack);
                    float radius = this.moltenMetalBaseRadius + this.moltenMetalRadiusPerLvl * level;
                    float damage = this.moltenMetalBaseDamage + this.moltenMetalDamagePerLvl * level;
                    WeaponUtil.doConsumerOnLine(world, user.getYaw() + 90, user.getPos(), 4, this.flamePillarAmount, 1.75f,
                            (Vec3d position, Integer warmup, Float yaw) -> {
                                FlamePillar pillar = new FlamePillar(world, user, this.flamePillarBaseRadius + this.flamePillarRadiusPerLvl * level, warmup, DamagingWarmupEntityEvents.SPAWN_MOLTEN_METAL);
                                pillar.setYaw(yaw);
                                pillar.setOtherAttributes(new DamagingWarmupEntityEvents.OtherAttributes(damage, radius));
                                pillar.setDamage(this.flamePillarBaseDamage + this.flamePillarDamagePerLvl * level);
                                pillar.setPos(position.getX(), position.getY(), position.getZ());
                                world.spawnEntity(pillar);
                            }
                    );
                    this.applyItemCooldown(stack.getItem(), player, Math.max(this.minCooldown, this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl));
                }
            }
        }
    }

    @Override
    public boolean isChargeToUse() {
        return true;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.flameburst").formatted(Formatting.RED),
                Text.translatable("tooltip.soulsweapons.flameburst.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.flameburst.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.flameburst.3").formatted(Formatting.GRAY)
        );
    }
}
