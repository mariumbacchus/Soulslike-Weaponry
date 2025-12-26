package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.entity.projectile.arrow.ChargedArrow;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record Cloudburst(
        float damage, float damagePerLvl, float velocity, float velocityPerLvl,
        int speedDuration, float speedDurationPerLvl, int speedAmp, float speedAmpPerLvl,
        int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IKeybindAbility {

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (!this.hasCooldownEffect(player)) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            if (!player.isCreative()) {
                int cooldown = Math.max(this.minCooldown, this.cooldown - lvl * this.reducedCooldownPerLvl);
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.COOLDOWN, cooldown, 0));
            }
            ItemStack arrowStack = player.getProjectileType(stack);
            if (arrowStack.isEmpty()) {
                arrowStack = new ItemStack(Items.ARROW);
            }
            if (player.getAttacking() != null && !player.getAttacking().isTeammate(player)) {
                LivingEntity target = player.getAttacking();
                double x = target.getX() - player.getX();
                double y = target.getEyeY() - player.getBodyY(1f);
                double z = target.getZ() - player.getZ();
                this.shootArrow(world, stack, arrowStack, player, new Vec3d(x, y, z));
            } else {
                this.shootArrow(world, stack, arrowStack, player, null);
            }
        }
    }

    public void shootArrow(ServerWorld world, ItemStack stack, ItemStack arrowStack, PlayerEntity player, @Nullable Vec3d currentTargetPos) {
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        float velocity = this.velocity + this.velocityPerLvl * lvl;
        double damage = (this.damage + this.damagePerLvl * lvl) / velocity;
        ChargedArrow chargedArrow = new ChargedArrow(world, player, arrowStack, stack, true);
        chargedArrow.setPos(player.getX(), player.getY() + 1.5F, player.getZ());
        if (currentTargetPos != null) {
            chargedArrow.setVelocity(currentTargetPos.getX(), currentTargetPos.getY(), currentTargetPos.getZ(), velocity, 1f);
        } else {
            chargedArrow.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, velocity, 1.0F);
        }
        chargedArrow.setCritical(true);
        chargedArrow.setDamage(damage);
        chargedArrow.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        world.spawnEntity(chargedArrow);

        stack.damage(1, player, WeaponUtil.getActiveHandSlot(player));
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
        player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, (int) (this.speedDuration + this.speedDurationPerLvl * lvl), (int) (this.speedAmp + this.speedAmpPerLvl * lvl)));
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (!this.hasCooldownEffect(player)) {
            WeaponUtil.launchTarget(player, 2f, false);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.cloudburst").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.cloudburst.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.cloudburst.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.cloudburst.3").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.cloudburst.4").formatted(Formatting.DARK_GRAY)
        );
    }
}
