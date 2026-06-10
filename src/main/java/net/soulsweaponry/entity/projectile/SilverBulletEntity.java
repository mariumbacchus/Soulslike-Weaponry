package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.soulsweaponry.api.entitystats.EntityPosture;
import net.soulsweaponry.config.EnchantmentConfig;
import net.soulsweaponry.config.GunConfig;
import net.soulsweaponry.entitydata.FrostData;
import net.soulsweaponry.entitydata.PostureData;
import net.soulsweaponry.items.abilities.ChainLightning;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class SilverBulletEntity extends ModPersistentProjectile implements GeoEntity, IPostureLossProjectile {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private int postureLoss;
    private boolean isEthereal;
    private float explosionPower;
    private float chainLightningDamage;
    private float chainLightningRange;
    private int blightCarrier;
    private int freezeAmplifier;
    private int tether;
    private int maxEchoDelay;
    private int maxAge = 60;
    private static final TrackedData<Boolean> ECHO_COPY = DataTracker.registerData(SilverBulletEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> ECHO_TIMER = DataTracker.registerData(SilverBulletEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> RICOCHET_BOUNCES = DataTracker.registerData(SilverBulletEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public SilverBulletEntity(EntityType<? extends SilverBulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public SilverBulletEntity(World world, LivingEntity owner, ItemStack bullet) {
        super(EntityRegistry.SILVER_BULLET_ENTITY_TYPE, owner, world, bullet, null);
    }

    public SilverBulletEntity(EntityType<? extends SilverBulletEntity> entityType, World world, LivingEntity owner, ItemStack bullet) {
        super(entityType, owner, world, bullet, null);
    }

    @Override
    public void tick() {
        if (this.isEchoCopy()) {
            this.reduceEchoCopyTimer();
            if (this.getEchoCopyTimer() > 0) {
                float spread = this.getWidth() * 0.1f;
                if (this.getWorld().isClient) {
                    this.getWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getBodyY(0.5f), this.getZ(),
                            this.random.nextFloat() * spread - spread / 2f, this.random.nextFloat() * spread - spread / 2f, this.random.nextFloat() * spread - spread / 2f);
                }
                return;
            }
        }
        if (this.isNoClip()) {
            this.setPitch(0f);
            this.setYaw(0f);
            Vec3d currentPos = this.getPos();
            Vec3d velocity = this.getVelocity();
            Vec3d nextPos = currentPos.add(velocity);
            EntityHitResult entityHit = ProjectileUtil.raycast(
                    this,
                    currentPos,
                    nextPos,
                    this.getBoundingBox().stretch(velocity).expand(1.0D),
                    e -> e instanceof LivingEntity && e != this.getOwner(), 5D
            );
            if (entityHit != null) {
                this.onEntityHit(entityHit);
                return;
            }
        }
        super.tick();
        if (this.isNoClip()) {
            this.setPitch(0f);
            this.setYaw(0f);
        }
        if (!this.inGround) {
            Vec3d vec3d = this.getVelocity();
            double e = vec3d.x;
            double f = vec3d.y;
            double g = vec3d.z;
            for (int i = 0; i < 2; ++i) {
                this.getWorld().addParticle(this.isEchoCopy() ? ParticleRegistry.ECHO_SMOKE : ParticleTypes.SMOKE, this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D + 0.25f, this.getZ() + g * (double)i / 4.0D, -e*0.2, (-f + 0.2D)*0.2, -g*0.2);
            }
        }
        if (this.age > this.getMaxAge() || this.inGroundTime >= 10) {
            this.discard();
        }
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        for (int i = 0; i < 4; i++) {
            float spread = this.getWidth() * 0.3f;
            this.getWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getBodyY(0.5f), this.getZ(),
                    this.random.nextFloat() * spread - spread / 2f, this.random.nextFloat() * spread - spread / 2f, this.random.nextFloat() * spread - spread / 2f);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (this.isEchoCopy() && type == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            if (entityHitResult.getEntity() == this.getOwner()) {
                return;
            }
        }
        super.onCollision(hitResult);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_STONE_BREAK;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (this.getRicochetBounces() > 0) {
            this.setRicochetBounces(this.getRicochetBounces() - 1);
            Predicate<LivingEntity> isPet = e ->
                    this.getOwner() instanceof LivingEntity owner
                            && e instanceof TameableEntity tame
                            && tame.getOwner() != null
                            && tame.getOwner().equals(owner);

            LivingEntity newTarget = null;
            if (this.getOwner() instanceof LivingEntity owner) {
                LivingEntity target = owner.getAttacking();
                if (target != null && target.isAlive() && !isPet.test(target)
                        && canSee(target) && target != owner) {
                    newTarget = target;
                }
            }
            if (newTarget == null) {
                double searchRadius = 8.0;
                Box box = this.getBoundingBox().expand(searchRadius);
                List<LivingEntity> candidates = this.getWorld().getEntitiesByClass(
                        LivingEntity.class,
                        box,
                        e -> e.isAlive() && e != this.getOwner() && !isPet.test(e)
                );
                newTarget = candidates.stream()
                        .filter(this::canSee)
                        .min(Comparator.comparingDouble(e -> e.squaredDistanceTo(this)))
                        .orElse(null);
            }

            Vec3d newDir;
            if (newTarget != null) {
                Vec3d from = this.getPos();
                Vec3d to = newTarget.getPos().add(0, newTarget.getHeight() * 0.5, 0);
                newDir = to.subtract(from).normalize();
            } else {
                Vec3i face = blockHitResult.getSide().getVector();
                Vec3d normal = new Vec3d(face.getX(), face.getY(), face.getZ()).normalize();
                Vec3d incoming = this.getVelocity().normalize();
                Vec3d reflect = incoming.subtract(normal.multiply(2 * incoming.dotProduct(normal))).normalize();
                // random jitter
                double jitterStrength = 0.2;
                Vec3d randVec = new Vec3d(
                        this.random.nextGaussian(),
                        this.random.nextGaussian(),
                        this.random.nextGaussian()
                ).normalize();
                Vec3d perp = randVec.subtract(reflect.multiply(randVec.dotProduct(reflect))).normalize();
                newDir = reflect.add(perp.multiply(jitterStrength)).normalize();
            }
            double oldSpeed = this.getVelocity().length();
            double newSpeed = Math.max(oldSpeed - 1.5, 1.5);
            Vec3d vel = newDir.multiply(newSpeed);
            this.setVelocity(vel);
            this.velocityDirty = true;
            return;
        }
        super.onBlockHit(blockHitResult);
        this.discard();
    }

    /**
     * Returns true if there are no opaque blocks between the bullet and the entity's mid‐body.
     */
    private boolean canSee(LivingEntity e) {
        Vec3d start = this.getPos();
        Vec3d end = e.getPos().add(0, e.getHeight() * 0.5, 0);
        BlockHitResult ray = this.getWorld().raycast(new RaycastContext(
                start, end,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                this
        ));
        // MISS means nothing blocked the path
        return ray.getType() == HitResult.Type.MISS;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity target) {
            this.applyPostureLoss(target);
            int posture = PostureData.getPosture(target);
            if (!EntityPosture.isPostureDisabled(target) && posture >= EntityPosture.getMaxPostureLoss(target)) {
                this.onPostureBreak(target);
            }
            if (target.hasInvertedHealingAndHarm()) {
                this.setDamage(this.getDamage() + (GunConfig.silver_bullet_undead_bonus_damage / this.getVelocity().length()));
            }
            if (this.getOwner() instanceof LivingEntity owner) {
                if (this.chainLightningDamage > 0f) {
                    ChainLightning.trigger(this.getWorld(), target, owner, this.getChainLightningDamage(), this.getChainLightningRange());
                }
            }
            if (this.getBlightCarrier() > 0) {
                StatusEffectInstance instance = target.getStatusEffect(EffectRegistry.BLIGHT);
                if (instance != null) {
                    target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLIGHT, (int) EnchantmentConfig.blight_carrier_enchant_blight_duration, instance.getAmplifier() + this.getBlightCarrier() - 1));
                } else {
                    target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLIGHT, (int) EnchantmentConfig.blight_carrier_enchant_blight_duration, this.getBlightCarrier() - 1));
                }
            }
        }
        super.onEntityHit(entityHitResult);
        if (this.getTether() > 0 && entityHitResult.getEntity() instanceof LivingEntity target) {
            Entity shooter = this.getOwner();
            if (shooter instanceof LivingEntity) {
                double resistanceFactor = Math.max(0.0, 1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                Vec3d toShooter = new Vec3d(shooter.getX() - target.getX(), 0.0, shooter.getZ() - target.getZ());
                double distance = toShooter.length();
                double minRange = 1 + this.getTether() * EnchantmentConfig.tether_enchant_min_activation_range_per_level;
                if (distance > minRange) {
                    double pullStrength = this.getTether() * EnchantmentConfig.tether_enchant_drag_mod * resistanceFactor;
                    Vec3d pullVel = toShooter.normalize().multiply(pullStrength);
                    double maxAllowedPull = distance - minRange;
                    if (pullVel.length() > maxAllowedPull) {
                        pullVel = pullVel.normalize().multiply(maxAllowedPull);
                    }
                    target.addVelocity(pullVel.x, 0.1, pullVel.z);
                }
            }
        }
        if (this.explosionPower > 0f && this.getWorld() instanceof ServerWorld serverWorld) {
            WeaponUtil.simulateExplosion(serverWorld, this.getOwner(), this.explosionPower, this.getX(), this.getY(), this.getZ());
        }
        this.discard();
    }

    private void onPostureBreak(LivingEntity target) {
        if (this.getFreezeAmplifier() > 0) {
            if (this.getOwner() != null) {
                FrostData.setFrostSource(target, this.getOwner());
            }
            target.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING, (int) EnchantmentConfig.frostsilver_enchant_permafrost_duration, this.getFreezeAmplifier() - 1));
            target.getWorld().playSound(null, target.getBlockPos(), SoundEvents.ENTITY_SKELETON_CONVERTED_TO_STRAY, SoundCategory.HOSTILE, 1f, 1f);
        }
    }

    public int getMaxAge() {
        return this.maxAge + this.getMaxEchoDelay();
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("postureLoss")) {
            this.setPostureLoss(nbt.getInt("postureLoss"));
        }
        if (nbt.contains("ethereal")) {
            this.isEthereal = nbt.getBoolean("ethereal");
        }
        if (nbt.contains("explosionPower")) {
            this.explosionPower = nbt.getFloat("explosionPower");
        }
        if (nbt.contains("chainLightningDamage")) {
            this.chainLightningDamage = nbt.getFloat("chainLightningDamage");
        }
        if (nbt.contains("chainLightningRange")) {
            this.chainLightningRange = nbt.getFloat("chainLightningRange");
        }
        if (nbt.contains("blightCarrier")) {
            this.blightCarrier = nbt.getInt("blightCarrier");
        }
        if (nbt.contains("freezeAmplifier")) {
            this.freezeAmplifier = nbt.getInt("freezeAmplifier");
        }
        if (nbt.contains("echoCopy")) {
            this.setEchoCopy(nbt.getBoolean("echoCopy"));
        }
        if (nbt.contains("echoCopyTimer")) {
            this.setEchoCopyTimer(nbt.getInt("echoCopyTimer"));
        }
        if (nbt.contains("tether")) {
            this.tether = nbt.getInt("tether");
        }
        if (nbt.contains("ricochetBounces")) {
            this.setRicochetBounces(nbt.getInt("ricochetBounces"));
        }
        if (nbt.contains("maxEchoDelay")) {
            this.setMaxEchoDelay(nbt.getInt("maxEchoDelay"));
        }
        if (nbt.contains("maxAge")) {
            this.maxAge = nbt.getInt("maxAge");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("postureLoss", this.getPostureLoss());
        nbt.putBoolean("ethereal", this.isEthereal);
        nbt.putFloat("explosionPower", this.explosionPower);
        nbt.putFloat("chainLightningDamage", this.chainLightningDamage);
        nbt.putFloat("chainLightningRange", this.chainLightningRange);
        nbt.putInt("blightCarrier", this.blightCarrier);
        nbt.putInt("freezeAmplifier", this.freezeAmplifier);
        nbt.putBoolean("echoCopy", this.isEchoCopy());
        nbt.putInt("echoCopyTimer", this.getEchoCopyTimer());
        nbt.putInt("tether", this.tether);
        nbt.putInt("ricochetBounces", this.getRicochetBounces());
        nbt.putInt("maxEchoDelay", this.getMaxEchoDelay());
        nbt.putInt("maxAge", this.maxAge);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ECHO_COPY, false);
        builder.add(ECHO_TIMER, 0);
        builder.add(RICOCHET_BOUNCES, 0);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ItemRegistry.SILVER_BULLET.getDefaultStack();
    }

    public void setEthereal(boolean ethereal) {
        this.isEthereal = ethereal;
    }

    /**
     * Remember that noClip is the server & client side version of whether the entity is ethereal or not,
     * this variable is just a simple check.
     */
    public boolean isEthereal() {
        return this.isEthereal;
    }

    public void setPostureLoss(int postureLoss) {
        this.postureLoss = postureLoss;
    }

    public int getPostureLoss() {
        return postureLoss;
    }

    public void setExplosionPower(float explosionPower) {
        this.explosionPower = explosionPower;
    }

    public void setChainLightningDamage(float chainLightningDamage) {
        this.chainLightningDamage = chainLightningDamage;
    }

    public float getChainLightningDamage() {
        return this.chainLightningDamage;
    }

    public void setChainLightningRange(float chainLightningRange) {
        this.chainLightningRange = chainLightningRange;
    }

    public float getChainLightningRange() {
        return this.chainLightningRange;
    }

    public void setBlightCarrier(int blightCarrier) {
        this.blightCarrier = blightCarrier;
    }

    public int getBlightCarrier() {
        return this.blightCarrier;
    }

    public void setFreezeAmplifier(int freezeAmplifier) {
        this.freezeAmplifier = freezeAmplifier;
    }

    public int getFreezeAmplifier() {
        return this.freezeAmplifier;
    }

    public void setEchoCopy(boolean bl) {
        this.dataTracker.set(ECHO_COPY, bl);
    }

    public boolean isEchoCopy() {
        return this.dataTracker.get(ECHO_COPY);
    }

    public void setEchoCopyTimer(int amount) {
        this.dataTracker.set(ECHO_TIMER, amount);
    }

    private void reduceEchoCopyTimer() {
        this.setEchoCopyTimer(this.getEchoCopyTimer() - 1);
    }

    private int getEchoCopyTimer() {
        return this.dataTracker.get(ECHO_TIMER);
    }

    public void setTether(int tether) {
        this.tether = tether;
    }

    public int getTether() {
        return this.tether;
    }

    public void setRicochetBounces(int ricochetBounces) {
        this.dataTracker.set(RICOCHET_BOUNCES, ricochetBounces);
    }

    public int getRicochetBounces() {
        return this.dataTracker.get(RICOCHET_BOUNCES);
    }

    public int getMaxEchoDelay() {
        return this.maxEchoDelay;
    }

    public void setMaxEchoDelay(int maxEchoDelay) {
        this.maxEchoDelay = maxEchoDelay;
    }

    @Override
    public boolean shouldAllowArrowSticking() {
        return false;
    }
}
