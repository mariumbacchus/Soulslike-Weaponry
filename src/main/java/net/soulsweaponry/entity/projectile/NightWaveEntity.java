package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.List;

public class NightWaveEntity extends InvisibleEntity {

    public NightWaveEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            for (int i = 0; i < 40; ++i) {
                this.getWorld().addParticle(ParticleRegistry.NIGHTFALL_PARTICLE, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0D, 0.0D, 0.0D);
            }
        } else {
            if (this.age % 4 == 0) {
                List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(0.2D));
                for (LivingEntity living : list) {
                    if (this.getOwner() instanceof LivingEntity) {
                        living.damage(DamageSource.mobProjectile(this, (LivingEntity) this.getOwner()), (float) this.getDamage());
                    } else {
                        living.damage(DamageSource.mobProjectile(this, null), 20f * ConfigConstructor.night_prowler_damage_modifier);
                    }
                }
            }
            if (this.age % 10 == 0) {
                this.playSound(SoundRegistry.MOONLIGHT_BIG_EVENT, 0.4f, 0.75f);
            }
        }
        if (this.age > 15) {
            this.discard();
        }
    }
}