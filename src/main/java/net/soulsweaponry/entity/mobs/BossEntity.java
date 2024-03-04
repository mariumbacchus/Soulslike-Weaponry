package net.soulsweaponry.entity.mobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.entity.boss.BossBar.Style;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.soulsweaponry.util.IAnimatedDeath;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BossEntity extends HostileEntity implements IAnimatedDeath {
    
    protected final ServerBossBar bossBar;

    protected BossEntity(EntityType<? extends HostileEntity> entityType, World world, Color barColor) {
        super(entityType, world);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), barColor, Style.NOTCHED_10)).setDarkenSky(true);
        this.experiencePoints = this.getXp();
    }

    public abstract int getXp();

    @Override
    protected void mobTick() {
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
    }

    /**
     * Gets the reduced cooldown in ticks for the boss' next attack based on number of attackers,
     * increased if the attackers are players. Will make huge raids more interesting.
     * <p> Returns {@code 5} for each player, and {@code 2} for other entities, such as
     * summons.
     */
    public int getReducedCooldownAttackers() {
        List<LivingEntity> attackers = this.getAttackers();
        int reduced = 0;
        for (LivingEntity entity : attackers) {
            reduced += entity instanceof PlayerEntity ? 5 : 2;
        }
        return reduced;
    }

    /**
     * Get all the attacking entities within 10 blocks radius from the bounding box of the boss.
     */
    public List<LivingEntity> getAttackers() {
        Box box = this.getBoundingBox().expand(10);
        List<Entity> entities = this.world.getOtherEntities(this, box);
        ArrayList<LivingEntity> attackers = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && ((LivingEntity)entity).getAttacking() == this) {
                attackers.add((LivingEntity) entity);
            }
        }
        return attackers;
    }

    /**
     * Returns a list of all attacking {@code players} only within 10 blocks radius of the boss' bounding box.
     */
    public List<PlayerEntity> getAttackingPlayers() {
        List<LivingEntity> entities = this.getAttackers();
        ArrayList<PlayerEntity> players = new ArrayList<>();
        for (LivingEntity entity : entities) {
            if (entity instanceof PlayerEntity) {
                players.add((PlayerEntity) entity);
            }
        }
        return players;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (this.hasCustomName()) {
           this.bossBar.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        this.setDeath();
    }

    @Override
    protected boolean shouldAlwaysDropXp() {
        return true;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    @Override
    public abstract void updatePostDeath();

    @Override
    public abstract int getTicksUntilDeath();

    @Override
    public abstract int getDeathTicks();

    @Override
    public abstract void setDeath();

    @Override
    public abstract boolean isFireImmune();

    @Override
    public abstract boolean isUndead();

    @Override
    public abstract EntityGroup getGroup();

    @Override
    public abstract boolean disablesShield();
}
