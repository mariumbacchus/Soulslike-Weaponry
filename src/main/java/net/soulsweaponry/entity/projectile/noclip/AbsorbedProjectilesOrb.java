package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AbsorbedProjectilesOrb extends NoClipEntity {

    private final List<EntityType<?>> projectileTypes = new ArrayList<>();
    private final List<Float> projectileDamage = new ArrayList<>();
    private int currentIndex = 0;
    private UUID targetUuid;

    public AbsorbedProjectilesOrb(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public AbsorbedProjectilesOrb(World world, LivingEntity target) {
        super(EntityRegistry.ABSORBED_PROJECTILES_ORB_ENTITY.get(), world);
        this.targetUuid = target.getUuid();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            if (this.age % 5 == 0) {
                List<Vec3d> list = ParticleHandler.getSphereParticleCords(50, 1f);
                for (Vec3d vec : list) {
                    this.getWorld().addParticle(ParticleRegistry.NIGHTFALL_PARTICLE.get(),
                            this.getX() + vec.getX(),
                            this.getY() + vec.getY(),
                            this.getZ() + vec.getZ(),
                            0, 0, 0);
                }
            }
        } else {
            if (this.age > 20 && this.age % 5 == 0 && !projectileTypes.isEmpty() && this.getTarget() != null) {
                try {
                    Entity target = this.getTarget();
                    double d = target.getX() - this.getX();
                    double e = target.getEyeY() - this.getBodyY(0.5f);
                    double f = target.getZ() - this.getZ();
                    double g = Math.sqrt(d * d + f * f);
                    EntityType<?> entityType = projectileTypes.get(currentIndex);
                    float damage = projectileDamage.get(currentIndex);
                    Entity entity = entityType.create(this.getWorld());
                    if (entity instanceof ProjectileEntity projectile) {
                        entity.setPos(this.getX(), this.getEyeY(), this.getZ());
                        projectile.setVelocity(d, e + g * 0.2F, f, 1.6F, 1f);
                        projectile.setOwner(this.getOwner());
                        if (projectile instanceof PersistentProjectileEntity persistent) {
                            persistent.setDamage(damage);
                        }
                        // Only spawn if it was a projectile
                        this.getWorld().spawnEntity(entity);
                    }
                    this.currentIndex++;
                    if (currentIndex >= projectileTypes.size()) {
                        this.discard();
                    }
                } catch (Exception e) {
                    SoulsWeaponry.LOGGER.warn("Current index exceeded amount in list. Discarding entity.");
                    this.discard();
                }
            }
            if (projectileTypes.isEmpty()) {
                this.discard();
            }
        }
    }

    @Nullable
    public Entity getTarget() {
        if (!this.getWorld().isClient) {
            return ((ServerWorld)this.getWorld()).getEntity(this.targetUuid);
        }
        return null;
    }

    public void setProjectiles(List<EntityType<?>> projectileTypes, List<Float> projectileDamage) {
        this.projectileTypes.addAll(projectileTypes);
        this.projectileDamage.addAll(projectileDamage);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Target")) {
            this.targetUuid = nbt.getUuid("Target");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putUuid("Target", this.targetUuid);
    }
}