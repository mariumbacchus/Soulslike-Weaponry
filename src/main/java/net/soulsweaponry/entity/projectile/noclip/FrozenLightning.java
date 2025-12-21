package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.posthit.Permafrost;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;

import java.util.ArrayList;
import java.util.List;

public class FrozenLightning extends Entity {

    public List<Long> seeds = new ArrayList<>();
    private int health = 10;

    public FrozenLightning(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
        for (int i = 0; i < 5; i++) {
            this.seeds.add(this.random.nextLong());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age % 30 == 0 && this.random.nextBoolean()) {
            this.playSound(SoundEvents.ENTITY_GUARDIAN_ATTACK, 1f, this.random.nextBetween(4, 8) / 10f);
        }
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean damage(ServerWorld serverWorld, DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity) {
            if (this.getWorld().isClient) {
                ParticleHandler.particleSphere(this.getWorld(), 100, this.getX(), this.getBodyY(0.5f), this.getZ(), ParticleEvents.ICE_PARTICLE, 1f);
            }
            this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 0.85f - (float)health / 40f);
            this.health--;
            if (this.health <= 0) {
                if (this.getWorld().isClient) {
                    ParticleHandler.particleSphere(this.getWorld(), 500, this.getX(), this.getBodyY(0.25f), this.getZ(), ParticleEvents.ICE_PARTICLE, 2f);
                }
                this.getWorld().createExplosion(null, this.getX(), this.getY(), this.getZ(), 6.0F, true, World.ExplosionSourceType.TNT);
                Permafrost.iceExplosion(getWorld(), this.getBlockPos(), this, 7.5f, 5);
                LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.getWorld(), SpawnReason.EVENT);
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(this.getBlockPos()));
                lightningEntity.setChanneler(null);
                this.getWorld().spawnEntity(lightningEntity);
                this.playSound(SoundEvents.ITEM_TRIDENT_THUNDER.value(), 1f, 1.0f);
                this.discard();
            }
        }
        return false;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        long[] seedArray = this.seeds.stream().mapToLong(Long::longValue).toArray();
        nbt.putLongArray("seeds", seedArray);
        nbt.putInt("health", this.health);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("seeds")) {
            long[] seedArray = nbt.getLongArray("seeds");
            this.seeds.clear();
            for (long seed : seedArray) {
                this.seeds.add(seed);
            }
        } else if (nbt.contains("seed")) {
            this.seeds.clear();
            this.seeds.add(nbt.getLong("seed"));
        }
        if (nbt.contains("health")) {
            this.health = nbt.getInt("health");
        }
    }
}
