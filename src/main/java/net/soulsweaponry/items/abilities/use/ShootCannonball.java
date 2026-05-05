package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.Cannonball;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import net.soulsweaponry.util.WeaponUtil;

public class ShootCannonball extends ShootSilverBullet {

    private final float launchPower;

    public ShootCannonball(
            float damage, float velocity, float divergence,
            int postureLoss, float postureLossPerVisceral,
            int projectileCount, float projectileCountPerLvl,
            int bulletsNeededWithInfinity, int bulletsNeeded,
            int levelToUnlockInfinity, int stackDamage,
            int maxProjectileAge, int maxProjectileAgeEthereal,
            int minCooldown, int cooldown, int reducedCooldownPerFastHands,
            int particleAmount, float particleSpread, float launchPower,
            boolean bypassIFrames
    ) {
        super(
                damage, velocity, divergence, postureLoss, postureLossPerVisceral,
                projectileCount, projectileCountPerLvl, bulletsNeededWithInfinity,
                bulletsNeeded, levelToUnlockInfinity, stackDamage, maxProjectileAge,
                maxProjectileAgeEthereal, minCooldown, cooldown, reducedCooldownPerFastHands,
                particleAmount, particleSpread, bypassIFrames
        );
        this.launchPower = launchPower;
    }

    @Override
    public int getCooldown(ItemStack stack) {
        return super.getCooldown(stack) + (this.hasInfinity(stack) ? 30 : 0);
    }

    @Override
    public SilverBulletEntity getModdedProjectile(World world, LivingEntity shooter, ItemStack gunStack) {
        return new Cannonball(world, shooter);
    }

    @Override
    public void postShot(World world, PlayerEntity user, ItemStack stack) {
        super.postShot(world, user, stack);
        WeaponUtil.launchTarget(user, this.launchPower, true);
    }
}
