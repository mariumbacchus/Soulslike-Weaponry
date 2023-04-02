package net.soulsweaponry.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.util.ParticleNetworking;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class MoonlightProjectile extends NonArrowProjectile implements GeoEntity {
    
    private static final TrackedData<Integer> POINTS = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TICK_PARTICLES = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MAX_AGE = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> HUGE_EXPLOSION = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> ROTATE_STATE = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private ItemStack stackShotFrom;

    public MoonlightProjectile(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public MoonlightProjectile(EntityType<? extends PersistentProjectileEntity> type, World world, LivingEntity owner) {
        super(type, owner, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(POINTS, 75);
        this.dataTracker.startTracking(TICK_PARTICLES, 4);
        this.dataTracker.startTracking(MAX_AGE, 30);
        this.dataTracker.startTracking(HUGE_EXPLOSION, false);
        this.dataTracker.startTracking(ROTATE_STATE, 0);
    }

    public void setAgeAndPoints(int maxAge, int explosionPoints, int tickParticleAmount) {
        this.dataTracker.set(MAX_AGE, maxAge);
        this.dataTracker.set(POINTS, explosionPoints);
        this.dataTracker.set(TICK_PARTICLES, tickParticleAmount);
    }

    public void setHugeExplosion(boolean bl) {
        this.dataTracker.set(HUGE_EXPLOSION, bl);
    }

    public void setRotateState(RotationState state) {
        for (int i = 0; i < RotationState.values().length; i++) {
            if (state.equals(RotationState.values()[i])) {
                this.dataTracker.set(ROTATE_STATE, i);
            }
        }
    }

    public RotationState getRotateState() {
        return RotationState.values()[this.dataTracker.get(ROTATE_STATE)];
    }

    public int getMaxParticlePoints() {
        return this.dataTracker.get(POINTS);
    }

    public int getTickParticleAmount() {
        return this.dataTracker.get(TICK_PARTICLES);
    }

    public double getMaxAge() {
        return this.dataTracker.get(MAX_AGE);
    }

    public void tick() {
        super.tick();
        Vec3d vec3d = this.getVelocity();
        double e = vec3d.x;
        double f = vec3d.y;
        double g = vec3d.z;
        for (int i = 0; i < this.getTickParticleAmount(); ++i) {
            this.world.addParticle(this.getParticleType(), this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D, this.getZ() + g * (double)i / 4.0D, -e, -f + 0.2D, -g);
        }

        if (this.age > this.getMaxAge()) {
            this.discard(); 
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.discard();
    }

    @Override
    public int getPunch() {
        if (stackShotFrom != null) {
            return EnchantmentHelper.getLevel(Enchantments.KNOCKBACK, stackShotFrom);
        }
        return super.getPunch();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() != null && entityHitResult.getEntity() instanceof LivingEntity && this.asItemStack() != null) {
            float bonus = EnchantmentHelper.getAttackDamage(this.asItemStack(), ((LivingEntity) entityHitResult.getEntity()).getGroup());
            this.setDamage(this.getDamage() + (bonus >= 5 ? bonus * 0.7f : bonus));
        }
        super.onEntityHit(entityHitResult);
        this.discard();
    }

    public void detonateEntity(World world, double x, double y, double z, double points, float sizeModifier) {
        double phi = Math.PI * (3. - Math.sqrt(5.));
        for (int i = 0; i < points; i++) {
            double velocityY = 1 - (i/(points - 1)) * 2;
            double radius = Math.sqrt(1 - velocityY*velocityY);
            double theta = phi * i;
            double velocityX = Math.cos(theta) * radius;
            double velocityZ = Math.sin(theta) * radius;
            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, true, x, y, z, velocityX*sizeModifier, velocityY*sizeModifier, velocityZ*sizeModifier);
        } 
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        if (!this.world.isClient) {
            if (this.dataTracker.get(HUGE_EXPLOSION)) {
                ParticleNetworking.sendServerParticlePacket((ServerWorld) this.world, PacketRegistry.DEATH_EXPLOSION_PACKET_ID, this.getBlockPos(), true);
            } else {
                ParticleNetworking.sendServerParticlePacket((ServerWorld) this.world, PacketRegistry.MOONLIGHT_PARTICLES_ID, this.getBlockPos(), this.getMaxParticlePoints());
            }
        } else {
            if (this.dataTracker.get(HUGE_EXPLOSION)) {
                this.detonateEntity(world, this.getX(), this.getY(), this.getZ(), 750, 0.5f);
            } else {
                this.detonateEntity(world, this.getX(), this.getY(), this.getZ(), this.getMaxParticlePoints(), 0.125f);
            }
        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ENTITY_GENERIC_EXPLODE;
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.GLOW;
    }

    protected float getDragInWater() {
        return 1.01F;
    }

    protected ItemStack asItemStack() {
        return this.stackShotFrom;
    }

    public void setItemStack(ItemStack stackShotFrom) {
        this.stackShotFrom = stackShotFrom;
    }

    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    public static enum RotationState {
        NORMAL,
        SWIPE_FROM_RIGHT,
        SWIPE_FROM_LEFT
    }
}
