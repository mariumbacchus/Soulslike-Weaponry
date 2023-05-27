package net.soulsweaponry.entity.mobs;

import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

public class NightProwler extends BossEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    public int deathTicks;
    public int ticksUntillDead = 100;

    public NightProwler(EntityType<? extends NightProwler> entityType, World world) {
        super(entityType, world, Color.PURPLE);
        this.drops.add(WeaponRegistry.SOUL_REAPER);
        this.drops.add(WeaponRegistry.FORLORN_SCYTHE);
        this.drops.add(ItemRegistry.LORD_SOUL_PURPLE); // add custom lord soul
    }

    private PlayState predicate(AnimationState<?> state) {
        
        return PlayState.CONTINUE;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        //this.setDeath(true);
    }
    
    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks == this.ticksUntillDead && !this.world.isClient()) {
            this.world.sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            CustomDeathHandler.deathExplosionEvent(world, this.getBlockPos(), true, SoundRegistry.DAWNBREAKER_EVENT);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public int getTicksUntilDeath() {
        return 0;
    }

    @Override
    public int getDeathTicks() {
        return 0;
    }

    @Override
    public void setDeath() {

    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean isUndead() {
        return false;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.DEFAULT;
    }

    @Override
    public boolean disablesShield() {
        return true;
    }

    @Override
    public double getBossMaxHealth() {
        return 1;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
}
