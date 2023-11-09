package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.soulsweaponry.entity.mobs.DayStalker;
import net.soulsweaponry.entity.mobs.NightProwler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class ChaosOrbEntity extends Entity implements GeoEntity, FlyingItemEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private int lifespan;

    public ChaosOrbEntity(EntityType<? extends ChaosOrbEntity> entityType, World world) {
        super(entityType, world);
    }

    private PlayState predicate(AnimationState<?> event){
        event.getController().setAnimation(RawAnimation.begin().then("spin", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void tick() {
        super.tick();
        Vec3d vec3d = this.getVelocity();
        double d = this.getX() + vec3d.x;
        double e = this.getY() + vec3d.y + 0.1f;
        double f = this.getZ() + vec3d.z;
        double g = vec3d.horizontalLength();
        if (!this.world.isClient) {
            double h = this.getX() - d;
            double i = this.getZ() - f;
            float j = (float)Math.sqrt(h * h + i * i);
            float k = (float)MathHelper.atan2(i, h);
            double l = MathHelper.lerp(0.0025, g, j);
            double m = vec3d.y;
            if (j < 1.0f) {
                l *= 0.8;
                m *= 0.8;
            }
            vec3d = new Vec3d(Math.cos(k) * l, m + (1D - m) * (double)0.015f, Math.sin(k) * l);
            this.setVelocity(vec3d);
        }
        if (this.isTouchingWater()) {
            for (int p = 0; p < 4; ++p) {
                this.world.addParticle(ParticleTypes.BUBBLE, d - vec3d.x * 0.25, e - vec3d.y * 0.25, f - vec3d.z * 0.25, vec3d.x, vec3d.y, vec3d.z);
            }
        } else {
            this.world.addParticle(ParticleTypes.PORTAL, d - vec3d.x * 0.25 + this.random.nextDouble() * 0.6 - 0.3, e - vec3d.y * 0.25 - 0.5, f - vec3d.z * 0.25 + this.random.nextDouble() * 0.6 - 0.3, vec3d.x, vec3d.y, vec3d.z);
        }
        if (!this.world.isClient && world instanceof ServerWorld) {
            this.setPosition(d, e, f);
            ++this.lifespan;
            if (this.lifespan > 100 && !this.world.isClient) {
                this.playSound(SoundEvents.ENTITY_ENDER_EYE_DEATH, 1.0f, 1.0f);
                this.discard();
                this.world.syncWorldEvent(WorldEvents.EYE_OF_ENDER_BREAKS, this.getBlockPos(), 0);

                NightProwler prowler = new NightProwler(EntityRegistry.NIGHT_PROWLER, this.getWorld());
                DayStalker stalker = new DayStalker(EntityRegistry.DAY_STALKER, this.getWorld());
                prowler.setPos(this.getX(), this.getY(), this.getZ());
                stalker.setPos(this.getX(), this.getY(), this.getZ());
                prowler.setVelocity(1 / 5f, 0.1f, - 1 / 5f);
                stalker.setVelocity(- 1 / 5f, 0.1f, 1 / 5f);
                boolean fly = this.random.nextBoolean();
                prowler.setFlying(fly);
                stalker.setFlying(!fly);
                prowler.setAttackAnimation(NightProwler.Attacks.SPAWN);
                stalker.setAttackAnimation(DayStalker.Attacks.SPAWN);
                prowler.setPartnerUuid(stalker.getUuid());
                stalker.setPartnerUuid(prowler.getUuid());
                this.getWorld().spawnEntity(stalker);
                this.getWorld().spawnEntity(prowler);
                for (ServerPlayerEntity player : ((ServerWorld) getWorld()).getPlayers()) {
                    getWorld().playSound(null, player.getBlockPos(), SoundRegistry.HARD_BOSS_SPAWN_EVENT, SoundCategory.HOSTILE, 0.3f, 1f);
                }
            }
        } else {
            this.setPos(d, e, f);
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(ItemRegistry.CHAOS_ORB);
    }
}
