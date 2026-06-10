package net.soulsweaponry.entity.mobs;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.config.EntityConfig;

public class SoulReaperGhost extends Remnant {

    public SoulReaperGhost(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createGhostAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 30D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, EntityConfig.familiar_ghost_health)
                .add(EntityAttributes.GENERIC_ARMOR, EntityConfig.familiar_ghost_armor)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3000000003D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.getWorld().addParticle(ParticleTypes.SOUL, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void initEquip() {
    }

    @Override
    protected SoundEvent getStepSound() {
        return null;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }
    
}
