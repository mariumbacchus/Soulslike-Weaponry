package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAbility;
import net.soulsweaponry.items.abilities.detonateground.IDetonateGround;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

/**
 * Use to gain {@link EffectRegistry#CALCULATED_FALL} and leap into the air.
 * It is up to the {@link DetonateGroundAbility} to determine the damage and effects.
 * <p>
 * Not fully charging/using the weapon triggers the {@link EffectRegistry#CALCULATED_FALL}
 * explosion right away.
 */
public record SwordLeap(
        float damage, float bonusDamagePerLvl, int calculatedFallDuration, float yVelocity,
        int minCooldown, int cooldown, int reducedCooldownPerLvl,
        float cooldownModFullyCharged, float cooldownModNotCharged
) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity player) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            int amp = (int) (this.damage + this.bonusDamagePerLvl * lvl);
            int cooldown = Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * lvl);
            user.addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL, this.calculatedFallDuration, amp));
            if (ticksUsed >= 10) {
                cooldown = (int) (cooldown * this.cooldownModFullyCharged);
                Vec3d rotation = player.getRotationVector().multiply(1f);
                player.addVelocity(rotation.getX(), 1, rotation.getZ());
                world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1f, 1f);
            } else {
                IDetonateGround.triggerCalculateFall(user, 0, world.getDamageSources().fall());
                cooldown = (int) (cooldown * this.cooldownModNotCharged);
            }
            stack.damage(3, user, WeaponUtil.getActiveHandSlot(player));
            this.applyItemCooldown(stack, player, cooldown);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.sword_leap").formatted(Formatting.DARK_PURPLE),
                Text.translatable("tooltip.soulsweapons.sword_leap.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.sword_leap.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.sword_leap.3").formatted(Formatting.GRAY)
        );
    }
}
