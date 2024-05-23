package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import net.soulsweaponry.entity.AreaEffectSphere;
import net.soulsweaponry.entity.mobs.DayStalker;
import net.soulsweaponry.entity.mobs.NightProwler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.entitydata.ParryData;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Optional;
import java.util.UUID;

public class GrowingFireball extends UntargetableFireball implements IAnimatable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private int maxAge = 126;
    private boolean hasChangedCourse;
    private static final TrackedData<Optional<UUID>> TARGET_UUID = DataTracker.registerData(GrowingFireball.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Float> RADIUS = DataTracker.registerData(GrowingFireball.class, TrackedDataHandlerRegistry.FLOAT);
    private float radiusGrowth;

    public GrowingFireball(EntityType<? extends GrowingFireball> entityType, World world) {
        super(entityType, world);
    }

    public GrowingFireball(World world, Entity owner) {
        super(EntityRegistry.GROWING_FIREBALL_ENTITY.get(), world);
        this.setOwner(owner);
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && !(entity instanceof DayStalker) && !(entity instanceof NightProwler);
    }

    @Override
    public void tick() {
        super.tick();
        float radius = this.getRadius();
        if (world.isClient && this.random.nextBoolean()) {
            int points = MathHelper.floor(this.getRadius() * 4f);
            AreaEffectSphere.randomParticleBox(this.world, this.getX(), this.getY() + this.getRadius()/2, this.getZ(), points, this.getRadius() * 0.5f, ParticleTypes.FLAME, this.random);
        }
        if (this.radiusGrowth != 0.0F) {
            radius += this.radiusGrowth;
            if (radius < 0.5F) {
                this.discard();
                return;
            }
            this.setRadius(radius);
        }
        Entity target;
        if (!this.world.isClient && this.age >= this.getMaxAge() && !this.hasChangedCourse && (target = this.getSavedTarget((ServerWorld) this.world)) != null) {
            double f = target.getX() - this.getX();
            double g = target.getBodyY(0.5) - (this.getBodyY(0.5D));
            double h = target.getZ() - this.getZ();
            this.setVelocity(f, g, h, 1f, 1f);
            this.hasChangedCourse = true;
        }
        if (this.age >= this.getMaxAge() * 3) {
            this.detonate();
        }
        if (!(this.getOwner() instanceof DayStalker) && !this.world.isClient) {
            for (Entity entity : this.world.getOtherEntities(this, this.getBoundingBox())) {
                if (!this.isOwner(entity) && entity instanceof LivingEntity) {
                    this.detonate();
                }
            }
        }
    }

    public float getExplosionPower() {
        return 1f + this.getRadius();
    }

    @Override
    protected float getDrag() {
        return 1f;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            if (((EntityHitResult) hitResult).getEntity() instanceof PlayerEntity player) {
                if (ParryData.successfulParry(player, false, DamageSource.explosion(this.getOwner() instanceof LivingEntity ? (LivingEntity) this.getOwner() : player))) {
                    if (!this.world.isClient) {
                        Vec3d vec3d = player.getRotationVector();
                        this.setVelocity(vec3d);
                        this.powerX = vec3d.x * 0.1;
                        this.powerY = vec3d.y * 0.1;
                        this.powerZ = vec3d.z * 0.1;
                        this.setOwner(player);
                        return;
                    }
                }
            }
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
            boolean bl = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
            Explosion.DestructionType destructionType = bl ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
            this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), this.getExplosionPower(), bl, destructionType);
            this.discard();
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TARGET_UUID, Optional.empty());
        this.getDataTracker().startTracking(RADIUS, 0.5F);
    }

    public void setRadius(float radius) {
        if (!this.world.isClient) {
            this.getDataTracker().set(RADIUS, MathHelper.clamp(radius, 0.0F, 32.0F));
        }
    }

    public void calculateDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.calculateDimensions();
        this.setPosition(d, e, f);
    }

    public float getRadius() {
        return this.getDataTracker().get(RADIUS);
    }

    public void setTargetUuid(@Nullable UUID uuid) {
        this.dataTracker.set(TARGET_UUID, Optional.ofNullable(uuid));
    }

    public UUID getTargetUuid() {
        return this.dataTracker.get(TARGET_UUID).orElse(null);
    }

    @Nullable
    public Entity getSavedTarget(ServerWorld world) {
        return world.getEntity(this.getTargetUuid());
    }

    public void setMaxAge(int age) {
        this.maxAge = age;
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    public void setRadiusGrowth(float radiusGrowth) {
        this.radiusGrowth = radiusGrowth;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("MaxAge", this.getMaxAge());
        if (this.getTargetUuid() != null) {
            nbt.putUuid("TargetUuid", this.getTargetUuid());
        }
        nbt.putBoolean("ChangedCourse", this.hasChangedCourse);
        nbt.putFloat("RadiusPerTick", this.radiusGrowth);
        nbt.putFloat("Radius", this.getRadius());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("MaxAge")) {
            this.setMaxAge(nbt.getInt("MaxAge"));
        }
        UUID uUID = null;
        if (nbt.containsUuid("TargetUuid")) {
            uUID = nbt.getUuid("TargetUuid");
        }
        if (uUID != null) {
            try {
                this.setTargetUuid(uUID);
            } catch (Throwable ignored) {}
        }
        if (nbt.contains("ChangedCourse")) {
            this.hasChangedCourse = nbt.getBoolean("ChangedCourse");
        }
        if (nbt.contains("RadiusPerTick")) {
            this.radiusGrowth = nbt.getFloat("RadiusPerTick");
        }
        if (nbt.contains("Radius")) {
            this.setRadius(nbt.getFloat("Radius"));
        }
    }

    private <E extends IAnimatable> PlayState predicate (AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("spin2", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}