package net.soulsweaponry.entity.mobs;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.entity.boss.BossBar.Style;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class DayStalker extends HostileEntity implements IAnimatable, IAnimationTickable {

    private final ServerBossBar bossBar;
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public int deathTicks;
    public int ticksUntillDead = 100; //1 sekund = 20 ticks

    public DayStalker(EntityType<? extends DayStalker> entityType, World world) {
        super(entityType, world);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), Color.YELLOW, Style.PROGRESS)).setDarkenSky(true);
        this.experiencePoints = 500;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        
        return PlayState.CONTINUE;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        //this.setDeath(true);
    }
    
    @Override
    protected void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks == this.ticksUntillDead && !this.world.isClient()) {
            this.world.sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            CustomDeathHandler.deathExplosionEvent(world, this.getBlockPos(), false, SoundRegistry.DAWNBREAKER_EVENT);
            this.remove(RemovalReason.KILLED);
        }
    }

    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        ItemEntity[] mainLoot = {
            this.dropItem(WeaponRegistry.DAWNBREAKER), 
            this.dropItem(ItemRegistry.LORD_SOUL_ROSE)
        };
        for (int i = 0; i < mainLoot.length; i++) {
            if (mainLoot[i] != null) {
                mainLoot[i].setCovetedItem();
            }
        }
    }

    @Override
    public int tickTimer() {
        return this.age;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));    
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    //Track boss health
    protected void mobTick() {
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
    }

    //Register and de-register bossbar
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (this.hasCustomName()) {
           this.bossBar.setName(this.getDisplayName());
        }
  
    }
  
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }
  
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }
    
}
