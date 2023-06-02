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
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class DayStalker extends BossEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    public int deathTicks;
    public int ticksUntillDead = 100;

    public DayStalker(EntityType<? extends DayStalker> entityType, World world) {
        super(entityType, world, Color.YELLOW);
        this.drops.add(WeaponRegistry.DAWNBREAKER);
        this.drops.add(ItemRegistry.LORD_SOUL_ROSE); // add custom lord soul
    }

    private PlayState chains(AnimationState<?> state) {
        state.getController().setAnimation(RawAnimation.begin().then("idle_chains_2", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }


    private PlayState idles(AnimationState<?> state) {
        state.getController().setAnimation(RawAnimation.begin().then("idle_2", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    //Testing animations, more will come soon.
    private PlayState attacks(AnimationState<?> state) {
        state.getController().setAnimation(RawAnimation.begin().then("chaos_storm_2", Animation.LoopType.LOOP));
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
            CustomDeathHandler.deathExplosionEvent(world, this.getBlockPos(), false, SoundRegistry.DAWNBREAKER_EVENT);
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
        controllers.add(new AnimationController<>(this, "idles", 0, this::idles));
        controllers.add(new AnimationController<>(this, "attacks", 0, this::attacks));
        controllers.add(new AnimationController<>(this, "chains", 0, this::chains));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
}
