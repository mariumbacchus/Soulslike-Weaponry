package net.soulsweaponry.entity.projectile;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleNetworking;

import java.util.List;

public class FlamePillar extends InvisibleEntity {

    private int warmup;
    public int maxTicks = 15;
    private int ticksLeft = maxTicks;

    public FlamePillar(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setWarmup(int warmup) {
        this.warmup = warmup;
    }

    public int getWarmup() {
        return this.warmup;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            this.setWarmup(this.getWarmup() - 1);
            if (this.getWarmup() < 0) {
                if (this.getWarmup() == -7) {
                    if (!this.getWorld().isClient) {
                        ParticleNetworking.sendServerParticlePacket((ServerWorld) this.getWorld(), PacketRegistry.FLAME_RUPTURE_ID, this.getBlockPos());
                    }
                    List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(0.2D));
                    for (LivingEntity livingEntity : list) {
                        this.damage(livingEntity);
                    }
                    this.getWorld().playSound(null, this.getBlockPos(), SoundRegistry.DAY_STALKER_CHAOS_STORM, SoundCategory.HOSTILE, 1f, 1f);
                    if (this.getWorld().getBlockState(this.getBlockPos()).isAir()) {
                        this.getWorld().setBlockState(this.getBlockPos(), Blocks.FIRE.getDefaultState());
                    }
                }
                if (--this.ticksLeft < 0) {
                    this.discard();
                }
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    private void damage(LivingEntity target) {
        Entity entity = this.getOwner();
        if (target.isAlive() && !target.isInvulnerable() && entity instanceof LivingEntity livingEntity && target != livingEntity) {
            if (!livingEntity.isTeammate(target)) {
                if (target.damage(DamageSource.mob(livingEntity), (float) this.getDamage())) {
                    target.addVelocity(0, 0.5f, 0);
                }
            }
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Warmup")) {
            this.warmup = nbt.getInt("Warmup");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Warmup", this.warmup);
    }
}
