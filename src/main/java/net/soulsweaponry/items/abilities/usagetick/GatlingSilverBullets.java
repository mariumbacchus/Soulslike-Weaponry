package net.soulsweaponry.items.abilities.usagetick;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.use.ShootSilverBullet;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class GatlingSilverBullets extends ShootSilverBullet implements IChargeUsageTicks {

    private final int maxUseTime;
    private final int useTimePerFastHands;
    private final int updateTick;

    public GatlingSilverBullets(
            float damage, float velocity, float divergence,
            int postureLoss, float postureLossPerVisceral,
            int projectileCount, float projectileCountPerLvl,
            int bulletsNeededWithInfinity, int bulletsNeeded,
            int levelToUnlockInfinity, int stackDamage, int maxProjectileAge,
            int maxProjectileAgeEthereal, int minCooldown, int cooldown,
            int reducedCooldownPerFastHands, int particleAmount, float particleSpread,
            int maxUseTime, int useTimePerFastHands, int updateTick,
            boolean bypassIFrames
    ) {
        super(
                damage, velocity, divergence, postureLoss, postureLossPerVisceral,
                projectileCount, projectileCountPerLvl, bulletsNeededWithInfinity,
                bulletsNeeded, levelToUnlockInfinity, stackDamage, maxProjectileAge,
                maxProjectileAgeEthereal, minCooldown, cooldown, reducedCooldownPerFastHands,
                particleAmount, particleSpread, bypassIFrames
        );
        this.maxUseTime = maxUseTime;
        this.useTimePerFastHands = useTimePerFastHands;
        this.updateTick = updateTick;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks > 0) {
            if (remainingUseTicks == this.getMaxUseTime(stack, user)) {
                world.playSound(user, user.getBlockPos(), SoundRegistry.GATLING_GUN_STARTUP_EVENT, SoundCategory.PLAYERS, 1f, 1f);
            }
            if (remainingUseTicks < this.getMaxUseTime(stack, user) - 15 && remainingUseTicks % this.updateTick == 0 && user instanceof PlayerEntity playerEntity) {
                ItemStack itemStack = this.canShoot(playerEntity, stack);
                if (itemStack != null) {
                    this.shootProjectiles(world, playerEntity, stack);
                    this.spawnShotParticles(world, playerEntity, this.particleAmount + WeaponUtil.getLevel(stack, EnchantRegistry.FAST_HANDS), this.particleSpread);
                    world.playSound(playerEntity, user.getBlockPos(), SoundRegistry.GATLING_GUN_BARRAGE_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                }
            }
        } else {
            user.stopUsingItem();
        }
    }

    @Override
    public int getCooldown(ItemStack stack, int ticksUsed) {
        return Math.max(this.minCooldown, this.cooldown - this.getReducedCooldown(stack) - ticksUsed + (this.hasInfinity(stack) ? 30 : 0));
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        int lvl = WeaponUtil.getLevel(stack, EnchantRegistry.FAST_HANDS);
        return this.maxUseTime + this.useTimePerFastHands * lvl;
    }

    @Override
    public UseAction getUseAction() {
        return UseAction.BOW;
    }

    @Override
    public void stop(LivingEntity user, ItemStack stack, int ticksUsed) {
        IChargeUsageTicks.super.stop(user, stack, ticksUsed);
        user.getWorld().playSound(null, user.getBlockPos(), SoundRegistry.GATLING_GUN_STOP_EVENT, SoundCategory.PLAYERS, 1f, 1f);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = super.getTooltipAbilities(stack);
        tooltip.add(Text.translatable("tooltip.soulsweapons.gun_max_use_time").append(Text.literal(String.valueOf(this.getMaxUseTime(stack, null)))).formatted(Formatting.GRAY));
        return tooltip;
    }
}
