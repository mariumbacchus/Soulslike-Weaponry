package net.soulsweaponry.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.FrostData;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

public class MoonlightProjectile extends ModPersistentProjectile implements GeoEntity {

    private static final TrackedData<Integer> MODEL_ROTATION = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> EFFECT_TICKS = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> EFFECT_AMPLIFIER = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<String> APPLIED_EFFECT_ID = DataTracker.registerData(MoonlightProjectile.class, TrackedDataHandlerRegistry.STRING);
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private float enchantBonusDamageMod = ConfigConstructor.moonlight_shortsword_projectile_bonus_enchant_damage_mod;//TODO make into variable that ShootMoonlight abilities call and change

    public MoonlightProjectile(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.quickInit();
    }
    
    public MoonlightProjectile(EntityType<? extends PersistentProjectileEntity> type, World world, LivingEntity owner, ItemStack weaponStack) {
        super(type, owner, world, weaponStack, weaponStack);
        this.quickInit();
    }

    public MoonlightProjectile(EntityType<? extends PersistentProjectileEntity> type, World world, LivingEntity owner) {
        super(type, owner, world, WeaponRegistry.MOONLIGHT_GREATSWORD.getDefaultStack(), WeaponRegistry.MOONLIGHT_GREATSWORD.getDefaultStack());
        this.quickInit();
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return WeaponRegistry.MOONLIGHT_GREATSWORD.getDefaultStack();
    }

    private void quickInit() {
        this.setMaxAge(30);
        this.setAreaParticle(ParticleRegistry.NIGHTFALL_PARTICLE);
        this.setDespawnParticle(ParticleTypes.SOUL_FIRE_FLAME);
        this.setDespawnParticleCount(75);
        this.setTrailParticle(ParticleTypes.GLOW);
        this.setDespawnParticleCount(4);
        this.setDespawnParticleExpansion(0.125f);
        this.setAllowArrowSticking(false);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(MODEL_ROTATION, 0);
        builder.add(EFFECT_TICKS, 0);
        builder.add(EFFECT_AMPLIFIER, 0);
        builder.add(APPLIED_EFFECT_ID, "");
    }

    public void setAgeAndPoints(int maxAge, int explosionPoints, byte tickParticleAmount) {
        this.setMaxAge(maxAge);
        this.setDespawnParticleCount(explosionPoints);
        this.setTrailParticleCount(tickParticleAmount);
    }

    public void setModelRotation(int degrees) {
        this.dataTracker.set(MODEL_ROTATION, degrees);
    }

    public int getModelRotation() {
        return this.dataTracker.get(MODEL_ROTATION);
    }

    public void tick() {
        super.tick();
        Vec3d vec3d = this.getVelocity();
        double e = vec3d.x;
        double f = vec3d.y;
        double g = vec3d.z;
        for (int i = 0; i < this.getTrailParticleCount(); ++i) {
            this.getWorld().addParticle(this.getTrailParticle(), this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D, this.getZ() + g * (double)i / 4.0D, -e, -f + 0.2D, -g);
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
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity living && this.getWorld() instanceof ServerWorld serverWorld) {
            DamageSource damageSource = this.getDamageSources().arrow(this, this.getOwner());
            float bonus = EnchantmentHelper.getDamage(serverWorld, this.getItemStack(), living, damageSource, 0);
            //TODO the bonus line is already calculated in super.onEnityHit, check with sout with and without line and with and without enchants
            this.setDamage(this.getDamage() + bonus * this.enchantBonusDamageMod);
        }
        super.onEntityHit(entityHitResult);
        if (this.getAppliedEffectTicks() > 0 && this.getAppliedEffectId().isEmpty() && entityHitResult.getEntity() != null) {
            entityHitResult.getEntity().setFireTicks(this.getAppliedEffectTicks());
        }
        if (!this.getAppliedEffectId().isEmpty() && entityHitResult.getEntity() instanceof LivingEntity target) {
            RegistryEntry<StatusEffect> effect = this.getAppliedEffect();
            if (effect.equals(EffectRegistry.FREEZING) && this.getOwner() != null) {
                FrostData.setFrostSource(target, this.getOwner());
            }
            target.addStatusEffect(new StatusEffectInstance(effect, this.getAppliedEffectTicks(), this.getEffectAmplifier()));
        }
        this.discard();
    }

    public void detonateEntity(World world, double x, double y, double z, double points, float sizeModifier) {
        for (Vec3d vec : ParticleHandler.getSphereParticleCords(points, sizeModifier)) {
            world.addParticle(this.getDespawnParticle(), x, y, z, vec.x, vec.y, vec.z);
        }
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        this.detonateEntity(getWorld(), this.getX(), this.getY(), this.getZ(), this.getDespawnParticleCount(), this.getDespawnParticleExpansion());
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ENTITY_GENERIC_EXPLODE.value();
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

    /**
     * Duration of the custom applied effect in ticks. If no custom id is set to {@code #setAppliedEffectId} and
     * this has greater value than 0, then the effect will default to fire.
     * @param ticks duration in ticks
     */
    public void setAppliedEffectDuration(int ticks) {
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

    public RegistryEntry<StatusEffect> getAppliedEffect() {
        var effect = Registries.STATUS_EFFECT.getEntry(Identifier.of(this.getAppliedEffectId()));
        if (effect.isPresent()) {
            return effect.get();
        }
        return EffectRegistry.FREEZING;
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
        nbt.putInt("ModelRotation", this.getModelRotation());
        nbt.putString("AppliedEffectId", this.getAppliedEffectId());
        nbt.putInt("AppliedEffectAmplifier", this.getEffectAmplifier());
        nbt.putInt("AppliedEffectTicks", this.getAppliedEffectTicks());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
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
            this.setAppliedEffectDuration(nbt.getInt("AppliedEffectTicks"));
        }
    }

    @Override
    public boolean shouldAllowArrowSticking() {
        return false;
    }
}