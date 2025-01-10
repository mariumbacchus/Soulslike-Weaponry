package net.soulsweaponry.entity.projectile;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.Objects;

public class MoonlightProjectile extends NonArrowProjectile implements GeoEntity {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final TrackedData<Integer> EXPLOSION_PARTICLE_COUNT = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TRAIL_PARTICLE_COUNT = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> AREA_PARTICLE_COUNT = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MAX_AGE = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> EXPLOSION_EXPANSION = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> MODEL_ROTATION = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<ParticleEffect> EXPLOSION_PARTICLE = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.PARTICLE);
    private static final TrackedData<ParticleEffect> TRAIL_PARTICLE = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.PARTICLE);
    private static final TrackedData<ParticleEffect> AREA_PARTICLE = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.PARTICLE);
    private static final TrackedData<Integer> EFFECT_TICKS = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> EFFECT_AMPLIFIER = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<String> APPLIED_EFFECT_ID = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.STRING);
    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);

    public MoonlightProjectile(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public MoonlightProjectile(EntityType<? extends PersistentProjectileEntity> type, World world, LivingEntity owner, ItemStack stack) {
        super(type, owner, world, stack);
    }

    public MoonlightProjectile(EntityType<? extends PersistentProjectileEntity> type, World world, LivingEntity owner) {
        super(type, owner, world, WeaponRegistry.MOONLIGHT_GREATSWORD.getDefaultStack());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(EXPLOSION_PARTICLE_COUNT, 75);
        this.dataTracker.startTracking(TRAIL_PARTICLE_COUNT, 4);
        this.dataTracker.startTracking(AREA_PARTICLE_COUNT, 0);
        this.dataTracker.startTracking(MAX_AGE, 30);
        this.dataTracker.startTracking(EXPLOSION_EXPANSION, 0.125f);
        this.dataTracker.startTracking(MODEL_ROTATION, 0);
        this.dataTracker.startTracking(EXPLOSION_PARTICLE, ParticleTypes.SOUL_FIRE_FLAME);
        this.dataTracker.startTracking(TRAIL_PARTICLE, ParticleTypes.GLOW);
        this.dataTracker.startTracking(AREA_PARTICLE, ParticleRegistry.NIGHTFALL_PARTICLE);
        this.dataTracker.startTracking(EFFECT_TICKS, 0);
        this.dataTracker.startTracking(APPLIED_EFFECT_ID, "");
        this.dataTracker.startTracking(EFFECT_AMPLIFIER, 0);
    }

    public void setAgeAndPoints(int maxAge, int explosionPoints, int tickParticleAmount) {
        this.dataTracker.set(MAX_AGE, maxAge);
        this.dataTracker.set(EXPLOSION_PARTICLE_COUNT, explosionPoints);
        this.dataTracker.set(TRAIL_PARTICLE_COUNT, tickParticleAmount);
    }

    public void setExplosionExpansion(float particleExpansion) {
        this.dataTracker.set(EXPLOSION_EXPANSION, particleExpansion);
    }

    public void setModelRotation(int degrees) {
        this.dataTracker.set(MODEL_ROTATION, degrees);
    }

    public int getModelRotation() {
        return this.dataTracker.get(MODEL_ROTATION);
    }

    public int getMaxParticlePoints() {
        return this.dataTracker.get(EXPLOSION_PARTICLE_COUNT);
    }

    public int getTrailParticleCount() {
        return this.dataTracker.get(TRAIL_PARTICLE_COUNT);
    }

    public int getMaxAge() {
        return this.dataTracker.get(MAX_AGE);
    }

    public float getParticleExplosionExpansion() {
        return this.dataTracker.get(EXPLOSION_EXPANSION);
    }

    public int getAreaParticleCount() {
        return this.dataTracker.get(AREA_PARTICLE_COUNT);
    }

    public void setAreaParticleCount(int particleCount) {
        this.dataTracker.set(AREA_PARTICLE_COUNT, particleCount);
    }

    public void tick() {
        super.tick();
        Vec3d vec3d = this.getVelocity();
        double e = vec3d.x;
        double f = vec3d.y;
        double g = vec3d.z;
        for (int i = 0; i < this.getTrailParticleCount(); ++i) {
            this.getWorld().addParticle(this.getTrailParticleType(), this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D, this.getZ() + g * (double)i / 4.0D, -e, -f + 0.2D, -g);
        }
        for (int i = 0; i < this.getAreaParticleCount(); i++) {
            this.getWorld().addParticle(ParticleRegistry.NIGHTFALL_PARTICLE, this.getParticleX(0.5f), this.getRandomBodyY() - 0.5f, this.getParticleZ(0.5f), 0, 0, 0);
        }
        if (this.age > this.getMaxAge()) {
            this.discard(); 
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.discard();
    }

    @Override
    public int getPunch() {
        if (this.asItemStack() != null) {
            return EnchantmentHelper.getLevel(Enchantments.KNOCKBACK, this.asItemStack());
        }
        return super.getPunch();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity living && this.asItemStack() != null) {
            float bonus = EnchantmentHelper.getAttackDamage(this.asItemStack(), living.getGroup());
            this.setDamage(this.getDamage() + (bonus >= 5 ? bonus * 0.7f : bonus));
        }
        super.onEntityHit(entityHitResult);
        if (this.getAppliedEffectTicks() > 0 && this.getAppliedEffectId().isEmpty() && entityHitResult.getEntity() != null) {
            entityHitResult.getEntity().setFireTicks(this.getAppliedEffectTicks());
        }
        if (!this.getAppliedEffectId().isEmpty() && entityHitResult.getEntity() instanceof LivingEntity target) {
            target.addStatusEffect(new StatusEffectInstance(this.getAppliedEffect(), this.getAppliedEffectTicks(), this.getEffectAmplifier()));
        }
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
            world.addParticle(this.getExplosionParticleType(), true, x, y, z, velocityX*sizeModifier, velocityY*sizeModifier, velocityZ*sizeModifier);
        } 
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        this.detonateEntity(getWorld(), this.getX(), this.getY(), this.getZ(), this.getMaxParticlePoints(), this.getParticleExplosionExpansion());
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ENTITY_GENERIC_EXPLODE;
    }

    protected float getDragInWater() {
        return 1.01F;
    }

    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    public void setExplosionParticleType(ParticleEffect particle) {
        this.getDataTracker().set(EXPLOSION_PARTICLE, particle);
    }

    public ParticleEffect getExplosionParticleType() {
        return this.getDataTracker().get(EXPLOSION_PARTICLE);
    }

    public void setTrailParticleType(ParticleEffect particle) {
        this.getDataTracker().set(TRAIL_PARTICLE, particle);
    }

    public ParticleEffect getTrailParticleType() {
        return this.getDataTracker().get(TRAIL_PARTICLE);
    }

    public void setAreaParticleType(ParticleEffect particle) {
        this.getDataTracker().set(AREA_PARTICLE, particle);
    }

    public ParticleEffect getAreaParticleType() {
        return this.getDataTracker().get(AREA_PARTICLE);
    }

    /**
     * Duration of the custom applied effect in ticks. If no custom id is set to {@code #setAppliedEffectId} and
     * this has greater value than 0, then the effect will default to fire.
     * @param ticks duration in ticks
     */
    public void setAppliedEffectTicks(int ticks) {
        this.dataTracker.set(EFFECT_TICKS, ticks);
    }

    public int getAppliedEffectTicks() {
        return this.dataTracker.get(EFFECT_TICKS);
    }

    public String getAppliedEffectId() {
        return this.dataTracker.get(APPLIED_EFFECT_ID);
    }

    /**
     * Apply a status effect when hitting an entity. Setting this string to empty or not updating it while
     * {@code #getAppliedEffectTicks} is greater than 0 makes the applied effect default to fire.
     * @param string string id of the effect
     */
    public void setAppliedEffectId(String string) {
        this.dataTracker.set(APPLIED_EFFECT_ID, string);
    }

    /**
     * Apply a status effect when hitting an entity. Not updating it while
     * {@code #getAppliedEffectTicks} is greater than 0 makes the applied effect default to fire.
     * @param effect effect
     */
    public void setAppliedStatusEffect(StatusEffect effect) {
        this.setAppliedEffectId(Objects.requireNonNull(Registries.STATUS_EFFECT.getId(effect)));
    }

    public void setAppliedEffectId(Identifier identifier) {
        this.setAppliedEffectId(identifier.toString());
    }

    public StatusEffect getAppliedEffect() {
        return Registries.STATUS_EFFECT.get(new Identifier(this.getAppliedEffectId()));
    }

    public int getEffectAmplifier() {
        return this.dataTracker.get(EFFECT_AMPLIFIER);
    }

    public void setEffectAmplifier(int amplifier) {
        this.dataTracker.set(EFFECT_AMPLIFIER, amplifier);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("ExplosionParticle", this.getExplosionParticleType().asString());
        nbt.putString("TrailParticle", this.getExplosionParticleType().asString());
        nbt.putString("AreaParticle", this.getAreaParticleType().asString());
        nbt.putInt("ExplosionParticleCount", this.getMaxParticlePoints());
        nbt.putInt("TrailParticleCount", this.getTrailParticleCount());
        nbt.putInt("MaxAge", this.getMaxAge());
        nbt.putFloat("ParticleExplosionExpansion", this.getParticleExplosionExpansion());
        nbt.putInt("ModelRotation", this.getModelRotation());
        nbt.putString("AppliedEffectId", this.getAppliedEffectId());
        nbt.putInt("AppliedEffectAmplifier", this.getEffectAmplifier());
        nbt.putInt("AppliedEffectTicks", this.getAppliedEffectTicks());
        nbt.putInt("AreaParticleCount", this.getAreaParticleCount());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("ExplosionParticle", 8)) {
            try {
                this.setExplosionParticleType(ParticleEffectArgumentType.readParameters(new StringReader(nbt.getString("ExplosionParticle")), Registries.PARTICLE_TYPE.getReadOnlyWrapper()));
            } catch (CommandSyntaxException var5) {
                LOGGER.warn("Couldn't load custom particle {}", nbt.getString("ExplosionParticle"), var5);
            }
        }
        if (nbt.contains("TrailParticle", 8)) {
            try {
                this.setTrailParticleType(ParticleEffectArgumentType.readParameters(new StringReader(nbt.getString("TrailParticle")), Registries.PARTICLE_TYPE.getReadOnlyWrapper()));
            } catch (CommandSyntaxException var5) {
                LOGGER.warn("Couldn't load custom particle {}", nbt.getString("TrailParticle"), var5);
            }
        }
        if (nbt.contains("AreaParticle", 8)) {
            try {
                this.setAreaParticleType(ParticleEffectArgumentType.readParameters(new StringReader(nbt.getString("AreaParticle")), Registries.PARTICLE_TYPE.getReadOnlyWrapper()));
            } catch (CommandSyntaxException var5) {
                LOGGER.warn("Couldn't load custom particle {}", nbt.getString("AreaParticle"), var5);
            }
        }
        if (nbt.contains("ExplosionParticleCount") && nbt.contains("TrailParticleCount") && nbt.contains("MaxAge")) {
            this.setAgeAndPoints(nbt.getInt("MaxAge"), nbt.getInt("ExplosionParticleCount"), nbt.getInt("TrailParticleCount"));
        }
        if (nbt.contains("ParticleExplosionExpansion")) {
            this.setExplosionExpansion(nbt.getFloat("ParticleExplosionExpansion"));
        }
        if (nbt.contains("ModelRotation")) {
            this.setModelRotation(nbt.getInt("ModelRotation"));
        }
        if (nbt.contains("AppliedEffectId")) {
            this.setAppliedEffectId(nbt.getString("AppliedEffectId"));
        }
        if (nbt.contains("AppliedEffectAmplifier")) {
            this.setEffectAmplifier(nbt.getInt("AppliedEffectAmplifier"));
        }
        if (nbt.contains("AppliedEffectTicks")) {
            this.setAppliedEffectTicks(nbt.getInt("AppliedEffectTicks"));
        }
        if (nbt.contains("AreaParticleCount")) {
            this.setAreaParticleCount(nbt.getInt("AreaParticleCount"));
        }
    }
}
