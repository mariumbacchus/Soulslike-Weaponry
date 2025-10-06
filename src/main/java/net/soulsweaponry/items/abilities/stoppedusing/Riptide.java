package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

/**
 * Launches the player when stopped using and while sneaking.
 * @param launchPower
 * @param bonusPowerPerLvl
 * @param collisionDamage
 * @param calculatedFallDuration
 * @param calculatedFallAmp used as the base damage for the ground detonation (Meteor Strike)
 * @param minCooldown
 * @param cooldown
 * @param reducedCooldownPerLvl
 */
public record Riptide(float launchPower, float bonusPowerPerLvl, float collisionDamage, int calculatedFallDuration, int calculatedFallAmp, int minCooldown, int cooldown, int reducedCooldownPerLvl) implements IAbility {

    @Override
    public void sneakingOnStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            if (remainingUseTicks >= 10) {
                int level = WeaponUtil.getUpgradeLevel(stack);
                WeaponUtil.launchTarget(user, this.launchPower + level * this.bonusPowerPerLvl, false);
                playerEntity.useRiptide(20, this.collisionDamage, stack);
                world.playSoundFromEntity(null, playerEntity, SoundEvents.ITEM_TRIDENT_RIPTIDE_3.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (playerEntity.isOnGround()) {
                    playerEntity.move(MovementType.SELF, new Vec3d(0.0D, 1.1999999284744263D, 0.0D));
                }
                //NOTE: Ground Smash method is in parent class DetonateGroundItem
                user.addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL, this.calculatedFallDuration, this.calculatedFallAmp));
                this.applyItemCooldown(stack.getItem(), playerEntity, Math.max(this.minCooldown, this.cooldown - level * this.reducedCooldownPerLvl));
                stack.damage(4, playerEntity, WeaponUtil.getActiveHandSlot(playerEntity));
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                //TODO
        );
    }
}
