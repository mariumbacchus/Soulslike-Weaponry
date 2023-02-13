package net.soulsweaponry.entity.mobs;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoulReaperGhost extends Remnant {

    public SoulReaperGhost(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.world.addParticle(ParticleTypes.SOUL, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.0D, 0.0D, 0.0D);
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
