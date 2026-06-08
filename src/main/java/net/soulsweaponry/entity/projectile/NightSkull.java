package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.soulsweaponry.config.BossConfig;
import net.soulsweaponry.entity.AreaEffectSphere;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class NightSkull extends ModPersistentProjectile implements GeoEntity {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private boolean destroyBlocks;
    private float explosionPower;
    public static final TrackedData<Boolean> CHARGED = DataTracker.registerData(NightSkull.class, TrackedDataHandlerRegistry.BOOLEAN);

    public NightSkull(World world, LivingEntity owner, float damage, boolean charged, boolean destroyBlocks, float explosionPower, int maxAge) {
        super(EntityRegistry.NIGHT_SKULL, world);
        this.setOwner(owner);
        this.setDamage(damage);
        this.setAllowArrowSticking(false);
        this.setCharged(charged);
        this.destroyBlocks = destroyBlocks;
        this.explosionPower = explosionPower;
        this.setMaxAge(maxAge);
    }

    public NightSkull(EntityType<? extends NightSkull> entityType, World world) {
        super(entityType, world);
        this.setDamage(10D);
        this.explosionPower = 2f;
        this.setAllowArrowSticking(false);
    }

    @Override
    public boolean isImmuneToExplosion(Explosion explosion) {
        return true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(CHARGED, false);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (entityHitResult.getEntity() instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(EffectRegistry.DECAY, 60, 0));
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 0));
        }
        this.detonate();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.detonate();
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundRegistry.NIGHT_SKULL_DIE;
    }

    @Override
    protected boolean canHit(Entity entity) {
        if (entity instanceof ProjectileEntity || this.isOwner(entity)) {
            return false;
        }
        return super.canHit(entity);
    }

    @Override
    public boolean isGlowing() {
        return BossConfig.night_prowler_eclipse_skulls_glow;
    }

    private void detonate() {
        if (!this.getWorld().isClient) {
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), this.explosionPower, false, this.destroyBlocks ? World.ExplosionSourceType.MOB : World.ExplosionSourceType.TRIGGER);
            if (this.isCharged()) {
                this.summonCloud();
            }
            this.discard();
        }
    }

    private void summonCloud() {
        List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(4.0, 2.0, 4.0));
        AreaEffectSphere areaEffectCloudEntity = new AreaEffectSphere(this.getWorld(), this.getX(), this.getY(), this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaEffectCloudEntity.setOwner((LivingEntity)entity);
        }
        areaEffectCloudEntity.setParticleAmountModifier(3f);
        areaEffectCloudEntity.setParticleType(ParticleRegistry.DARK_STAR);
        areaEffectCloudEntity.setRadius(0.5f);
        areaEffectCloudEntity.setDuration(100);
        areaEffectCloudEntity.setRadiusGrowth((2.5f - areaEffectCloudEntity.getRadius()) / (float)areaEffectCloudEntity.getDuration());
        areaEffectCloudEntity.addEffect(new StatusEffectInstance(EffectRegistry.DECAY, 60, 0));
        areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 0));
        areaEffectCloudEntity.setAffectOwner(false);
        if (!list.isEmpty()) {
            for (LivingEntity livingEntity : list) {
                double d = this.squaredDistanceTo(livingEntity);
                if (!(d < 16.0)) continue;
                areaEffectCloudEntity.setPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                break;
            }
        }
        this.getWorld().spawnEntity(areaEffectCloudEntity);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > this.getMaxAge()) {
            this.detonate();
        }
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    private PlayState idle(AnimationState<?> state) {
        state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "idle", 0, this::idle));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    protected ItemStack asItemStack() {
        return Items.WITHER_SKELETON_SKULL.getDefaultStack();
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.WITHER_SKELETON_SKULL.getDefaultStack();
    }

    @Override
    public boolean shouldAllowArrowSticking() {
        return false;
    }

    public boolean isCharged() {
        return this.dataTracker.get(CHARGED);
    }

    public void setCharged(boolean bl) {
        this.dataTracker.set(CHARGED, bl);
    }

    public float getExplosionPower() {
        return this.explosionPower;
    }

    public void setExplosionPower(float explosionPower) {
        this.explosionPower = explosionPower;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("charged", this.isCharged());
        nbt.putFloat("explosionPower", this.getExplosionPower());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setCharged(nbt.getBoolean("charged"));
        this.setExplosionPower(nbt.getFloat("explosionPower"));
    }
}
