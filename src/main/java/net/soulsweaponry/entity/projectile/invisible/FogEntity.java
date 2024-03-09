package net.soulsweaponry.entity.projectile.invisible;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;

public class FogEntity extends InvisibleEntity {

    public FogEntity(EntityType<? extends FogEntity> entityType, World world) {
        super(entityType, world, Items.AIR.getDefaultStack());
    }

    public FogEntity(EntityType<? extends FogEntity> entityType, World world, ItemStack stack) {
        super(entityType, world, stack);
    }

    public FogEntity(World world, LivingEntity owner) {
        super(EntityRegistry.FOG_ENTITY, world, owner);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            for (int i = 0; i < 60; ++i) {
                this.getWorld().addParticle(ParticleTypes.LARGE_SMOKE, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0D, 0.0D, 0.0D);
            }
        } else {
            for (Entity entity : this.getWorld().getOtherEntities(this, this.getBoundingBox())) {
                if (entity instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 80, 0));
                }
            }
            if (this.age % 10 == 0) {
                this.playSound(SoundRegistry.ENGULF, 0.4f, 1f);
            }
        }
        if (this.age > 100) {
            this.discard();
        }
    }
}
