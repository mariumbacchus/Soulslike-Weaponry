package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Launches the player when stopped using in offhand.
 * @param launchPower
 * @param bonusPowerPerLvl
 * @param collisionDamage
 * @param calculatedFallDuration
 * @param calculatedFallAmp used as the base damage for the ground detonation (Meteor Strike)
 * @param minCooldown
 * @param cooldown
 * @param reducedCooldownPerLvl
 */
public record Riptide(
        float launchPower, float bonusPowerPerLvl, float collisionDamage,
        int calculatedFallDuration, int calculatedFallAmp,
        int minCooldown, int cooldown, int reducedCooldownPerLvl,
        Condition<World, LivingEntity> shouldApplyCooldown
) implements ISneakChargeToUse {

    public static final Condition<World, LivingEntity> ALWAYS_COOLDOWN = Condition.of(Text.literal(""), ((world, user) -> true));
    public static final Condition<World, LivingEntity> RAINING = Condition.of(Text.translatable("tooltip.soulsweapons.riptide.raining"), (world, user) -> world.isRaining());

    public Riptide(float launchPower, float bonusPowerPerLvl, float collisionDamage, int minCooldown, int cooldown, int reducedCooldownPerLvl, Condition<World, LivingEntity> shouldApplyCooldown) {
        this(launchPower, bonusPowerPerLvl, collisionDamage, 0, 0, minCooldown, cooldown, reducedCooldownPerLvl, shouldApplyCooldown);
    }

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
                if (this.calculatedFallAmp > 0) {
                    //NOTE: Ground Smash method is in parent class DetonateGroundItem
                    user.addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL, this.calculatedFallDuration, this.calculatedFallAmp));
                }
                if (this.shouldApplyCooldown.test(world, user)) {
                    this.applyItemCooldown(stack.getItem(), playerEntity, Math.max(this.minCooldown, this.cooldown - level * this.reducedCooldownPerLvl));
                }
                stack.damage(3, playerEntity, WeaponUtil.getActiveHandSlot(playerEntity));
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        tooltip.add(Text.translatable("tooltip.soulsweapons.riptide").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("tooltip.soulsweapons.riptide.1").formatted(Formatting.GRAY));
        if (this.calculatedFallAmp > 0) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.riptide.2").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.riptide.3").formatted(Formatting.GRAY));
        }
        if (!Objects.equals(this.shouldApplyCooldown.label().getLiteralString(), "")) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.riptide.4").append(this.shouldApplyCooldown.label()).formatted(Formatting.GRAY));
        }
        return tooltip;
    }

    public interface Condition<C, R> extends BiPredicate<C, R> {
        Text label();

        static <C, R> Condition<C, R> of(Text label, BiPredicate<C, R> pred) {

            return new Condition<>() {

                @Override public Text label() {
                    return label;
                }

                @Override public boolean test(C ctx, R ctx2) {
                    return pred.test(ctx, ctx2);
                }
            };
        }
    }
}
