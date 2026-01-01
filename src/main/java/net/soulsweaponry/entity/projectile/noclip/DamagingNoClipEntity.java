package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class DamagingNoClipEntity extends NoClipEntity {

    private final Set<UUID> entitiesHit = new HashSet<>();

    public DamagingNoClipEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setMaxAge(100);
    }

    public DamagingNoClipEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world, LivingEntity owner, int maxAge) {
        super(entityType, world);
        this.setMaxAge(maxAge);
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        super.tick();
        // Fixes weird rotation due to the entity being noclip
        Vec3d v = this.getVelocity();
        if (v.lengthSquared() > 1.0E-7) {
            float yaw = (float)(MathHelper.atan2(v.x, v.z) * 180.0F / (float)Math.PI);
            float pitch = (float)(MathHelper.atan2(v.y, v.horizontalLength()) * 180.0F / (float)Math.PI);

            this.setYaw(yaw);
            this.setPitch(pitch);
            this.prevYaw = yaw;
            this.prevPitch = pitch;
        }

        if (this.getWorld() instanceof ServerWorld serverWorld) {
            List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(0.4D));
            DamageSource source;
            for (LivingEntity living : list) {
                if (this.isOwner(living) || this.entitiesHit.contains(living.getUuid())) {
                    continue;
                }
                if (this.getOwner() instanceof LivingEntity) {
                    source = this.getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner());
                } else {
                    source = this.getDamageSources().mobProjectile(this, null);
                }
                this.updateEntityDamage(serverWorld, living, source);
                boolean wasHit = living.damage(serverWorld, source, (float) this.getDamage());
                this.applyDamageEffects(wasHit, living);
                this.entitiesHit.add(living.getUuid());
            }
        }
        if (this.age > this.getMaxAge()) {
            this.discard();
        }
    }

    /**
     * Apply custom effects after damage has been dealt, like adding effects.
     */
    public abstract void applyDamageEffects(boolean wasHit, LivingEntity target);

    public void updateEntityDamage(ServerWorld serverWorld, LivingEntity target, DamageSource source) {
        this.setDamage(this.getDamage() + EnchantmentHelper.getDamage(serverWorld, this.getItemStack(), target, source, 0));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("EntitiesHit", 9)) {
            NbtList list = nbt.getList("EntitiesHit", 10);
            this.entitiesHit.clear();
            for (int i = 0; i < list.size(); ++i) {
                NbtCompound tag = list.getCompound(i);
                this.entitiesHit.add(tag.getUuid("UUID"));
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        NbtList list = new NbtList();
        for (UUID uuid : this.entitiesHit) {
            list.add(this.saveUuid(uuid));
        }
        nbt.put("EntitiesHit", list);
    }

    private NbtCompound saveUuid(UUID uuid) {
        NbtCompound tag = new NbtCompound();
        tag.putUuid("UUID", uuid);
        return tag;
    }
}
