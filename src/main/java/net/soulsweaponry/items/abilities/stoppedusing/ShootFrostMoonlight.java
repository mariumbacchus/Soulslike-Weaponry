package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;

public class ShootFrostMoonlight extends ShootMoonlight {

    private final int permafrostAmp;
    private final float bonusAmpPerLvl;
    private final int permafrostDuration;
    private final float bonusDurationPerLvl;

    public ShootFrostMoonlight(int projectileAmount, float bonusProjectilesPerLvl,
                               float speed, float damage, float bonusDamagePerLvl,
                               int permafrostAmp, float bonusAmpPerLvl,
                               int permafrostDuration, float bonusDurationPerLvl
    ) {
        super(projectileAmount, bonusProjectilesPerLvl, speed, damage, bonusDamagePerLvl);
        this.permafrostAmp = permafrostAmp;
        this.bonusAmpPerLvl = bonusAmpPerLvl;
        this.permafrostDuration = permafrostDuration;
        this.bonusDurationPerLvl = bonusDurationPerLvl;
    }

    @Override
    public MoonlightProjectile createMoonlightProjectile(World world, LivingEntity user, ItemStack stack, int projectileNr, int lvl) {
        int duration = (int) (this.permafrostDuration + this.bonusDurationPerLvl * lvl);
        int amp = (int) (this.permafrostAmp + this.bonusAmpPerLvl * lvl);
        MoonlightProjectile entity = super.createMoonlightProjectile(world, user, stack, projectileNr, lvl);
        entity.setAppliedStatusEffect(EffectRegistry.FREEZING.get());
        entity.setEffectAmplifier(amp);
        entity.setAppliedEffectDuration(duration);
        entity.setAreaParticleCount((byte) 8);
        return entity;
    }

    @Override
    public EntityType<? extends MoonlightProjectile> getMoonlightType() {
        return EntityRegistry.DARK_MOON_PROJECTILE.get();
    }
}
