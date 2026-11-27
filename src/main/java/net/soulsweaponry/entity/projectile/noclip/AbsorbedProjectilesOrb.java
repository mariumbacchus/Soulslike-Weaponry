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

    public static final String REFLECTED_TAG = "soulsweaponry_reflected";
    public static final int REFLECTED_LIFETIME = 100;
    private final List<NbtCompound> projectiles = new ArrayList<>();
    private int currentIndex = 0;
    private UUID targetUuid;

    public AbsorbedProjectilesOrb(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public AbsorbedProjectilesOrb(World world, LivingEntity target) {
        super(EntityRegistry.ABSORBED_PROJECTILES_ORB_ENTITY, world);
        this.targetUuid = target.getUuid();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            if (this.age % 5 == 0) {
                List<Vec3d> list = ParticleHandler.getSphereParticleCords(50, 1f);
                for (Vec3d vec : list) {
                    this.getWorld().addParticle(ParticleRegistry.NIGHTFALL_PARTICLE,
                            this.getX() + vec.getX(),
                            this.getY() + vec.getY(),
                            this.getZ() + vec.getZ(),
                            0, 0, 0);
                }
            }
        } else {
            if (this.projectiles.isEmpty()) {
                this.discard();
                return;
            }
            Entity target = this.getTarget();
            if (target == null || !target.isAlive()) {
                this.discard();
                return;
            }
            if (this.age > 20 && this.age % 5 == 0) {
                this.fireProjectile(target);
                if (++this.currentIndex >= this.projectiles.size()) {
                    this.discard();
                }
            }
        }
    }

    private void fireProjectile(Entity target) {
        NbtCompound nbt = this.projectiles.get(this.currentIndex).copy();
        float damage = nbt.getFloat("SoulsweaponryAbsorbedDamage");

        // Give cloned entity a new identity
        nbt.remove("UUID");
        nbt.remove("Passengers");
        nbt.remove("SoulsweaponryAbsorbedDamage");

        // Reset the projectile's previous flight state so that tridents don't want to travel back to user
        nbt.putBoolean("DealtDamage", false);
        nbt.putBoolean("ShouldReturn", false);
        nbt.putBoolean("inGround", false);
        nbt.putShort("life", (short) 0);

        try {
            Entity entity = EntityType.getEntityFromNbt(nbt, this.getWorld()).orElse(null);
            if (!(entity instanceof ProjectileEntity projectile)) {
                return;
            }
            // Shoot from the center of the orb toward the center of the target
            Vec3d start = this.getBoundingBox().getCenter();
            Vec3d targetPos = target.getEyePos();
            Vec3d direction = targetPos.subtract(start);

            entity.setUuid(UUID.randomUUID());
            entity.setPosition(start.x, start.y, start.z);
            projectile.setOwner(this.getOwner());
            projectile.setVelocity(direction.x, direction.y, direction.z, 1.6F, 0F);

            if (projectile instanceof PersistentProjectileEntity persistent) {
                persistent.setDamage(damage);
                persistent.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            }

            // Store despawn safeguard to make sure other modded projectiles despawns after set time
            entity.age = 0;
            entity.addCommandTag(REFLECTED_TAG);
            this.getWorld().spawnEntity(entity);
        } catch (Exception e) {
            SoulsWeaponry.LOGGER.warn("Failed to recreate absorbed projectile", e);
        }
    }

    @Nullable
    public Entity getTarget() {
        if (this.getWorld() instanceof ServerWorld world && this.targetUuid != null) {
            return world.getEntity(this.targetUuid);
        }
        return null;
    }

    public void setProjectiles(List<NbtCompound> projectiles) {
        this.projectiles.clear();
        for (NbtCompound nbt : projectiles) {
            this.projectiles.add(nbt.copy());
        }
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
        if (this.targetUuid != null) {
            nbt.putUuid("Target", this.targetUuid);
        }
    }
}