package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.AreaEffectSphere;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class DragonStaffProjectile extends DragonFireballEntity {

    private final ItemStack stack;
    private float radius = 2f;

    public DragonStaffProjectile(EntityType<? extends DragonStaffProjectile> entityType, World world) {
        super(entityType, world);
        this.stack = new ItemStack(WeaponRegistry.DRAGON_STAFF);
    }

    public DragonStaffProjectile(World world, LivingEntity user, ItemStack stack) {
        super(EntityRegistry.DRAGON_STAFF_PROJECTILE, world);
        this.setOwner(user);
        this.setRotation(user.getYaw(), user.getPitch());
        this.stack = stack;
    }

    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult)hitResult);
        } else if (type == HitResult.Type.BLOCK) {
            this.onBlockHit((BlockHitResult)hitResult);
        }
        if (type != HitResult.Type.MISS) {
            this.emitGameEvent(GameEvent.PROJECTILE_LAND, this.getOwner());
        }
        this.detonate();
    }

    private void detonate() {
        if (!this.world.isClient) {
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(4.0, 2.0, 4.0));
            AreaEffectSphere areaEffectCloudEntity = new AreaEffectSphere(this.world, this.getX(), this.getY(), this.getZ());
            Entity entity = this.getOwner();
            if (entity instanceof LivingEntity) {
                areaEffectCloudEntity.setOwner((LivingEntity) entity);
            }
            areaEffectCloudEntity.setParticleType(ParticleTypes.DRAGON_BREATH);
            areaEffectCloudEntity.setRadius(this.radius + (float) WeaponUtil.getEnchantDamageBonus(this.stack) / 2.5f);
            areaEffectCloudEntity.setDuration(200 + WeaponUtil.getEnchantDamageBonus(this.stack) * 20);
            areaEffectCloudEntity.setRadiusGrowth((3.0f - areaEffectCloudEntity.getRadius() + (float) WeaponUtil.getEnchantDamageBonus(this.stack) / 2.5f) / (float) areaEffectCloudEntity.getDuration());
            areaEffectCloudEntity.addEffect(new StatusEffectInstance(EffectRegistry.HALLOWED_DRAGON_MIST, 50, ConfigConstructor.dragon_staff_aura_strength));
            if (!list.isEmpty()) {
                for (LivingEntity livingEntity : list) {
                    double d = this.squaredDistanceTo(livingEntity);
                    if (!(d < 16.0)) continue;
                    areaEffectCloudEntity.setPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                    break;
                }
            }
            this.world.syncWorldEvent(WorldEvents.DRAGON_BREATH_CLOUD_SPAWNS, this.getBlockPos(), this.isSilent() ? -1 : 1);
            this.world.spawnEntity(areaEffectCloudEntity);
            this.discard();
        }
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age >= 100) {
            this.detonate();
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("itemStack")) {
            this.stack.setNbt((NbtCompound) nbt.get("itemStack"));
        }
        if (nbt.contains("sphereRadius")) {
            this.radius = nbt.getFloat("sphereRadius");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.stack.getNbt() != null) {
            nbt.put("itemStack", this.stack.getNbt());
        }
        nbt.putFloat("sphereRadius", this.radius);
    }
}