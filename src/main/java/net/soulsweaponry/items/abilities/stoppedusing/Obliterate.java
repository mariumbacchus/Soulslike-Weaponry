package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record Obliterate(
        float baseDamage, float bonusDamagePerLvl, float enchantBonusModifier,
        float yVelocity, double aoeExpansion, double rangeOutwards,
        int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            if (ticksUsed >= 10 && world instanceof ServerWorld serverWorld) {
                this.applyItemCooldown(stack.getItem(), player, this.getScaledCooldownSmash(stack));
                stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
                Vec3d vecBlocksAway = player.getRotationVector().multiply(this.rangeOutwards).add(player.getPos());
                BlockPos targetArea = new BlockPos((int)vecBlocksAway.x, (int) user.getY(), (int) vecBlocksAway.z);
                Box aoe = new Box(targetArea).expand(this.aoeExpansion);
                List<Entity> entities = world.getOtherEntities(player, aoe);
                float power = this.baseDamage + WeaponUtil.getUpgradeLevel(stack) * this.bonusDamagePerLvl;
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity target) {
                        entity.damage(DamageSourceRegistry.create(world, DamageSourceRegistry.OBLITERATED, player),
                                power + this.enchantBonusModifier * EnchantmentHelper.getDamage(serverWorld, stack, target, world.getDamageSources().playerAttack(player), 0));
                        entity.addVelocity(0, this.yVelocity, 0);
                    }
                }
                world.playSound(null, targetArea, SoundRegistry.NIGHTFALL_BONK_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                ParticleHandler.particleOutburstMap(world, 150, targetArea.getX(), targetArea.getY() + .1f, targetArea.getZ(), ParticleEvents.OBLITERATE_MAP, 1f);
            }
        }
    }

    private int getScaledCooldownSmash(ItemStack stack) {
        return Math.max(this.minCooldown, this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.obliterate").formatted(Formatting.DARK_BLUE),
                Text.translatable("tooltip.soulsweapons.obliterate.description.1").formatted(Formatting.GRAY)
        );
    }
}
