package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.entity.AreaEffectSphere;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;

import java.util.List;

public class DragonStaffProjectile extends DragonFireballEntity {

    private float radius = 2f;
    private int maxAge = 100;
    private int duration = 200;
    private float radiusGrowth = 1f;
    private int effectDuration = 50;
    private int effectAmp = (int) WeaponConfig.dragon_staff_projectile_cloud_effect_amp;

    public DragonStaffProjectile(EntityType<? extends DragonStaffProjectile> entityType, World world) {
        super(entityType, world);
    }

    public DragonStaffProjectile(World world, LivingEntity user) {
        super(EntityRegistry.DRAGON_STAFF_PROJECTILE, world);
        this.setOwner(user);
        this.setRotation(user.getYaw(), user.getPitch());
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult)hitResult);
            this.getWorld().emitGameEvent(GameEvent.PROJECTILE_LAND, hitResult.getPos(), GameEvent.Emitter.of(this, null));
        } else if (type == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            this.onBlockHit(blockHitResult);
            BlockPos blockPos = blockHitResult.getBlockPos();
            this.getWorld().emitGameEvent(GameEvent.PROJECTILE_LAND, blockPos, GameEvent.Emitter.of(this, this.getWorld().getBlockState(blockPos)));
        }
        this.detonate();
    }

    private void detonate() {
        if (!this.getWorld().isClient) {
            List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(4.0, 2.0, 4.0));
            AreaEffectSphere areaEffectCloudEntity = new AreaEffectSphere(this.getWorld(), this.getX(), this.getY(), this.getZ());
            Entity entity = this.getOwner();
            if (entity instanceof LivingEntity) {
                areaEffectCloudEntity.setOwner((LivingEntity)entity);
            }
            areaEffectCloudEntity.setParticleType(ParticleTypes.DRAGON_BREATH);
            areaEffectCloudEntity.setRadius(this.radius);
            areaEffectCloudEntity.setDuration(this.duration);
            areaEffectCloudEntity.setRadiusGrowth(this.radiusGrowth / (float) areaEffectCloudEntity.getDuration());
            areaEffectCloudEntity.addEffect(new StatusEffectInstance(EffectRegistry.HALLOWED_DRAGON_MIST, this.effectDuration, this.effectAmp));
            if (!list.isEmpty()) {
                for (LivingEntity livingEntity : list) {
                    double d = this.squaredDistanceTo(livingEntity);
                    if (!(d < 16.0)) continue;
                    areaEffectCloudEntity.setPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                    break;
                }
            }
            this.getWorld().syncWorldEvent(WorldEvents.DRAGON_BREATH_CLOUD_SPAWNS, this.getBlockPos(), this.isSilent() ? -1 : 1);
            this.getWorld().spawnEntity(areaEffectCloudEntity);
            this.discard();
        }
    }

    public void setCloudRadius(float radius) {
        this.radius = radius;
    }

    public void setProjectileMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setCloudDuration(int duration) {
        this.duration = duration;
    }

    public void setEffectAmp(int effectAmp) {
        this.effectAmp = effectAmp;
    }

    public void setEffectDuration(int effectDuration) {
        this.effectDuration = effectDuration;
    }

    public void setCloudRadiusGrowth(float radiusGrowth) {
        this.radiusGrowth = radiusGrowth;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > this.maxAge) {
            this.detonate();
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("sphereRadius")) {
            this.radius = nbt.getFloat("sphereRadius");
        }
        if (nbt.contains("projectileMaxAge")) {
            this.maxAge = nbt.getInt("projectileMaxAge");
        }
        if (nbt.contains("cloudDuration")) {
            this.duration = nbt.getInt("cloudDuration");
        }
        if (nbt.contains("cloudRadiusGrowth")) {
            this.radius = nbt.getFloat("cloudRadiusGrowth");
        }
        if (nbt.contains("effectDuration")) {
            this.effectDuration = nbt.getInt("effectDuration");
        }
        if (nbt.contains("effectAmp")) {
            this.effectAmp = nbt.getInt("effectAmp");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("sphereRadius", this.radius);
        nbt.putInt("projectileMaxAge", this.maxAge);
        nbt.putInt("cloudDuration", this.duration);
        nbt.putFloat("cloudRadiusGrowth", this.radiusGrowth);
        nbt.putInt("effectDuration", this.effectDuration);
        nbt.putInt("effectAmp", this.effectAmp);
    }
}
