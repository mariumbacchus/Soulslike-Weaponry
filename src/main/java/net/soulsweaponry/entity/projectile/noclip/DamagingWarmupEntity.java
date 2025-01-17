package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;

import java.util.List;

public abstract class DamagingWarmupEntity extends NoClipWarmupEntity {

    private boolean startedAttack;
    private int ticksLeft = 20;
    private static final TrackedData<Boolean> EMERGE = DataTracker.registerData(DamagingWarmupEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public DamagingWarmupEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.ticksLeft = this.getMaxTicks();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            this.reduceWarmup(1);
            if (this.getWarmup() < 0) {
                if (this.getWarmup() == -7) {
                    this.setEmerge(true);
                    List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(0.2D));
                    for (LivingEntity livingEntity : list) {
                        if (livingEntity.isTeammate(this.getOwner()) || this.isOwner(livingEntity) || !livingEntity.isAlive() || livingEntity.isInvulnerable()) {
                            continue;
                        }
                        boolean wasHit;
                        if (this.getOwner() instanceof LivingEntity) {
                            wasHit = livingEntity.damage(this.getWorld().getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner()), (float) this.getDamage() + this.getBonusDamage(livingEntity));
                        } else {
                            wasHit = livingEntity.damage(this.getWorld().getDamageSources().mobProjectile(this, null), (float) this.getDamage() + this.getBonusDamage(livingEntity));
                        }
                        this.applyDamageEffects(wasHit, livingEntity);
                    }
                    this.onTrigger();
                }
                if (!this.startedAttack) {
                    this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_ATTACK_SOUND);
                    this.startedAttack = true;
                }
                if (--this.ticksLeft < 0) {
                    this.discard();
                }
            }
        }
    }

    /**
     * Is by default 20. Override this to make the entities last longer so any animations can finish.
     */
    public int getMaxTicks() {
        return 20;
    }

    @Override
    public void handleStatus(byte status) {
        super.handleStatus(status);
        if (status == EntityStatuses.PLAY_ATTACK_SOUND) {
            if (!this.isSilent()) {
                this.handleSoundStatus(status);
            }
        }
    }

    public void setEmerge(boolean bl) {
        this.dataTracker.set(EMERGE, bl);
    }

    /**
     * Data-tracked boolean set to true at te same time as damaging effects are triggered.
     */
    public boolean getEmerge() {
        return this.dataTracker.get(EMERGE);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(EMERGE, false);
    }

    /**
     * Trigger custom sounds when damaging effects are applied.
     */
    public abstract void handleSoundStatus(byte status);

    /**
     * Apply other effects such as knockup or status effects at the same time as damaging effects are triggered.
     */
    public abstract void applyDamageEffects(boolean wasHit, LivingEntity target);

    /**
     * Called on server side at the same time as damaging effects are triggered.
     */
    public abstract void onTrigger();

    /**
     * Override this if additional damage should be added based on the target, for example if it is undead.
     */
    public float getBonusDamage(LivingEntity target) {
        return 0f;
    }
}
